/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.utility;

import java.util.Set;

import de.topicmapslab.tmql4j.common.utility.HashUtil;

/**
 * Utility class for TMDM default subject-identifier.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TMDMIdentifier {

	/**
	 * hidden constructor
	 */
	private TMDMIdentifier() {

	}

	/**
	 * TMDM default name type -
	 * http://psi.topicmaps.org/iso13250/model/topic-name
	 */
	public static final String DEFAULT_NAME_TYPE = "http://psi.topicmaps.org/iso13250/model/topic-name";
	/**
	 * TMDM type-instance-association type -
	 * http://psi.topicmaps.org/iso13250/model/type-instance
	 */
	public static final String INSTANCE_OF_TYPE = "http://psi.topicmaps.org/iso13250/model/type-instance";
	/**
	 * TMDM type-role type - http://psi.topicmaps.org/iso13250/model/type
	 */
	public static final String TYPE_ROLE = "http://psi.topicmaps.org/iso13250/model/type";
	/**
	 * TMDM instance-role type -
	 * http://psi.topicmaps.org/iso13250/model/instance
	 */
	public static final String INSTANCE_ROLE = "http://psi.topicmaps.org/iso13250/model/instance";
	/**
	 * TMDM supertype-subtype-association type -
	 * http://psi.topicmaps.org/iso13250/model/supertype-subtype
	 */
	public static final String KIND_OF_TYPE = "http://psi.topicmaps.org/iso13250/model/supertype-subtype";
	/**
	 * TMDM supertype-role type
	 * -http://psi.topicmaps.org/iso13250/model/supertype
	 */
	public static final String SUPERTYPE_ROLE = "http://psi.topicmaps.org/iso13250/model/supertype";
	/**
	 * TMDM subtype-role type - http://psi.topicmaps.org/iso13250/model/subtype
	 */
	public static final String SUBTYPE_ROLE = "http://psi.topicmaps.org/iso13250/model/subtype";

	/**
	 * set of all TMDM default subject-identifier
	 */
	public static final Set<String> TMDM_IDENTIFIERS = HashUtil.getHashSet();

	static {
		TMDM_IDENTIFIERS.add(DEFAULT_NAME_TYPE);
		TMDM_IDENTIFIERS.add(INSTANCE_OF_TYPE);
		TMDM_IDENTIFIERS.add(TYPE_ROLE);
		TMDM_IDENTIFIERS.add(INSTANCE_ROLE);
		TMDM_IDENTIFIERS.add(KIND_OF_TYPE);
		TMDM_IDENTIFIERS.add(SUPERTYPE_ROLE);
		TMDM_IDENTIFIERS.add(SUBTYPE_ROLE);
	}
}
