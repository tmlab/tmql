package de.topicmapslab.tmql4j.draft2010.components.navigation.axis;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Supertype;
import de.topicmapslab.tmql4j.draft2010.util.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.HashUtil;
/**
 * The supertype axis returns all supertypes of a topic type
 * @author Sven Krosse
 *
 */
public class SupertypeAxis implements IAxis {

	/**
	 * {@inheritDoc}
	 */
	public IToken getIdentifier() {
		return new Supertype();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Topic> types = HashUtil.getHashSet();

		if (source instanceof Topic) {
			types
					.addAll(TypeHierarchyUtils.getSupertypes((Topic) source,
							true));
		}

		/*
		 * filter by type
		 */
		if (type != null) {
			List<Topic> filters = TypeHierarchyUtils.getSupertypes(type, true);
			filters.add(type);
			types.retainAll(filters);
		}

		return types;
	}

}
