package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.DatatypeAware;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.tokens.Datatype;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class DatatypeAxis implements IAxis {

	@Override
	public IToken getIdentifier() {
		return new Datatype();
	}

	@Override
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<String> locators = HashUtil.getHashSet();

		if (source instanceof DatatypeAware) {
			locators.add(((DatatypeAware) source).getDatatype()
					.toExternalForm());
		}

		return locators;
	}

}
