/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis;

import java.util.Collection;
import java.util.LinkedList;

import org.tmapi.core.DatatypeAware;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.exception.NavigationException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisValue;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the value axis.
 * <p>
 * If the value is a name or occurrence item, this step schedules the item for atomification, i.e. marks the item to be
 * converted to the atomic value (integer, string, etc.) within the item. The item is eventually converted into an atom
 * according to the atomification rules. The optional item has no relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ValueAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public ValueAxis() {
		super(AxisValue.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {

		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Object> set = new LinkedList<Object>();
		/*
		 * check if construct is an occurrence or a variant
		 */
		if (source instanceof DatatypeAware) {
			DatatypeAware aware = (DatatypeAware) source;
			try {
				set.add(aware.getValue());
			} catch (Exception e) {
				throw new NavigationException(e);
			}
			return set;
		}
		/*
		 * check if construct is a name
		 */
		else if (source instanceof Name) {
			Name name = (Name) source;
			set.add(name.getValue());
			return set;
		}
		/*
		 * check if construct is a locator
		 */
		else if (source instanceof Locator) {
			set.add(((Locator) source).getReference());
			return set;
		}
		throw new InvalidValueException();
	}

}
