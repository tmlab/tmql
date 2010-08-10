package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class OccurrenceAxis implements IAxis {

	
	public IToken getIdentifier() {
		return new de.topicmapslab.tmql4j.draft2010.tokens.Occurrence();
	}

	
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Occurrence> occurrences = HashUtil.getHashSet();

		if (source instanceof Topic) {
			Topic topic = (Topic) source;
			if (type != null) {
				// TODO handle default type
				Set<Topic> types = TypeHierarchyUtils.getSupertypes(type, true);
				types.add(type);
				for (Occurrence occurrence : topic.getOccurrences()) {
					if (types.contains(occurrence.getType())) {
						occurrences.add(occurrence);
					}
				}
			}else{
				occurrences.addAll(topic.getOccurrences());
			}
		}

		return occurrences;
	}

}
