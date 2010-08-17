/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.other;

import junit.framework.Assert;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class PropertyTest extends BaseTest {

	public void testEnableTmqlUlExtension() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		query = prefix + " DELETE CASCADE o:Puccini";
		execute(query);

		try {
			runtime.getProperties().enableLanguageExtensionTmqlUl(false);
			execute(query);
		} catch (Exception e) {
			Assert
					.assertEquals(
							"Parser failed because of Keyword 'DELETE' not allowed at current TMQL session, because the TMQL-UL extension is disabled.",
							e.getMessage());
		}
	}

}
