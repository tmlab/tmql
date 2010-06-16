package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Reifiable;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.AxisReifier;

public class ReifierAxis implements IAxis {

	@Override
	public IToken getIdentifier() {
		return new AxisReifier();
	}

	@Override
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Topic> reifiers = HashUtil.getHashSet();

		if (source instanceof Reifiable) {
			Topic reifier = ((Reifiable) source).getReifier();
			if (type != null
					&& TypeHierarchyUtils.getTypes(reifier, true)
							.contains(type)) {
				reifiers.add(reifier);
			} else if (type == null) {
				reifiers.add(reifier);
			}
		}

		return reifiers;
	}
}
