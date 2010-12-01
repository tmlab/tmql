/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests.draft2010;

import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.query.IQuery;

/**
 * @author Sven Krosse
 * 
 */
public abstract class Draft2010Tmql4JTestCase extends Tmql4JTestCase {

	@SuppressWarnings("unchecked")
	protected <T extends IResultSet<?>> T execute(String query) throws TMQLRuntimeException {
		runtime.getProperties().enableMaterializeMetaModel(true);
		IQuery q = new TMQLQueryDraft2010(query);
		runtime.run(q);
		return (T) q.getResults();
	}

}
