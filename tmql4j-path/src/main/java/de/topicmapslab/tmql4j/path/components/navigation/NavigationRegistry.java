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

import de.topicmapslab.tmql4j.grammar.lexical.IToken;
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
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisAtomify;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisCharacteristics;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisId;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisIndicators;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisInstances;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisItem;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisLocators;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisPlayers;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisReifier;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisRoles;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisScope;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisSubtypes;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisSupertypes;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisTraverse;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisTyped;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisTypes;

/**
 * Base implementation of the {@link INavigationHandler} to manage the axis
 * using TMAPI functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class NavigationRegistry implements INavigationHandler {

	/**
	 * singleton instance
	 */
	private static NavigationRegistry instance = null;

	/**
	 * the internal map containing axis-classes of each axis type
	 */
	private final Map<Class<? extends IToken>, Class<? extends INavigationAxis>> axis;

	/**
	 * invisible private constructor of singleton implementation.
	 */
	private NavigationRegistry() {
		axis = new HashMap<Class<? extends IToken>, Class<? extends INavigationAxis>>();
		axis.put(AxisTypes.class, TypesNavigationAxis.class);
		axis.put(AxisInstances.class, InstancesNavigationAxis.class);
		axis.put(AxisSupertypes.class, SupertypesNavigationAxis.class);
		axis.put(AxisSubtypes.class, SubtypesNavigationAxis.class);
		axis.put(AxisPlayers.class, PlayersNavigationAxis.class);
		axis.put(AxisRoles.class, RolesNavigationAxis.class);
		axis.put(AxisTraverse.class, TraverseNavigationAxis.class);
		axis.put(AxisCharacteristics.class, CharacteristicsNavigationAxis.class);
		axis.put(AxisScope.class, ScopeNavigationAxis.class);
		axis.put(AxisLocators.class, LocatorsNavigationAxis.class);
		axis.put(AxisIndicators.class, IndicatorsNavigationAxis.class);
		axis.put(AxisItem.class, ItemNavigationAxis.class);
		axis.put(AxisReifier.class, ReifierNavigationAxis.class);
		axis.put(AxisAtomify.class, AtomifyNavigationAxis.class);
		axis.put(AxisTyped.class, TypedNavigationAxis.class);
		axis.put(AxisId.class, IdNavigationAxis.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<INavigationAxis> getSupportedNavigationAxis(Construct construct) throws NavigationException {
		Set<INavigationAxis> set = new HashSet<INavigationAxis>();
		for (Entry<Class<? extends IToken>, Class<? extends INavigationAxis>> entry : axis.entrySet()) {
			try {
				INavigationAxis axis = lookup(entry.getKey());
				if (axis.supportsBackwardNavigation(construct) || axis.supportsForwardNavigation(construct)) {
					set.add(axis);
				}
			} catch (UnsupportedNavigationTypeException e) {
				throw new NavigationException("Navigation error for axis " + entry.getKey().toString(), e);
			}
		}
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<INavigationAxis> getSupportedNavigationAxis(Construct construct, final Construct optional) throws NavigationException {
		Set<INavigationAxis> set = new HashSet<INavigationAxis>();
		for (Entry<Class<? extends IToken>, Class<? extends INavigationAxis>> entry : axis.entrySet()) {
			try {
				INavigationAxis axis = lookup(entry.getKey());
				if (axis.supportsBackwardNavigation(construct) || axis.supportsForwardNavigation(construct)) {
					set.add(axis);
				}
			} catch (UnsupportedNavigationTypeException e) {
				throw new NavigationException("Navigation error for axis " + entry.getKey().toString(), e);
			}
		}
		return set;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public INavigationAxis lookup(IToken token) throws UnsupportedNavigationTypeException {
		return lookup(token.getClass());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public INavigationAxis lookup(Class<? extends IToken> token) throws UnsupportedNavigationTypeException {
		if (axis.containsKey(token)) {
			Class<? extends INavigationAxis> clazz = axis.get(token);
			try {
				Constructor<? extends INavigationAxis> constructor = clazz.getConstructor();
				INavigationAxis axis = constructor.newInstance();
				return axis;
			} catch (NoSuchMethodError e) {
				throw new UnsupportedNavigationTypeException("Initialization error of " + token.getName(), e);
			} catch (IllegalArgumentException e) {
				throw new UnsupportedNavigationTypeException("Initialization error of " + token.getName(), e);
			} catch (InstantiationException e) {
				throw new UnsupportedNavigationTypeException("Initialization error of " + token.getName(), e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedNavigationTypeException("Initialization error of " + token.getName(), e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedNavigationTypeException("Initialization error of " + token.getName(), e);
			} catch (SecurityException e) {
				throw new UnsupportedNavigationTypeException("Initialization error of " + token.getName(), e);
			} catch (NoSuchMethodException e) {
				throw new UnsupportedNavigationTypeException("Initialization error of " + token.getName(), e);
			}
		}
		throw new UnsupportedNavigationTypeException("Unknown type " + token.getName());
	}

	/**
	 * Static method, which provides access to singleton instance of navigation
	 * handler.
	 * 
	 * @return the singleton instance
	 * @throws NavigationException
	 */
	public static final NavigationRegistry buildHandler() throws NavigationException {
		if (instance == null) {
			instance = new NavigationRegistry();
		}

		return instance;
	}

	/**
	 * Register a new navigation axis
	 * 
	 * @param token
	 *            the token
	 * @param clazz
	 *            the class of navigation axis
	 */
	public final void registryAxis(Class<? extends IToken> token, Class<? extends INavigationAxis> clazz) {
		axis.put(token, clazz);
	}
}
