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
package de.topicmapslab.tmql4j.extensions.core;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.extensions.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.extensions.model.IExtensionPoint;
import de.topicmapslab.tmql4j.extensions.model.ILanguageExtension;
import de.topicmapslab.tmql4j.osgi.TMQLActivator;
import de.topicmapslab.tmql4j.parser.model.IExpression;

/**
 * Extension adapter importing the register extensions using Java service
 * providers.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ExtensionPointAdapter {

	/**
	 * internal map of all registered extensions
	 */
	private final Map<String, IExtensionPoint> extensionPoints = HashUtil
			.getHashMap();
	/**
	 * internal map of all registered language extensions
	 */
	private final Map<Class<? extends IExpression>, Set<ILanguageExtension>> languageExtensions = HashUtil
			.getHashMap();

	/**
	 * the Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

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
	public void loadExtensionPoints(ITMQLRuntime runtime)
			throws TMQLExtensionRegistryException {
		
		Iterable<IExtensionPoint> pointList = null;
		
		// try to load the extension points via OSGi
		try {
			// check if we are in an OSGi environment if not an exception is thrown
			if (TMQLActivator.getDefault()!=null)
				pointList = TMQLActivator.getDefault().getExtensionPoints();
		} catch (Exception e) {
			// we do nothing, cause we are not in an OSGi environment
			logger.warn("No Osgi Bundle founde", e);
		}
		
		
		// if no OSGi list found use the loader
		if (pointList==null) {
			ServiceLoader<IExtensionPoint> loader = ServiceLoader.load(IExtensionPoint.class);
			loader.reload();
			pointList = loader;
		}
		
		for (IExtensionPoint extensionPoint : pointList) {
			if (extensionPoints.containsKey(extensionPoint
					.getExtensionPointId())) {
				throw new TMQLExtensionRegistryException(
						"Duplicate extension point id '"
								+ extensionPoint.getClass().getName()
								+ " and "
								+ extensionPoints.get(extensionPoint
										.getExtensionPointId()));
			}
			extensionPoint.registerExtension(runtime);
			if (runtime.isVerbose()) {
				logger.info("Initializing extension point with id - "
						+ extensionPoint.getExtensionPointId());
			}
			extensionPoints.put(extensionPoint.getExtensionPointId(),
					extensionPoint);
			if (extensionPoint instanceof ILanguageExtension) {
				Set<ILanguageExtension> set = languageExtensions
						.get(((ILanguageExtension) extensionPoint)
								.getLanguageExtensionEntry()
								.getExpressionType());
				if (set == null) {
					set = HashUtil.getHashSet();
				}
				set.add((ILanguageExtension) extensionPoint);
				languageExtensions.put(((ILanguageExtension) extensionPoint)
						.getLanguageExtensionEntry().getExpressionType(), set);
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
	public Set<ILanguageExtension> getLanguageExtensions(
			Class<? extends IExpression> expressionType) {
		return languageExtensions.get(expressionType);
	}

}
