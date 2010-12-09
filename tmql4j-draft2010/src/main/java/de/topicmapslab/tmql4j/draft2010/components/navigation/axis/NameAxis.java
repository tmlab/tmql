package de.topicmapslab.tmql4j.draft2010.components.navigation.axis;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.util.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * The name axis returns all names of a topic
 * @author Sven Krosse
 *
 */
public class NameAxis implements IAxis {

	/**
	 * {@inheritDoc}
	 */
	public IToken getIdentifier() {
		return new de.topicmapslab.tmql4j.draft2010.grammar.lexical.Name();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Name> names = HashUtil.getHashSet();

		if (source instanceof Topic) {
			Topic topic = (Topic) source;
			if (type != null) {
				List<Topic> types = TypeHierarchyUtils.getSupertypes(type, true);
				types.add(type);
				for (Name name : topic.getNames()) {
					if (types.contains(name.getType())) {
						names.add(name);
					}
				}
			} else {
				names.addAll(topic.getNames());
			}
		}

		return names;
	}

}
