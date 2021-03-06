/**
 * TMQL4J Plugin for Ontopia
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * Author: Sven Krosse
 * 
 */
package net.ontopia.topicmaps.query.tmql.impl.basic;

import java.io.File;

import junit.framework.TestCase;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.utils.ltm.LTMTopicMapReader;

public class TMQLQueryPluginTest extends TestCase {

	public void testStringTreeFunction() throws Exception {
		LTMTopicMapReader reader = new LTMTopicMapReader(new File("src/test/resources/ItalianOpera.ltm"));
		TopicMapIF tm = reader.read();

		TMQL4JQueryProcessor processor = new TMQL4JQueryProcessor(tm);

		processor.execute("FOR $variable IN // http://psi.ontopedia.net/Composer RETURN $variable ");

	}

}
