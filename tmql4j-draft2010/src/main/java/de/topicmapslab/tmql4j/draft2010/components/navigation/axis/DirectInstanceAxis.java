package de.topicmapslab.tmql4j.draft2010.components.navigation.axis;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DirectInstance;
import de.topicmapslab.tmql4j.draft2010.util.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.HashUtil;

public class DirectInstanceAxis implements IAxis {

	/**
	 * {@inheritDoc}
	 */
	public IToken getIdentifier() {
		return new DirectInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Topic> instances = HashUtil.getHashSet();

		if (source instanceof Topic) {
			instances.addAll(TypeHierarchyUtils.getInstances((Topic) source,
					false));
		}

		/*
		 * filter by type
		 */
		if (type != null) {
			List<Topic> filters = TypeHierarchyUtils.getSupertypes(type, true);
			filters.add(type);
			instances.retainAll(filters);
		}

		return instances;
	}

}
