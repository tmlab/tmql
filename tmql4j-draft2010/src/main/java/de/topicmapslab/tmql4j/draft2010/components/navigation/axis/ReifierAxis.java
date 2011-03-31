package de.topicmapslab.tmql4j.draft2010.components.navigation.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Reifiable;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.AxisReifier;
import de.topicmapslab.tmql4j.draft2010.util.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * The reifier axis returns the reifier of a construct.
 * @author Sven Krosse
 *
 */
public class ReifierAxis implements IAxis {

	/**
	 * {@inheritDoc}
	 */
	public IToken getIdentifier() {
		return new AxisReifier();
	}

	/**
	 * {@inheritDoc}
	 */
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
