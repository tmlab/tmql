/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.navigation;

import java.net.URI;

import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;

/**
 * This enumeration represents the navigation axis of the TMDM, which also use
 * within the TMQL ISO Standard.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public enum NavigationAxis {

	/**
	 * <b>navigation axis:</b> types<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve all types of the instance<br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve all instances of the given
	 * type
	 */
	types,
	typed,
	instances,

	/**
	 * <b>navigation axis:</b> super type<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve all supertypes of the given
	 * instance<br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve all subtypes of the given
	 * instance <br />
	 */
	supertypes,
	subtypes,

	/**
	 * <b>navigation axis:</b> player<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve all role players of the
	 * given association<br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve all association, where the
	 * given topic takes places as a role player <br />
	 */
	players,

	/**
	 * <b>navigation axis:</b> role<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve all role player types of
	 * the given association<br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve all associations, where
	 * the given topic takes places as a role player type <br />
	 */
	roles,

	/**
	 * <b>navigation axis:</b> traverse<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve all associations, where the
	 * given topic takes place as a role player <br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve all topics being a player
	 * and an instance of the additional given topic type <br />
	 */
	traverse,

	/**
	 * <b>navigation axis:</b> characteristic<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve all characteristics of the
	 * given topic, includes all {@link Occurrence} and {@link Name} <br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve all topics, which already
	 * have this characteristic <br />
	 */
	characteristics,

	/**
	 * <b>navigation axis:</b> scope<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve all scopes of the given
	 * characteristic ( {@link Occurrence} or {@link Name} )<br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve all characteristics of the
	 * given topic in the additional given scope <br />
	 */
	scope,

	/**
	 * <b>navigation axis:</b> locator<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve all {@link Locator} of the
	 * given topic<br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve all topics with the given
	 * {@link Locator} <br />
	 */
	locators,

	/**
	 * <b>navigation axis:</b> indicator<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve all subject identifier of
	 * the given topic as {@link URI}<br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve the topic with the given
	 * subject identifier as {@link URI} <br />
	 */
	indicators,

	/**
	 * <b>navigation axis:</b> item<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve one subject identifier of
	 * the given topic as {@link String}<br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve the topic with the given
	 * subject identifier as {@link String} <br />
	 */
	item,
	
	/**
	 * <b>navigation axis:</b> id<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve the id of the construct as {@link String}<br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve the construct with the given
	 * id <br />
	 */
	id,

	/**
	 * <b>navigation axis:</b> reifier<br />
	 * <b>forward direction (&gt;&gt;):</b> retrieve the reify topic of the
	 * given reification topic <br />
	 * <b>backward direction (&lt;&lt;):</b> retrieve all reifications of the
	 * given topic <br />
	 */
	reifier,

	/**
	 * <b>navigation axis:</b> atomify<br />
	 * <b>forward direction (&gt;&gt;):</b> transform all characteristics the
	 * given topic to simple the simple datatype<br />
	 * <b>backward direction (&lt;&lt;):</b> transform the given value to a real
	 * topic map element <br />
	 */
	atomify;

}
