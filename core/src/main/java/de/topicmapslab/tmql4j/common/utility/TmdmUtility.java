/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.utility;

import org.tmapi.core.Association;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

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
	 */
	public static final void ako(TopicMap map, Topic supertype, Topic subtype) {
		Topic assoType = map
				.getTopicBySubjectIdentifier(map
						.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION));
		if (assoType == null) {
			assoType = map
					.createTopicBySubjectIdentifier(map
							.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION));
		}
		Topic subRole = map.getTopicBySubjectIdentifier(map
				.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE));
		if (subRole == null) {
			subRole = map
					.createTopicBySubjectIdentifier(map
							.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE));
		}
		Topic superRole = map.getTopicBySubjectIdentifier(map
				.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE));
		if (superRole == null) {
			superRole = map
					.createTopicBySubjectIdentifier(map
							.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE));
		}
		Association a = map.createAssociation(assoType, new Topic[0]);
		a.createRole(superRole, supertype);
		a.createRole(subRole, subtype);
	}

}
