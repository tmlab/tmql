/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.components.processor.runtime.module;

import java.util.Map;
import java.util.WeakHashMap;

import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TemplateManager {

	private Map<TopicMap, Map<String, Template>> templates;

	private TemplateManager() {
	}

	/**
	 * Store the given template definition as template for the given topic map
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param name
	 *            the template name
	 * @param definition
	 *            the template definition
	 */
	public void setTemplate(final TopicMap topicMap, final String name, final String definition) {
		if (templates == null) {
			templates = new WeakHashMap<TopicMap, Map<String,Template>>();
		}
		Map<String, Template> map = templates.get(topicMap);
		if (map == null) {
			map = HashUtil.getHashMap();
			templates.put(topicMap, map);
		}
		map.put(name, new Template(name, definition));
	}
	
	public void removeTemplate(final TopicMap topicMap, final String name){
		/*
		 * any templates stored for the topic map
		 */
		if (templates == null || !templates.containsKey(topicMap)) {
			return;
		}
		/*
		 * remove the template for the topic map
		 */
		Map<String, Template> map = templates.get(topicMap);
		map.remove(name);
		/*
		 * free memory
		 */
		if ( map.isEmpty()){
			templates.remove(topicMap);
		}		
		if ( templates.isEmpty()){
			templates = null;
		}
	}

	/**
	 * Returns the template for the given topic map by name
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param name
	 *            the name
	 * @return the template or <code>null</code> if the template is <code>null</code>
	 */
	public Template getTemplate(final TopicMap topicMap, final String name) {
		if (isKnownTemplate(topicMap, name)) {
			return templates.get(topicMap).get(name);
		}
		return null;
	}

	/**
	 * Checks if the template name is known by the manager
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param name
	 *            the name
	 * @return <code>true</code> if any template is known for the topic map by
	 *         this name, <code>false</code> otherwise
	 */
	public boolean isKnownTemplate(final TopicMap topicMap, final String name) {
		if (templates == null || !templates.containsKey(topicMap)) {
			return false;
		}
		return templates.get(topicMap).containsKey(name);
	}

	private static TemplateManager MANAGER = null;

	public static TemplateManager getTemplateManager() {
		if (MANAGER == null) {
			MANAGER = new TemplateManager();
		}
		return MANAGER;
	}

}
