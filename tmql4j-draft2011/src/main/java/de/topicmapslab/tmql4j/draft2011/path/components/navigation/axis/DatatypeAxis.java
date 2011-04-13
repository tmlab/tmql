/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis;

import java.util.ArrayList;
import java.util.Collection;

import org.tmapi.core.DatatypeAware;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.exception.NavigationException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisDatatype;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the datatype axis.
 * <p>
 * If the value is a variant or occurrence item, this step schedules the datatype of the item. The optional item has no
 * relevance.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DatatypeAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public DatatypeAxis() {
		super(AxisDatatype.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Locator> set = new ArrayList<Locator>();
		/*
		 * check if construct is an occurrence or a variant
		 */
		if (source instanceof DatatypeAware) {
			DatatypeAware aware = (DatatypeAware) source;
			try {
				set.add(aware.getDatatype());
			} catch (Exception e) {
				throw new NavigationException(e);
			}
			return set;
		}
		throw new InvalidValueException();
	}

}
