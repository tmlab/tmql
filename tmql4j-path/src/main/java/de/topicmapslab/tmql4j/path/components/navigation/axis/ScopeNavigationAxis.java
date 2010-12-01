/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.navigation.axis;

import java.util.Collection;
import java.util.LinkedList;

import org.tmapi.core.Construct;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;
import org.tmapi.index.ScopedIndex;

import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.components.navigation.NavigationAxis;
import de.topicmapslab.tmql4j.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.path.exception.NavigationException;

/**
 * Class definition representing the scope axis.
 * <p>
 * In forward direction, this navigation leads from characteristics (names and
 * occurrences) and association items to their scope.
 * </p>
 * <p>
 * In backward direction, this navigation leads from a topic to all associations
 * and characteristic items in that scope. The optional item has no relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ScopeNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public ScopeNavigationAxis() {
		super(NavigationAxis.scope);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getBackwardNavigationResultClass(Object construct)
			throws NavigationException {
		return Scoped.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getForwardNavigationResultClass(Object construct)
			throws NavigationException {
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateBackward(Object construct, Object optional)
			throws NavigationException {
		/*
		 * check if construct is a topic
		 */
		if (construct instanceof Topic) {
			Topic scope = (Topic) construct;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();

			ScopedIndex index = getTopicMap().getIndex(ScopedIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			/*
			 * check if topic is theme of an association
			 */
			set.addAll(index.getAssociations(scope));
			/*
			 * check if topic is theme of a name
			 */
			set.addAll(index.getNames(scope));
			/*
			 * check if topic is theme of an occurrence
			 */
			set.addAll(index.getOccurrences(scope));
			/*
			 * check if topic is theme of a variant
			 */
			set.addAll(index.getVariants(scope));

			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateForward(Object construct, Object optional)
			throws NavigationException {
		/*
		 * check if construct is scoped
		 */
		if (construct instanceof Scoped) {
			Scoped scoped = (Scoped) construct;
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();

			set.addAll(scoped.getScope());
			return set;
		}

		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct,
			Construct optional) throws NavigationException {
		if (construct instanceof Topic) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsForwardNavigation(Object construct, Object optional)
			throws NavigationException {
		if (construct instanceof Scoped) {
			return true;
		}
		return false;
	}

}
