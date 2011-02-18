/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.hatana.query.transformer;

import java.util.List;
import java.util.Set;

/**
 * @author Sven Krosse
 * 
 */
public interface IPSIRegistry {

	public List<Set<String>> getIdentifiersBySubjectIdentifier(final String identifier);

	public List<Set<String>> getIdentifiersBySubjectLocator(final String identifier);

	public List<Set<String>> getIdentifiersByItemIdentifier(final String identifier);

}
