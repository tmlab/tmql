/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.select.extension;

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
public class Draft2007SelectStlye implements IExtensionPoint {

	/**
	 * constant extension point id
	 */
	private static final String DRAFT2007_SELECT_STYLE = "draft2007-select-style";
	private Set<ILanguageExtension> extensions;

	/**
	 * {@inheritDoc}
	 */
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException {
		extensions = HashUtil.getHashSet();

		Draft2007SelectStyleQueryExpression draft2007SelectStyleQueryExpression = new Draft2007SelectStyleQueryExpression();
		draft2007SelectStyleQueryExpression.registerExtension(runtime);
		extensions.add(draft2007SelectStyleQueryExpression);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getExtensionPointId() {
		return DRAFT2007_SELECT_STYLE;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<ILanguageExtension> getLanguageExtensions() {
		return extensions;
	}

}
