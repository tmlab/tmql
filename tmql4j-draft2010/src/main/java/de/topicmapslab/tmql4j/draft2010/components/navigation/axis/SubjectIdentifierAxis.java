package de.topicmapslab.tmql4j.draft2010.components.navigation.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.SubjectIdentifier;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.HashUtil;

public class SubjectIdentifierAxis implements IAxis {

	/**
	 * {@inheritDoc}
	 */
	public IToken getIdentifier() {
		return new SubjectIdentifier();
	}

	/**
	 * {@inheritDoc}
	 */
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
