/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.navigation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.tmapi.core.Construct;

import de.topicmapslab.tmql4j.path.components.navigation.axis.AtomifyNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.CharacteristicsNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.IdNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.IndicatorsNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.InstancesNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.ItemNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.LocatorsNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.PlayersNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.ReifierNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.RolesNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.ScopeNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.SubtypesNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.SupertypesNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.TraverseNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.TypedNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.axis.TypesNavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.model.INavigationAxis;
import de.topicmapslab.tmql4j.path.components.navigation.model.INavigationHandler;
import de.topicmapslab.tmql4j.path.exception.NavigationException;
import de.topicmapslab.tmql4j.path.exception.UnsupportedNavigationTypeException;

/**
 * Base implementation of the {@link INavigationHandler} to manage the axis
 * using TMAPI functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NavigationHandler implements INavigationHandler {

	/**
	 * singleton instance
	 */
	private static NavigationHandler instance = null;

	/**
	 * the internal map containing axis-classes of each axis type
	 */
	private final Map<NavigationAxis, Class<? extends INavigationAxis>> axis;

	/**
	 * invisible private constructor of singleton implementation.
	 */
	private NavigationHandler() {
		axis = new HashMap<NavigationAxis, Class<? extends INavigationAxis>>();
		axis.put(NavigationAxis.types, TypesNavigationAxis.class);
		axis.put(NavigationAxis.instances, InstancesNavigationAxis.class);
		axis.put(NavigationAxis.supertypes, SupertypesNavigationAxis.class);
		axis.put(NavigationAxis.subtypes, SubtypesNavigationAxis.class);
		axis.put(NavigationAxis.players, PlayersNavigationAxis.class);
		axis.put(NavigationAxis.roles, RolesNavigationAxis.class);
		axis.put(NavigationAxis.traverse, TraverseNavigationAxis.class);
		axis.put(NavigationAxis.characteristics,
				CharacteristicsNavigationAxis.class);
		axis.put(NavigationAxis.scope, ScopeNavigationAxis.class);
		axis.put(NavigationAxis.locators, LocatorsNavigationAxis.class);
		axis.put(NavigationAxis.indicators, IndicatorsNavigationAxis.class);
		axis.put(NavigationAxis.item, ItemNavigationAxis.class);
		axis.put(NavigationAxis.reifier, ReifierNavigationAxis.class);
		axis.put(NavigationAxis.atomify, AtomifyNavigationAxis.class);
		axis.put(NavigationAxis.typed, TypedNavigationAxis.class);
		axis.put(NavigationAxis.id, IdNavigationAxis.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<INavigationAxis> getSupportedNavigationAxis(
			Construct construct) throws NavigationException {
		Set<INavigationAxis> set = new HashSet<INavigationAxis>();
		for (Entry<NavigationAxis, Class<? extends INavigationAxis>> entry : axis
				.entrySet()) {
			try {
				INavigationAxis axis = lookup(entry.getKey());
				if (axis.supportsBackwardNavigation(construct)
						|| axis.supportsForwardNavigation(construct)) {
					set.add(axis);
				}
			} catch (UnsupportedNavigationTypeException e) {
				throw new NavigationException("Navigation error for axis "
						+ entry.getKey().toString(), e);
			}
		}
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<INavigationAxis> getSupportedNavigationAxis(
			Construct construct, final Construct optional)
			throws NavigationException {
		Set<INavigationAxis> set = new HashSet<INavigationAxis>();
		for (Entry<NavigationAxis, Class<? extends INavigationAxis>> entry : axis
				.entrySet()) {
			try {
				INavigationAxis axis = lookup(entry.getKey());
				if (axis.supportsBackwardNavigation(construct)
						|| axis.supportsForwardNavigation(construct)) {
					set.add(axis);
				}
			} catch (UnsupportedNavigationTypeException e) {
				throw new NavigationException("Navigation error for axis "
						+ entry.getKey().toString(), e);
			}
		}
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	public INavigationAxis lookup(NavigationAxis type)
			throws UnsupportedNavigationTypeException {
		if (axis.containsKey(type)) {
			Class<? extends INavigationAxis> clazz = axis.get(type);
			try {
				Constructor<? extends INavigationAxis> constructor = clazz
						.getConstructor();
				INavigationAxis axis = constructor.newInstance();
				return axis;
			} catch (NoSuchMethodError e) {
				throw new UnsupportedNavigationTypeException(
						"Initialization error of " + type.name(), e);
			} catch (IllegalArgumentException e) {
				throw new UnsupportedNavigationTypeException(
						"Initialization error of " + type.name(), e);
			} catch (InstantiationException e) {
				throw new UnsupportedNavigationTypeException(
						"Initialization error of " + type.name(), e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedNavigationTypeException(
						"Initialization error of " + type.name(), e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedNavigationTypeException(
						"Initialization error of " + type.name(), e);
			} catch (SecurityException e) {
				throw new UnsupportedNavigationTypeException(
						"Initialization error of " + type.name(), e);
			} catch (NoSuchMethodException e) {
				throw new UnsupportedNavigationTypeException(
						"Initialization error of " + type.name(), e);
			}
		}
		throw new UnsupportedNavigationTypeException("Unknown type "
				+ type.name());
	}

	/**
	 * Static method, which provides access to singleton instance of navigation
	 * handler.
	 * 
	 * @return the singleton instance
	 * @throws NavigationException
	 */
	public static final NavigationHandler buildHandler()
			throws NavigationException {
		if (instance == null) {
			instance = new NavigationHandler();
		}

		return instance;
	}
}
