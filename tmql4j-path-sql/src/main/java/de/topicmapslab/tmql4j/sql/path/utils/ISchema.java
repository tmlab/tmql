/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.utils;

/**
 * @author Sven Krosse
 * 
 */
public interface ISchema {

	public interface Constructs {

		public static final String TABLE = "constructs";

		public static final String ID = "id";

		public static final String ID_PARENT = "id_parent";

		public static final String ID_TOPICMAP = "id_topicmap";
	}

	public interface Locators {

		public static final String TABLE = "locators";

		public static final String ID = Constructs.ID;

		public static final String REFERENCE = "reference";
	}

	public interface RelSubjectIdentifiers {

		public static final String TABLE = "rel_subject_identifiers";

		public static final String ID_TOPIC = "id_topic";

		public static final String ID_LOCATOR = "id_locator";
	}

	public interface RelSubjectLocators {

		public static final String TABLE = "rel_subject_locators";

		public static final String ID_TOPIC = "id_topic";

		public static final String ID_LOCATOR = "id_locator";
	}

	public interface RelItemIdentifiers {

		public static final String TABLE = "rel_item_identifiers";

		public static final String ID_CONSTRUCT = "id_construct";

		public static final String ID_LOCATOR = "id_locator";
	}

	public interface RelKindOf {

		public static final String TABLE = "rel_kind_of";

		public static final String ID_SUPERTYPE = "id_supertype";

		public static final String ID_SUBTYPE = "id_subtype";
	}

	public interface RelInstanceOf {

		public static final String TABLE = "rel_instance_of";

		public static final String ID_TYPE = "id_type";

		public static final String ID_INSTANCE = "id_instance";
	}

	public interface DatatypeAwares {

		public static final String TABLE = "datatypeawares";

		public static final String ID_DATATYPE = "id_datatype";
	}

	public interface Typeables {

		public static final String TABLE = "typeables";

		public static final String ID_TYPE = "id_type";
	}

	public interface Scopables {

		public static final String TABLE = "scopeables";

		public static final String ID_SCOPE = "id_scope";
	}

	public interface Reifiables {

		public static final String TABLE = "reifiables";

		public static final String ID_REIFIER = "id_reifier";
	}

	public interface RelThemes {

		public static final String TABLE = "rel_themes";

		public static final String ID_THEME = "id_theme";
	}

	public interface Topics {
		public static final String TABLE = "topics";
	}

	public interface Names {

		public static final String TABLE = "names";
	}

	public interface Occurrences {

		public static final String TABLE = "occurrences";
	}

	public interface Variants {

		public static final String TABLE = "variants";

		public static final String VALUE = "value";
	}

	public interface Associations {

		public static final String TABLE = "associations";
	}

	public interface Roles {

		public static final String TABLE = "roles";

		public static final String ID_PLAYER = "id_player";
	}

	public interface Characteristics {

		public static final String NAMES_AND_OCCURRENCES = "SELECT id_parent, id, value, id_type FROM names UNION SELECT id_parent, id, value, id_type FROM occurrences";

		public static final String NAMES_OCCURRENCES_AND_LOCATORS = "SELECT id_parent, id, value FROM names UNION SELECT id_parent, id, value FROM occurrences UNION SELECT NULL AS id_parent, id, reference AS value FROM locators";

		public static final String VALUE = "value";
	}

}
