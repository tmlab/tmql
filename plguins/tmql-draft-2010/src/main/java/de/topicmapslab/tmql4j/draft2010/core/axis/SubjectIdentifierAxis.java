package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.tokens.SubjectIdentifier;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class SubjectIdentifierAxis implements IAxis {

	@Override
	public IToken getIdentifier() {
		return new SubjectIdentifier();
	}

	@Override
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<String> identifiers = HashUtil.getHashSet();

		if (source instanceof Topic) {
			for (Locator locator : ((Topic) source).getSubjectIdentifiers()) {
				identifiers.add(locator.toExternalForm());
			}
		}

		return identifiers;
	}

}
