package de.topicmapslab.tmql4j.testsuite.other;

import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

public class TestCommentLine extends BaseTest {

	public void testCommentLine() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		IResultSet<?> set = null;

		query = prefix + " // tm:subject # should be ignored";
		set = execute(query);
		assertTrue(set.size() > 0);
		
		query = prefix + " // tm:subject#should be failed";
		try {
			set = execute(query);
			fail("Invalid query should fail!");
		} catch (Exception e) {
			// NOTHING TO DO
		}
		
		query = prefix + " // tm:subject\r\n# should be ignored";
		set = execute(query);
		assertTrue(set.size() > 0);
		
		query = prefix + " // tm:subject\n# should be ignored";
		set = execute(query);
		assertTrue(set.size() > 0);
		
		query = prefix + " // tm:subject\n# should be ignored \n[ . >> types == o:Composer ]";
		set = execute(query);
		assertEquals(16,set.size());
		
		query = prefix + " // tm:subject\r\n# should be ignored \n[ . >> types == o:Composer ]";
		set = execute(query);
		assertEquals(16,set.size());
		
		query = prefix + " // tm:subject [ $# == 1 ]";
		set = execute(query);
		assertEquals(1,set.size());
		
		query = prefix + " // tm:subject [ $# == 1 ] # should be ignored";
		set = execute(query);
		assertEquals(1,set.size());
		
		query = prefix + " // tm:subject  # should be ignored [ $# == 1 ]";
		set = execute(query);
		assertTrue(set.size() > 1);
		
		query = prefix + " // tm:subject >> characteristics tm:name [ . >> scope == http://www.topicmaps.org/xtm/1.0/language.xtm#it ]";
		set = execute(query);
		assertEquals(29,set.size());
		
		query = prefix + " // tm:subject >> characteristics tm:name [ . >> scope == http://www.topicmaps.org/xtm/1.0/language.xtm#it ] # should be ignored";
		set = execute(query);
		assertEquals(29,set.size());
	}

}
