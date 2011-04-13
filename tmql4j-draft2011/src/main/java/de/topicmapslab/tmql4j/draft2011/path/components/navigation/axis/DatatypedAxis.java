/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.tmapi.core.DatatypeAware;
import org.tmapi.core.Locator;
import org.tmapi.core.MalformedIRIException;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Typed;
import org.tmapi.core.Variant;
import org.tmapi.index.Index;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisDatatyped;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the datatyped axis.
 * 
 * <p>
 * If the value is a string or locator, this step fetch all variants and occurrences using this datatype. The optional
 * type represents the type of the occurrence. If the type is set, variants will be ignored automatically.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DatatypedAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public DatatypedAxis() {
		super(AxisDatatyped.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		Locator loc;
		TopicMap topicMap = context.getQuery().getTopicMap();
		/*
		 * value is a string -> convert to locator
		 */
		if (source instanceof String) {
			try {
				loc = topicMap.createLocator((String) source);
			} catch (MalformedIRIException e) {
				throw new InvalidValueException("Given value is not a valid IRI", e);
			}
		}
		/*
		 * value is a locator
		 */
		else if (source instanceof Locator) {
			loc = (Locator) source;
		}
		/*
		 * value is anything else
		 */
		else {
			throw new InvalidValueException();
		}
		Collection<DatatypeAware> set = tryMajortomIndex(topicMap, loc);
		/*
		 * is MaJorToM engine
		 */
		if (set == null) {
			set = new ArrayList<DatatypeAware>();
			/*
			 * get all topics
			 */
			for (Topic t : topicMap.getTopics()) {
				/*
				 * get occurrences
				 */
				for (Occurrence o : t.getOccurrences()) {
					/*
					 * check datatype and type if optional argument is given
					 */
					if (loc.equals(o.getDatatype()) && (type == null || o.getType().equals(type))) {
						set.add(o);
					}
				}
				/*
				 * get all names only if type is not provided
				 */
				if (type == null) {
					for (Name n : t.getNames()) {
						/*
						 * get all variants
						 */
						for (Variant v : n.getVariants()) {
							/*
							 * check datatype
							 */
							if (loc.equals(v.getDatatype())) {
								set.add(v);
							}
						}
					}
				}
			}
			return set;
		}
		/*
		 * filter type
		 */
		else if (type != null) {
			Collection<DatatypeAware> set_ = new ArrayList<DatatypeAware>();
			for (DatatypeAware d : set) {
				if (d instanceof Typed && ((Typed) d).getType().equals(type)) {
					set_.add(d);
				}
			}
			return set_;
		}
		return set;
	}

	/**
	 * Internal method try to access special ILiteralIndex of majortom
	 * 
	 * @param tm
	 *            the topic map
	 * @param datatype
	 *            the datatype
	 * @return the result
	 */
	@SuppressWarnings("unchecked")
	private Collection<DatatypeAware> tryMajortomIndex(TopicMap tm, Locator datatype) {
		try {
			/*
			 * get index class
			 */
			Class<? extends Index> clazz = (Class<? extends Index>) Class.forName("de.topicmapslab.majortom.model.index.ILiteralIndex");
			/*
			 * get index instance
			 */
			Index index = tm.getIndex(clazz);
			/*
			 * get method
			 */
			Method methodGetDatatypeAwares = index.getClass().getMethod("getDatatypeAwares", Locator.class);
			/*
			 * call method
			 */
			Object result = methodGetDatatypeAwares.invoke(index, datatype);
			return (Collection<DatatypeAware>) result;
		} catch (Exception e) {
			// engine is not MaJorToM -> IGNORE
			return null;
		}
	}
}
