package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.tokens.DirectInstance;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class DirectInstanceAxis implements IAxis {

	@Override
	public IToken getIdentifier() {
		return new DirectInstance();
	}

	@Override
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
			Set<Topic> filters = TypeHierarchyUtils.getSupertypes(type, true);
			filters.add(type);
			instances.retainAll(filters);
		}

		return instances;
	}

}
