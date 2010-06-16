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
import de.topicmapslab.tmql4j.draft2010.tokens.Type;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class TypeAxis implements IAxis {

	@Override
	public IToken getIdentifier() {
		return new Type();
	}

	@Override
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Topic> types = HashUtil.getHashSet();

		if (source instanceof Typed) {
			types.addAll(TypeHierarchyUtils.getTypes((Typed) source, true));
		} else if (source instanceof Topic) {
			types.addAll(TypeHierarchyUtils.getTypes((Topic) source, true));
		}
		// to be discussed
		else if (source instanceof Variant) {
			types.addAll(TypeHierarchyUtils.getTypes(((Variant) source)
					.getParent(), true));
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
