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

import org.tmapi.core.Construct;
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

import de.topicmapslab.tmql4j.draft2011.path.components.navigation.BaseNavigationAxisImpl;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.exception.NavigationException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisDatatype;

/**
 * Class definition representing the datatype axis.
 * <p>
 * If the value is a variant or occurrence item, in forward direction this step
 * schedules the datatype of the item. The optional item has no relevance.
 * </p>
 * <p>
 * If the value is a string or locator, in backward direction this step fetch
 * all variants and occurrences using this datatype. The optional type
 * represents the type of the occurrence. If the type is set, variants will be
 * ignored automatically.
 * </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DatatypeNavigationAxis extends BaseNavigationAxisImpl {

	/**
	 * base constructor to create an new instance
	 */
	public DatatypeNavigationAxis() {
		super(AxisDatatype.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends Construct> getBackwardNavigationResultClass(Object construct) throws NavigationException {
		return DatatypeAware.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getForwardNavigationResultClass(Object construct) throws NavigationException {
		return Locator.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateBackward(Object construct, Object optional) throws NavigationException {
		Locator loc;
		/*
		 * value is a string -> convert to locator
		 */
		if (construct instanceof String) {
			try {
				loc = getTopicMap().createLocator((String) construct);
			} catch (MalformedIRIException e) {
				throw new InvalidValueException("Given value is not a valid IRI", e);
			}
		}
		/*
		 * value is a locator
		 */
		else if (construct instanceof Locator) {
			loc = (Locator) construct;
		}
		/*
		 * value is anything else
		 */
		else {
			throw new InvalidValueException();
		}
		Collection<DatatypeAware> set = tryMajortomIndex(loc);
		/*
		 * is MaJorToM engine
		 */
		if (set == null) {
			set = new ArrayList<DatatypeAware>();
			/*
			 * get all topics
			 */
			for (Topic t : getTopicMap().getTopics()) {
				/*
				 * get occurrences
				 */
				for (Occurrence o : t.getOccurrences()) {
					/*
					 * check datatype and type if optional argument is given
					 */
					if (loc.equals(o.getDatatype()) && (!(optional instanceof Topic) || o.getType().equals(optional))) {
						set.add(o);
					}
				}
				/*
				 * get all names only if type is not provided
				 */
				if (optional == null) {
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
		else if (optional != null && optional instanceof Topic) {
			Topic type = (Topic) optional;
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
	 * @param datatype
	 *            the datatype
	 * @return the result
	 */
	@SuppressWarnings("unchecked")
	private Collection<DatatypeAware> tryMajortomIndex(Locator datatype) {
		try {
			/*
			 * get index class
			 */
			Class<? extends Index> clazz = (Class<? extends Index>) Class.forName("de.topicmapslab.majortom.model.index.ILiteralIndex");
			/*
			 * get index instance
			 */
			Index index = getTopicMap().getIndex(clazz);
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

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigateForward(Object construct, Object optional) throws NavigationException {
		/*
		 * create new instance of tuple-sequence
		 */
		Collection<Locator> set = new ArrayList<Locator>();
		/*
		 * check if construct is an occurrence or a variant
		 */
		if (construct instanceof DatatypeAware) {
			DatatypeAware aware = (DatatypeAware) construct;
			try {
				set.add(aware.getDatatype());
			} catch (Exception e) {
				throw new NavigationException(e);
			}
			return set;
		}
		throw new InvalidValueException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsBackwardNavigation(Object construct, Construct optional) throws NavigationException {
		if (construct instanceof Object && optional instanceof TopicMap) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supportsForwardNavigation(Object construct, Object optional) throws NavigationException {
		if (construct instanceof Name || construct instanceof DatatypeAware) {
			return true;
		}
		return false;
	}

}
