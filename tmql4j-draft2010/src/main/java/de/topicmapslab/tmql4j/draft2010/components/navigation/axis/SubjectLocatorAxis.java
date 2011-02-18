package de.topicmapslab.tmql4j.draft2010.components.navigation.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.SubjectLocator;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.HashUtil;
/**
 * The subject-locator axis returns all subject-locators of a topic
 * @author Sven Krosse
 *
 */
public class SubjectLocatorAxis implements IAxis {

	/**
	 * {@inheritDoc}
	 */
	public IToken getIdentifier() {
		return new SubjectLocator();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<String> locators = HashUtil.getHashSet();

		if (source instanceof Topic) {
			for (Locator locator : ((Topic) source).getSubjectLocators()) {
				locators.add(locator.toExternalForm());
			}
		}

		return locators;
	}

}
