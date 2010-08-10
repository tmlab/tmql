package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class VariantAxis implements IAxis {

	
	public IToken getIdentifier() {
		return new de.topicmapslab.tmql4j.draft2010.tokens.Variant();
	}

	
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Variant> variants = HashUtil.getHashSet();
		if (source instanceof Name) {
			variants.addAll(((Name) source).getVariants());
		}
		return variants;
	}

}
