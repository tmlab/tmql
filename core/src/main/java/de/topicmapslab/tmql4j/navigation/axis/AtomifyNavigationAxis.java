/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.navigation.axis;

import java.util.Collection;
import java.util.LinkedList;

import org.tmapi.core.Construct;
import org.tmapi.core.DatatypeAware;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.TopicMap;
import org.tmapi.index.LiteralIndex;

import de.topicmapslab.tmql4j.common.utility.XmlSchemeDatatypes;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;
import de.topicmapslab.tmql4j.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.navigation.NavigationAxis;
import de.topicmapslab.tmql4j.navigation.exception.InvalidValueException;
import de.topicmapslab.tmql4j.navigation.exception.NavigationException;

/**
 * Class definition representing the atomify axis.
 * <p>
 * If the value is a name or occurrence item, in forward direction this step
 * schedules the item for atomification, i.e. marks the item to be converted to
 * the atomic value (integer, string, etc.) within the item. The item is
 * eventually converted into an atom according to the atomification rules. The
 * optional item has no relevance.
 * </p>
 * <p>
 * If the value is an atom, in backward direction this step de-atomifies
 * immediately the atom and returns all names and occurrences where this atom is
 * used as data value. Also here the optional item has no relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class AtomifyNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public AtomifyNavigationAxis() {
		super(NavigationAxis.atomify);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getBackwardNavigationResultClass(
			Object construct) throws NavigationException {
		return Construct.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getForwardNavigationResultClass(Object construct)
			throws NavigationException {
		return Object.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateBackward(Object construct, Construct optional)
			throws NavigationException {
		if (construct instanceof Object) {
			TopicMap map = getTopicMap();
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			/*
			 * get literal index
			 */
			LiteralIndex index = (LiteralIndex) map
					.getIndex(LiteralIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			/*
			 * check if occurrence with literal exists
			 */
			set.addAll(index.getOccurrences(construct.toString()));
			/*
			 * check if names with literal exists
			 */
			set.addAll(index.getNames(construct.toString()));
			try {
				/*
				 * check if literal is a locator
				 */
				Locator locator = map.createLocator(construct.toString());
				Construct c = map.getTopicBySubjectIdentifier(locator);
				/*
				 * check if locator is subject-identifier
				 */
				if (c != null) {
					set.add(locator);
				}
				/*
				 * check if locator is subject-locator
				 */
				else {
					c = map.getTopicBySubjectLocator(locator);
					if (c != null) {
						set.add(locator);
					}
					/*
					 * check if locator is item-indicator
					 */
					else {
						c = map.getConstructByItemIdentifier(locator);
						if (c != null) {
							set.add(locator);
						}
					}
				}
			} catch (Exception e) {
				// NOTHING TO DO
			}
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
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();
		/*
		 * check if construct is an occurrence or a variant
		 */
		if (construct instanceof DatatypeAware) {
			DatatypeAware aware = (DatatypeAware) construct;
			try {
				set.add(deatomify(aware));
			} catch (Exception e) {
				throw new NavigationException(e);
			}
			return set;
		}
		/*
		 * check if construct is a name
		 */
		else if (construct instanceof Name) {
			Name name = (Name) construct;
			set.add(name.getValue());
			return set;
		}
		/*
		 * check if construct is a locator
		 */
		else if (construct instanceof Locator) {
			set.add(((Locator) construct).getReference());
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct,
			Construct optional) throws NavigationException {
		if (construct instanceof Object && optional instanceof TopicMap) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsForwardNavigation(Object construct, Object optional)
			throws NavigationException {
		if (construct instanceof Name || construct instanceof DatatypeAware) {
			return true;
		}
		return false;
	}

	/**
	 * Internal method to de-serialize the given string-representation to its
	 * 
	 * @param literal
	 *            the literal to de-serialize
	 * @return the de-serialized instance
	 */
	private Object deatomify(final DatatypeAware aware) throws Exception {
		final String ref = XmlSchemeDatatypes.toExternalForm(aware
				.getDatatype().getReference());
		if (ref.equalsIgnoreCase(XmlSchemeDatatypes.XSD_INTEGER)) {
			return aware.integerValue();
		} else if (ref.equalsIgnoreCase(XmlSchemeDatatypes.XSD_INT)) {
			return aware.intValue();
		} else if (ref.equalsIgnoreCase(XmlSchemeDatatypes.XSD_FLOAT)) {
			return aware.floatValue();
		} else if (ref.equalsIgnoreCase(XmlSchemeDatatypes.XSD_ANYURI)) {
			return aware.locatorValue();
		} else if (ref.equalsIgnoreCase(XmlSchemeDatatypes.XSD_LONG)) {
			return aware.longValue();
		} else if (ref.equalsIgnoreCase(XmlSchemeDatatypes.XSD_DECIMAL)) {
			return aware.decimalValue();
		} else if (ref.equalsIgnoreCase(XmlSchemeDatatypes.XSD_DATE)) {
			return LiteralUtils.asDate(aware.getValue());
		} else if (ref.equalsIgnoreCase(XmlSchemeDatatypes.XSD_DATETIME)) {
			return LiteralUtils.asDateTime(aware.getValue());
		} else if (ref.equalsIgnoreCase(XmlSchemeDatatypes.XSD_TIME)) {
			return LiteralUtils.asTime(aware.getValue());
		} else if (ref.equalsIgnoreCase(XmlSchemeDatatypes.XSD_BOOLEAN)) {
			return Boolean.parseBoolean(aware.getValue());
		}
		return aware.getValue();
	}

}
