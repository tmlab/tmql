package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Reifiable;
import org.tmapi.core.Topic;
import org.tmapi.core.Typed;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.tokens.Reified;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class ReifiedAxis implements IAxis {

	@Override
	public IToken getIdentifier() {
		return new Reified();
	}

	@Override
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Reifiable> reifiables = HashUtil.getHashSet();

		/*
		 * source should be an instance of topic
		 */
		if (source instanceof Topic) {
			/*
			 * get reified
			 */
			Reifiable reifiable = ((Topic) source).getReified();
			if (type != null) {
				if (reifiable instanceof Typed
						&& TypeHierarchyUtils.getTypes((Typed) reifiable, true)
								.equals(type)) {
					reifiables.add(reifiable);
				} else if (reifiable instanceof Variant
						&& TypeHierarchyUtils.getTypes(
								((Variant) reifiable).getParent(), true)
								.equals(type)) {
					reifiables.add(reifiable);
				}
			} else {
				reifiables.add(reifiable);
			}
		}
		return reifiables;
	}
}
