/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.template.tests;

import org.junit.Test;

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.template.components.processor.runtime.module.Template;
import de.topicmapslab.tmql4j.template.components.processor.runtime.module.TemplateManager;
import static junit.framework.Assert.*;
/**
 * @author Sven Krosse
 *
 */
public class TestTemplateDefinition extends Tmql4JTestCase {

	@Test(expected=TMQLRuntimeException.class)
	public void testErrorByRedefinition() throws Exception {
		execute("DEFINE TEMPLATE myTemplate \"\"\"<div>?value?</div>\"\"\"");
		/*
		 * may cause an exception
		 */
		execute("DEFINE TEMPLATE myTemplate \"\"\"<div>?value?</div>\"\"\"");
	}
	
	@Test
	public void testDefintion() throws Exception {
		TemplateManager manager = TemplateManager.getTemplateManager();
		
		assertFalse(manager.isKnownTemplate(topicMap, "myTemplate"));
		execute("DEFINE TEMPLATE myTemplate \"\"\"<div>?value?</div>\"\"\"");		
		assertTrue(manager.isKnownTemplate(topicMap, "myTemplate"));
		assertEquals("<div>?value?</div>", manager.getTemplate(topicMap, "myTemplate").getDefinition());
		
		assertFalse(manager.isKnownTemplate(topicMap, "myTemplate2"));
		execute("DEFINE TEMPLATE myTemplate2 \"\"\"<div>?value?<br />?name?</div>\"\"\"");
		assertTrue(manager.isKnownTemplate(topicMap, "myTemplate2"));
		assertEquals("<div>?value?<br />?name?</div>", manager.getTemplate(topicMap, "myTemplate2").getDefinition());
		
		execute("REDEFINE TEMPLATE myTemplate2 \"\"\"<div>?value?<br />?other?</div>\"\"\"");
		assertTrue(manager.isKnownTemplate(topicMap, "myTemplate2"));
		assertEquals("<div>?value?<br />?other?</div>", manager.getTemplate(topicMap, "myTemplate2").getDefinition());
		
		execute("DELETE TEMPLATE myTemplate2 ");
		assertFalse(manager.isKnownTemplate(topicMap, "myTemplate2"));
		assertNull(manager.getTemplate(topicMap, "myTemplate2"));
	}
	
	@Test
	public void testAnonymousDefintion() throws Exception {
		TemplateManager manager = TemplateManager.getTemplateManager();
		assertFalse(manager.isKnownTemplate(topicMap, Template.ANONYMOUS));
		execute("%pragma TEMPLATE \"\"\"<div>?value?</div>\"\"\" // tm:subject ");		
		assertTrue(manager.isKnownTemplate(topicMap, Template.ANONYMOUS));
		assertEquals("<div>?value?</div>", manager.getTemplate(topicMap, Template.ANONYMOUS).getDefinition());
	}
}
