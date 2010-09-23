package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class TopicAxis implements IAxis {

	/**
	 * {@inheritDoc}
	 */
	
	public IToken getIdentifier() {
		return new de.topicmapslab.tmql4j.draft2010.tokens.Topic();
	}

	/**
	 * {@inheritDoc}
	 */
	
	public Collection<?> navigate(Construct source,
			final org.tmapi.core.Topic type) throws TMQLRuntimeException {
		Set<Topic> topics = HashUtil.getHashSet();
		if (source instanceof TopicMap) {
			if (type != null) {
				final Set<Topic> types = TypeHierarchyUtils.getSubtypes(type,
						true);
				types.add(type);
				for (Topic t : types) {
					TypeInstanceIndex index = ((TopicMap)source).getIndex(TypeInstanceIndex.class);
					if ( !index.isOpen()){
						index.open();
					}
					topics.addAll(index.getTopics(t));
				}
			} else {
				topics.addAll(((TopicMap) source).getTopics());
			}
		}
		return topics;
	}

}
