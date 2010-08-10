package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.Typed;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.tokens.DirectType;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class DirectTypeAxis implements IAxis {

	
	public IToken getIdentifier() {
		return new DirectType();
	}

	
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Topic> types = HashUtil.getHashSet();

		if (source instanceof Typed) {
			types.add(((Typed) source).getType());
		} else if (source instanceof Topic) {
			types.addAll(((Topic) source).getTypes());
		}
		// to be discussed
		else if (source instanceof Variant) {
			types.add(((Variant) source).getParent().getType());
		}

		/*
		 * filter by type
		 */
		if (type != null) {
			Set<Topic> filters = TypeHierarchyUtils.getSupertypes(type, true);
			filters.add(type);
			types.retainAll(filters);
		}

		return types;
	}

}
