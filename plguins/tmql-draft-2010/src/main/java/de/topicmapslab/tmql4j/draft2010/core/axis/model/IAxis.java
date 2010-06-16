package de.topicmapslab.tmql4j.draft2010.core.axis.model;

import java.util.Collection;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public interface IAxis {

	public Collection<?> navigate(final Construct source, final Topic type) throws TMQLRuntimeException;
		
	public IToken getIdentifier();
	
}
