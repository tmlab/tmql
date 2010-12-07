/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.extension;

import java.util.Set;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.extension.IExtensionPoint;
import de.topicmapslab.tmql4j.extension.ILanguageExtension;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class MajortomExtension implements IExtensionPoint {

	private static final String MAJORTOM_EXTENSION = "majortom-extension";

	private Set<ILanguageExtension> extensions;

	/**
	 * {@inheritDoc}
	 */
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException {
		extensions = HashUtil.getHashSet();

		MajortomFunctions majortomFunctions = new MajortomFunctions();
		majortomFunctions.registerExtension(runtime);
		extensions.add(majortomFunctions);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getExtensionPointId() {
		return MAJORTOM_EXTENSION;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<ILanguageExtension> getLanguageExtensions() {
		return extensions;
	}

}
