package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.tokens.Value;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class ValueAxis implements IAxis {

	@Override
	public IToken getIdentifier() {
		return new Value();
	}

	@Override
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<String> values = HashUtil.getHashSet();

		if (source instanceof Name) {
			values.add(((Name) source).getValue());
		} else if (source instanceof Occurrence) {
			values.add(((Occurrence) source).getValue());
		} else if (source instanceof Variant) {
			values.add(((Variant) source).getValue());
		} else if (source instanceof Locator) {
			values.add(((Locator) source).toExternalForm());
		}

		return values;
	}

}
