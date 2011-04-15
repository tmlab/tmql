/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.insert.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.majortom.model.index.IRevisionIndex;
import de.topicmapslab.majortom.model.revision.IRevision;
import de.topicmapslab.majortom.model.revision.IRevisionChange;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestInsertExpression extends Tmql4JTestCase {

	@Test
	public void testInsertTopic() throws Exception {

		String subjectIdentifier = "http://psi.example.org/topic";
		assertNull(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(subjectIdentifier)));

		String query = null;
		SimpleResultSet set = null;

		query = " INSERT ''' " + subjectIdentifier + " . '''";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(1L, set.first().first());

		assertNotNull(topicMap.getTopicBySubjectIdentifier(topicMap.createLocator(subjectIdentifier)));
	}

	@Test
	public void testInsertAnAssociation() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");

		assertEquals(0, topic.getRolesPlayed().size());

		String query = null;
		SimpleResultSet set = null;

		query = " INSERT ''' " + base + "myType ( " + base + "myType : " + base + "myTopic ) '''";
		set = execute(new TMQLQuery(topicMap, query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(3L, set.first().first());

		assertEquals(1, topic.getRolesPlayed().size());
		assertEquals(1, topic.getRolesPlayed(type).size());
		assertEquals(type, topic.getRolesPlayed(type).iterator().next().getParent().getType());
	}

	@Test
	public void testInsertFailingIRIs() throws Exception {
		String queries[] = { "INSERT '''<http://maiana.topicmapslab.de/u/peter/tm/archiv-ostpreussen/#haus> - \"some name\" . '''",
				"INSERT '''<http://en.wikipedia.org/wiki/Munich_(district)> - \"some name\" . ''' " };

		for (String q : queries) {
			execute(new TMQLQuery(topicMap, q));
		}

		IRevisionIndex index = topicMap.getIndex(IRevisionIndex.class);
		index.open();
		IRevision r = index.getFirstRevision();
		while (r != null) {
			System.out.println(r.getChangesetType().name());
			for (IRevisionChange c : r.getChangeset()) {
				System.out.println("\t" + c.getType().name());
			}
			r = r.getFuture();
		}

	}

	@Test
	public void testTemplateDef() throws Exception {
		String query = "INSERT ''' %prefix tmcl http://psi.topicmaps.org/tmcl/  %prefix tmdm http://psi.topicmaps.org/iso13250/model  def overlaps($tt1, $tt2) ?c isa tmcl:overlap-declaration. tmcl:overlaps(tmcl:allows : ?c, tmcl:allowed : $tt1) tmcl:overlaps(tmcl:allows : ?c, tmcl:allowed : $tt2) end  overlaps( <http://en.wikipedia.org/wiki/State_(administrative_division)>, <http://en.wikipedia.org/wiki/City>) '''";
		execute(query);
	}

}
