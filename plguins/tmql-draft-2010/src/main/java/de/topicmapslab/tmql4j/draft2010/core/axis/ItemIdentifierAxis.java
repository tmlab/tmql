package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.tokens.ItemIdentifier;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class ItemIdentifierAxis implements IAxis {

	
	public IToken getIdentifier() {
		return new ItemIdentifier();
	}

	
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<String> identifiers = HashUtil.getHashSet();

		for (Locator locator : source.getItemIdentifiers()) {
			identifiers.add(locator.toExternalForm());
		}

		return identifiers;
	}

}
