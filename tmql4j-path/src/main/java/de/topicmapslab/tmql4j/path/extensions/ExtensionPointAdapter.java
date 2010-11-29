/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.extensions;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.util.HashUtil;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.extensions.IExtensionPoint;
import de.topicmapslab.tmql4j.extensions.IExtensionPointAdapter;
import de.topicmapslab.tmql4j.extensions.ILanguageExtension;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.osgi.TMQLActivator;

/**
 * Extension adapter importing the register extensions using Java service
 * providers.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExtensionPointAdapter implements IExtensionPointAdapter {

	/**
	 * internal map of all registered extensions
	 */
	private final Map<String, IExtensionPoint> extensionPoints = HashUtil.getHashMap();

	/**
	 * internal set of all ignored extensions
	 */
	private final Set<String> disabledExtensionPoints = HashUtil.getHashSet();
	/**
	 * internal map of all registered language extensions
	 */
	private final Map<Class<? extends IExpression>, Set<ILanguageExtension>> languageExtensions = HashUtil.getHashMap();

	/**
	 * a map containing all language extensions of a specific extension point
	 */
	private final Map<IExtensionPoint, Set<ILanguageExtension>> registeredLanguageExtenstions = HashUtil.getHashMap();

	/**
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

	private ITMQLRuntime runtime;

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the runtime calling the construct
	 */
	public ExtensionPointAdapter(ITMQLRuntime runtime) {
		this.runtime = runtime;
	}

	/**
	 * Loading all included extensions contained in the classpath. The method
	 * uses a service provider of the interface {@link IExtensionPoint} to get
	 * all implementing extensions.
	 * 
	 * @param runtime
	 *            the calling runtime
	 * @throws TMQLExtensionRegistryException
	 *             thrown if a duplicated id will be found
	 */
	public void loadExtensionPoints() throws TMQLExtensionRegistryException {

		Iterable<IExtensionPoint> pointList = null;

		// try to load the extension points via OSGi
		try {
			// check if we are in an OSGi environment if not an exception is
			// thrown
			if (TMQLActivator.getDefault() != null)
				pointList = TMQLActivator.getDefault().getExtensionPoints();
		} catch (Throwable e) {
			// we do nothing, cause we are not in an OSGi environment
			logger.warn("No Osgi Bundle founde", e);
		}

		// if no OSGi list found use the loader
		if (pointList == null) {
			ServiceLoader<IExtensionPoint> loader = ServiceLoader.load(IExtensionPoint.class, getClass().getClassLoader());
			getClass().getResourceAsStream("/META-INF/services/" + IExtensionPoint.class.getName());
			loader.reload();
			pointList = loader;
		}

		for (IExtensionPoint extensionPoint : pointList) {
			if (extensionPoints.containsKey(extensionPoint.getExtensionPointId())) {
				throw new TMQLExtensionRegistryException("Duplicate extension point id '" + extensionPoint.getClass().getName() + " and " + extensionPoints.get(extensionPoint.getExtensionPointId()));
			}
			extensionPoint.registerExtension(runtime);
			logger.info("Initializing extension point with id - " + extensionPoint.getExtensionPointId());
			extensionPoints.put(extensionPoint.getExtensionPointId(), extensionPoint);
			if (extensionPoint instanceof ILanguageExtension) {
				Set<ILanguageExtension> set = languageExtensions.get(((ILanguageExtension) extensionPoint).getLanguageExtensionEntry().getExpressionType());
				if (set == null) {
					set = HashUtil.getHashSet();
				}
				set.add((ILanguageExtension) extensionPoint);
				languageExtensions.put(((ILanguageExtension) extensionPoint).getLanguageExtensionEntry().getExpressionType(), set);
				registeredLanguageExtenstions.put(extensionPoint, set);
			}
		}
	}

	/**
	 * Getter of a set of language extension applicable for the given expression
	 * type.
	 * 
	 * @param expressionType
	 *            the expression type
	 * @return a set of the language extensions or <code>null</code>
	 */
	public Set<ILanguageExtension> getLanguageExtensions(Class<? extends IExpression> expressionType) {
		/*
		 * get language extensions
		 */
		Set<ILanguageExtension> set = languageExtensions.get(expressionType);
		/*
		 * check if set is null
		 */
		if (set != null) {
			/*
			 * iterate over disabled extension points
			 */
			for (String extensionPointId : disabledExtensionPoints) {
				/*
				 * extract language extensions of the diables extension point
				 */
				Set<ILanguageExtension> tmp = getLanguageExtensions(extensionPoints.get(extensionPointId));
				if (tmp != null) {
					/*
					 * remove from set
					 */
					set.removeAll(tmp);
				}
			}
		}
		return set;
	}

	/**
	 * Getter of a set of language extension registered by the given extension
	 * point
	 * 
	 * @param point
	 *            the language extension
	 * @return a set of the language extensions or <code>null</code>
	 */
	public Set<ILanguageExtension> getLanguageExtensions(IExtensionPoint point) {
		return registeredLanguageExtenstions.get(point);
	}

	/**
	 * Enable the extension point with the given id if it exists
	 * 
	 * @param extensionPointId
	 *            the id
	 */
	public final void enableExtensionPoint(final String extensionPointId) {
		if (extensionPoints.containsKey(extensionPointId)) {
			disabledExtensionPoints.remove(extensionPointId);
		}
	}

	/**
	 * Disable the extension point with the given id if it exists
	 * 
	 * @param extensionPointId
	 *            the id
	 */
	public final void disableExtensionPoint(final String extensionPointId) {
		if (extensionPoints.containsKey(extensionPointId)) {
			disabledExtensionPoints.add(extensionPointId);
		}
	}

}
