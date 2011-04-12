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

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.Typed;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisTyped;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * Class definition representing the typed axis.
 * <p>
 * In forward direction this step computes all typed of the topic type.
 * </p>
 * <p>
 * In backward direction this step produces the type of the given typed. The
 * optional item has no relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TypedNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public TypedNavigationAxis() {
		super(AxisTyped.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends Construct> getBackwardNavigationResultClass(Object construct) throws NavigationException {
		return Topic.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends Construct> getForwardNavigationResultClass(Object construct) throws NavigationException {
		return Typed.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigateBackward(Object construct, Object optional) throws NavigationException {
		return new TypesNavigationAxis().navigateForward(construct, optional);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigateForward(Object construct, Object optional) throws NavigationException {
		if (construct instanceof Topic) {
			Topic topic = (Topic) construct;
			TypeInstanceIndex index = getTopicMap().getIndex(TypeInstanceIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			if (!index.isAutoUpdated()) {
				index.reindex();
			}
			Collection<Typed> list = HashUtil.getHashSet();
			if (optional == null || TmdmSubjectIdentifier.isTmdmAssociation(optional)) {
				list.addAll(index.getAssociations(topic));
			}
			if (optional == null || TmdmSubjectIdentifier.isTmdmName(optional)) {
				list.addAll(index.getNames(topic));
			}
			if (optional == null || TmdmSubjectIdentifier.isTmdmRole(optional)) {
				list.addAll(index.getRoles(topic));
			}
			if (optional == null || TmdmSubjectIdentifier.isTmdmOccurrence(optional)) {
				list.addAll(index.getOccurrences(topic));
			}
			return list;
		}
		throw new NavigationException("Anchor type '" + construct.getClass().getSimpleName() + "' not supported by the typed axis.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportsBackwardNavigation(Object construct, Construct optional) throws NavigationException {
		if (construct instanceof Topic) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportsForwardNavigation(Object construct, Object optional) throws NavigationException {
		if (construct instanceof Topic) {
			return true;
		}
		return false;
	}

}
