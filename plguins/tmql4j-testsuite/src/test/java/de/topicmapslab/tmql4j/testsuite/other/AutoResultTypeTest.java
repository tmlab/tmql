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
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.ResultType;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class AutoResultTypeTest extends BaseTest {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testAutoResultTypeSwitching() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		IResultSet<?> set = null;
		query = prefix
				+ " RETURN <composers> { FOR $composer IN // o:Composer RETURN <composer> <name>{ $composer / tm:name [0] }</name> <name>{ $composer / tm:name [1] }</name> </composer> } </composers>";
		set = execute(query);
		Assert.assertEquals(ResultType.XML.name(), set.getResultType());

		query = prefix
				+ " FOR $composer IN // o:Composer RETURN $composer / tm:name";
		set = execute(query);
		Assert.assertEquals(ResultType.TMAPI.name(), set.getResultType());

		query = prefix
				+ " FOR $composer IN // o:Composer RETURN \"\"\" { $composer } \"\"\"";
		set = execute(query);
		Assert.assertEquals(ResultType.CTM.name(), set.getResultType());

	}
}
