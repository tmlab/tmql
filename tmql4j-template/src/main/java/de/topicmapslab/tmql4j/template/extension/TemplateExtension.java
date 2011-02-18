/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.extension;

import java.util.Set;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.extension.IExtensionPoint;
import de.topicmapslab.tmql4j.extension.ILanguageExtension;
import de.topicmapslab.tmql4j.template.grammar.pragma.TemplatePragma;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TemplateExtension implements IExtensionPoint {

	public static final String EXTENSION_ID = "tmql4j-template";
	private Set<ILanguageExtension> extensions;

	/**
	 * {@inheritDoc}
	 */
	public String getExtensionPointId() {
		return EXTENSION_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException {
		runtime.getLanguageContext().getPragmaRegistry().register(new TemplatePragma());

		extensions = HashUtil.getHashSet();

		TemplateQueryExpression templateQueryExpression = new TemplateQueryExpression();
		templateQueryExpression.registerExtension(runtime);
		extensions.add(templateQueryExpression);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<ILanguageExtension> getLanguageExtensions() {
		return extensions;
	}

}
