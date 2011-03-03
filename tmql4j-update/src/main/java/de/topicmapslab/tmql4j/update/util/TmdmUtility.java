/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.tmapi.core.Association;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.update.components.results.IUpdateAlias;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.TmdmSubjectIdentifier;

/**
 * Utility class containing useful functions in context of using the TMDM.
 * 
 * @author Sven Krosse
 * 
 */
public class TmdmUtility {

	/**
	 * Creates the supertype-subtype association.
	 * 
	 * @param supertype
	 *            the supertype
	 * @param subtype
	 *            the subtype
	 * @param constructIds
	 *            a map containing new constructs created or <code>null</code>
	 */
	public static final void addSupertype(TopicMap map, Topic supertype, Topic subtype, Map<String, Object> constructIds) {
		/*
		 * first try to access special method
		 */
		try {
			Method m = subtype.getClass().getMethod("addSupertype", Topic.class);
			m.invoke(subtype, supertype);
			return;
		} catch (Exception e) {
			// IGNORE
		}
		Collection<String> topicIds = HashUtil.getHashSet();
		Topic assoType = map.getTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION));
		if (assoType == null) {
			assoType = map.createTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION));
			if (topicIds != null) {
				topicIds.add(assoType.getId());
			}
		}
		Topic subRole = map.getTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE));
		if (subRole == null) {
			subRole = map.createTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE));
			if (topicIds != null) {
				topicIds.add(subRole.getId());
			}
		}
		Topic superRole = map.getTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE));
		if (superRole == null) {
			superRole = map.createTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE));
			if (topicIds != null) {
				topicIds.add(superRole.getId());
			}
		}
		Association a = map.createAssociation(assoType, new Topic[0]);
		Role r = a.createRole(superRole, supertype);
		Role r2 = a.createRole(subRole, subtype);
		
		/*
		 * store relations
		 */
		if ( constructIds != null ){
			constructIds.put(IUpdateAlias.ASSOCIATIONS, a.getId());
			if ( !topicIds.isEmpty()){
				constructIds.put(IUpdateAlias.TOPICS, topicIds);
			}
			Collection<String> roleIds = HashUtil.getHashSet();
			roleIds.add(r.getId());
			roleIds.add(r2.getId());
			constructIds.put(IUpdateAlias.ROLES, roleIds);
		}
	}
	
	/**
	 * Removes the supertype-subtype association.
	 * 
	 * @param supertype
	 *            the supertype
	 * @param subtype
	 *            the subtype
	 * @param constructIds
	 *            a map containing new constructs created or <code>null</code>
	 */
	public static final void removeSupertype(TopicMap map, Topic supertype, Topic subtype, Map<String, Object> constructIds) {
		/*
		 * first try to access special method
		 */
		try {
			Method m = subtype.getClass().getMethod("removeSupertype", Topic.class);
			m.invoke(subtype, supertype);
			return;
		} catch (Exception e) {
			// IGNORE
		}
		Topic assoType = map.getTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION));
		if (assoType == null) {
			return;
		}
		Topic subRole = map.getTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE));
		if (subRole == null) {
			return;
		}
		Topic superRole = map.getTopicBySubjectIdentifier(map.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE));
		if (superRole == null) {
			return;
		}
		/*
		 * remove association
		 */
		for ( Role r : subtype.getRolesPlayed(subRole, assoType)){
			for ( Role r2 : r.getParent().getRoles(superRole)){
				if ( supertype.equals(r2.getPlayer())){
					constructIds.put(IUpdateAlias.ASSOCIATIONS, r2.getParent().getId());
					Collection<String> roleIds = HashUtil.getHashSet();
					roleIds.add(r.getId());
					roleIds.add(r2.getId());
					constructIds.put(IUpdateAlias.ROLES, roleIds);
					r2.getParent().remove();
					return;
				}
			}
		}
	}

}
