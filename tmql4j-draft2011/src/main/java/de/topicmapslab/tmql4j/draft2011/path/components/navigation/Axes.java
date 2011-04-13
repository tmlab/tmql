/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.navigation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.ByItemIdentifierAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.ByRegularExpressionAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.BySubjectIdentifierAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.BySubjectLocatorAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.ByValueAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.DatatypeAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.DatatypedAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.IdAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.InstancesAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.ItemIdentifiersAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.NamesAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.OccurrencesAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.ParentAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.PlayedAssociationsAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.PlayedRolesAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.PlayersNavigationAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.ReifiedAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.ReifierAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.RoleTypesAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.RolesAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.ScopeAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.ScopedAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.SubjectIdentifiersAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.SubjectLocatorsAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.SubtypesAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.SupertypesAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.TraverseAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.TypedAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.TypesAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.ValueAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.VariantsAxis;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2011.path.exception.NavigationException;
import de.topicmapslab.tmql4j.draft2011.path.exception.UnsupportedNavigationTypeException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisByItemIdentifier;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisByRegularExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisBySubjectIdentifier;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisBySubjectLocator;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisByValue;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisDatatype;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisDatatyped;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisId;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisInstances;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisItemIdentifiers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisNames;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisOccurrences;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisParent;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisPlayedAssociations;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisPlayedRoles;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisPlayers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisReified;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisReifier;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisRoleTypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisRoles;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisScope;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisScoped;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSubjectIdentifiers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSubjectLocators;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSubtypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSupertypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTraverse;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTyped;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisValue;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisVariants;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * Base implementation of the {@link INavigationHandler} to manage the axis using TMAPI functions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class Axes {

	/**
	 * singleton instance
	 */
	private static Axes instance = null;

	/**
	 * the internal map containing axis-classes of each axis type
	 */
	private final Map<Class<? extends IToken>, Class<? extends IAxis>> axis;

	/**
	 * invisible private constructor of singleton implementation.
	 */
	private Axes() {
		axis = new HashMap<Class<? extends IToken>, Class<? extends IAxis>>();
		axis.put(AxisByItemIdentifier.class, ByItemIdentifierAxis.class);
		axis.put(AxisBySubjectIdentifier.class, BySubjectIdentifierAxis.class);
		axis.put(AxisBySubjectLocator.class, BySubjectLocatorAxis.class);
		axis.put(AxisByValue.class, ByValueAxis.class);
		axis.put(AxisByRegularExpression.class, ByRegularExpressionAxis.class);
		axis.put(AxisDatatype.class, DatatypeAxis.class);
		axis.put(AxisDatatyped.class, DatatypedAxis.class);
		axis.put(AxisId.class, IdAxis.class);
		axis.put(AxisInstances.class, InstancesAxis.class);
		axis.put(AxisItemIdentifiers.class, ItemIdentifiersAxis.class);
		axis.put(AxisNames.class, NamesAxis.class);
		axis.put(AxisOccurrences.class, OccurrencesAxis.class);
		axis.put(AxisPlayedAssociations.class, PlayedAssociationsAxis.class);
		axis.put(AxisPlayedRoles.class, PlayedRolesAxis.class);
		axis.put(AxisParent.class, ParentAxis.class);
		axis.put(AxisPlayers.class, PlayersNavigationAxis.class);
		axis.put(AxisReified.class, ReifiedAxis.class);
		axis.put(AxisReifier.class, ReifierAxis.class);
		axis.put(AxisRoles.class, RolesAxis.class);
		axis.put(AxisRoleTypes.class, RoleTypesAxis.class);
		axis.put(AxisScope.class, ScopeAxis.class);
		axis.put(AxisScoped.class, ScopedAxis.class);
		axis.put(AxisSubjectIdentifiers.class, SubjectIdentifiersAxis.class);
		axis.put(AxisSubjectLocators.class, SubjectLocatorsAxis.class);
		axis.put(AxisSubtypes.class, SubtypesAxis.class);
		axis.put(AxisSupertypes.class, SupertypesAxis.class);
		axis.put(AxisTraverse.class, TraverseAxis.class);
		axis.put(AxisTyped.class, TypedAxis.class);
		axis.put(AxisTypes.class, TypesAxis.class);
		axis.put(AxisValue.class, ValueAxis.class);
		axis.put(AxisVariants.class, VariantsAxis.class);
	}

	/**
	 * Returns the navigation axis for the given token
	 * 
	 * @param token
	 *            the token class
	 * @return the axis
	 * @throws UnsupportedNavigationTypeException
	 */
	public IAxis lookup(Class<? extends IToken> token) throws UnsupportedNavigationTypeException {
		if (axis.containsKey(token)) {
			Class<? extends IAxis> clazz = axis.get(token);
			try {
				Constructor<? extends IAxis> constructor = clazz.getConstructor();
				IAxis axis = constructor.newInstance();
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
	 * Static method, which provides access to singleton instance of navigation handler.
	 * 
	 * @return the singleton instance
	 * @throws NavigationException
	 */
	public static final Axes buildHandler() throws NavigationException {
		if (instance == null) {
			instance = new Axes();
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
	public final void registryAxis(Class<? extends IToken> token, Class<? extends IAxis> clazz) {
		axis.put(token, clazz);
	}
}
