package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.tokens.Parent;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class ParentAxis implements IAxis {

	
	public IToken getIdentifier() {
		return new Parent();
	}

	
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Construct> parents = HashUtil.getHashSet();
		if (!(source instanceof TopicMap)) {
			parents.add(source.getParent());
		}
		return parents;
	}

}
