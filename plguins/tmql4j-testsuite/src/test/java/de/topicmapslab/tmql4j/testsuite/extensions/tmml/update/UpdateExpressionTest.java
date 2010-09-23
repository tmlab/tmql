/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.testsuite.extensions.tmml.update;

import org.junit.Assert;
import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.tmql4j.common.core.query.TMQLQuery;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class UpdateExpressionTest extends BaseTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uni_leipzig.topicmapslab.tmql.testsuite.base.BaseTest#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testAddLocator() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini >> locators";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE locators ADD \"http://locators.puccini.net\" WHERE o:Puccini ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> locators";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testAddIndicator() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini >> indicators";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " UPDATE indicators ADD \"http://indicators.puccini.net\" WHERE o:Puccini ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> indicators";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
	}

	public void testAddItemIdentifier() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini >> item";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " UPDATE item ADD \"http://item.puccini.net\" WHERE o:Puccini ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> item";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
	}

	public void testAddNameWithoutType() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini >> characteristics tm:name";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());

		query = prefix
				+ " UPDATE names ADD \"Puccini, the Composer\" WHERE o:Puccini ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> characteristics tm:name";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(4, set.size());
	}

	public void testAddNameWithType() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini >> characteristics o:webpage";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(4, set.size());

		query = prefix
				+ " UPDATE names o:webpage ADD \"Puccini, the Composer\" WHERE o:Puccini ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " o:Puccini >> characteristics tm:name [ ^ o:webpage ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testSetNameByScope() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics tm:name [ @ o:short_name ]";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals("Puccini", ((Name) set.first().first()).getValue());

		query = prefix
				+ " UPDATE names SET \"Puccini, the Composer\" WHERE o:Puccini >> characteristics tm:name [ @ o:short_name ] ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " o:Puccini >> characteristics tm:name [ @ o:short_name ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals("Puccini, the Composer", ((Name) set.first()
				.first()).getValue());
	}

	public void testAddOccurrence() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini >> characteristics tm:occurrence";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(15, set.size());

		query = prefix
				+ " UPDATE occurrences ADD \"http://the-real-puccini.com\"^^xsd:anyURI WHERE o:Puccini";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> characteristics tm:occurrence";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(16, set.size());
	}

	public void testAddOccurrenceWithType() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics tm:occurrence [ ^ o:webpage ]";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(4, set.size());

		query = prefix
				+ " UPDATE occurrences o:webpage ADD \"http://the-real-puccini.com\" WHERE o:Puccini";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " o:Puccini >> characteristics tm:occurrence [ ^ o:webpage ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(5, set.size());
	}

	public void testSetOccurrenceByScope() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics o:website [ @ o:Web ]";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals("http://www.puccini.it/", ((Occurrence) set.first()
				.first()).getValue());

		query = prefix
				+ " UPDATE occurrences SET \"http://the-real-puccini.com\" WHERE o:Puccini >> characteristics o:website [ @ o:Web ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> characteristics o:website [ @ o:Web ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals("http://the-real-puccini.com", ((Occurrence) set
				.first().first()).getValue());
	}

	public void testAddScopeToOccurrence() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics o:website [ @ o:Web ]";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, ((Occurrence) set.first().first()).getScope()
				.size());

		query = prefix + " o:Puccini >> characteristics [ @ o:short_name ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " UPDATE scope ADD o:short_name WHERE o:Puccini >> characteristics o:website [ @ o:Web ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> characteristics o:website [ @ o:Web ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(4, ((Occurrence) set.first().first()).getScope()
				.size());

		query = prefix + " o:Puccini >> characteristics [ @ o:short_name ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
	}

	public void testAddScopeToName() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics  tm:name [ @ o:short_name ]";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, ((Name) set.first().first()).getScope().size());

		query = prefix
				+ " o:Puccini >> characteristics  tm:name [ @ o:normal ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " UPDATE scope ADD o:normal WHERE o:Puccini >> characteristics tm:name [ @ o:short_name ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " o:Puccini >> characteristics  tm:name [ @ o:short_name ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, ((Name) set.first().first()).getScope().size());

		query = prefix
				+ " o:Puccini >> characteristics  tm:name [ @ o:normal ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
	}

	public void testAddScopeToAssociation() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini << players >> scope";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE scope ADD o:Web WHERE o:Puccini << players o:Composer [ . >> players o:Work == o:La_Boheme ] ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini << players o:Composer >> scope";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testAddTypeToTopic() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini >> types";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " UPDATE types ADD o:Opera WHERE o:Puccini ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> types";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
	}

	public void testSetTypeToOccurrenceTransformAllWebsitesToWebpages()
			throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini / o:webpage ";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(4, set.size());

		query = prefix + "  o:Puccini / o:website";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix
				+ " UPDATE types SET o:webpage WHERE o:Puccini >> characteristics o:website";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "  o:Puccini / o:webpage";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(6, set.size());

		query = prefix + "  o:Puccini / o:website";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());
	}

	public void testSetTypeToNames() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics tm:name >> types";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "  o:Puccini / o:webpage";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(4, set.size());

		query = prefix
				+ " UPDATE types SET o:webpage WHERE o:Puccini >> characteristics tm:name [ @ o:short_name ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "  o:Puccini >> characteristics tm:name >> types";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix + "  o:Puccini / o:webpage";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(5, set.size());
	}

	public void testSetTypeToAssociation() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ "  SELECT $opera WHERE o:born_in ( o:Composer : o:Puccini , o:Work :  $opera )";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE types SET o:born_in WHERE o:Puccini << players o:Composer [ . >> players o:Work == o:La_Boheme ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ "  SELECT $opera WHERE o:born_in ( o:Composer : o:Puccini , o:Work : $opera )";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testAddInstance() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + "  o:Opera >> instances";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(171, set.size());

		query = prefix + " UPDATE instances ADD o:Puccini WHERE o:Opera";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "   o:Opera >> instances";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(172, set.size());
	}

	public void testAddSupertypes() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + "  o:Composer >> supertypes";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " UPDATE supertypes ADD o:Opera WHERE o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "  o:Composer >> supertypes";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testAddSubtypes() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + "  o:Opera >> subtypes";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " UPDATE subtypes ADD o:Composer WHERE o:Opera";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "  o:Opera >> subtypes";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testSetPlayersWithoutRoleType() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Franco_Alfano << players o:Work >> players o:Composer";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE players SET o:Franco_Alfano WHERE o:Puccini << players o:Composer [ . >> players o:Work == o:La_Boheme ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " o:Franco_Alfano << players o:Work >> players o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testSetPlayersWithRoleType() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Franco_Alfano << players o:Composer >> players o:Work";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(13, set.size());

		query = prefix
				+ " o:Franco_Alfano << players o:Work >> players o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE players o:Composer SET o:Franco_Alfano WHERE o:Puccini << players o:Composer [ . >> players o:Work == o:La_Boheme ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " o:Franco_Alfano << players o:Composer >> players o:Work";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(14, set.size());

		query = prefix
				+ " o:Franco_Alfano << players o:Work >> players o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());
	}

	public void testAddRolesWithoutPlayer() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini << players o:Composer >> players";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(13, set.size());

		query = prefix
				+ " o:Puccini << players o:Composer >> players o:Franco_Alfano";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE roles ADD o:Franco_Alfano WHERE o:Puccini << players o:Composer [ . >> players o:Work == o:La_Boheme ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini << players o:Composer >> players";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(14, set.size());

		query = prefix
				+ " o:Puccini << players o:Composer >> players o:Franco_Alfano";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testAddRolesWithPlayer() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:La_Boheme << players >> players";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(22, set.size());

		query = prefix + " o:La_Boheme << players o:Work >> players o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " UPDATE roles o:Franco_Alfano ADD o:Composer WHERE o:Puccini << players o:Composer [ . >> players o:Work == o:La_Boheme ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:La_Boheme << players >> players";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(23, set.size());

		query = prefix + " o:La_Boheme << players o:Work >> players o:Composer";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());
	}

	public void testSetReifierToOccurrence() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics tm:occurrence << reifier";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " o:La_Boheme >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE reifier SET o:La_Boheme WHERE o:Puccini >> characteristics o:website [ @ o:Web ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " o:Puccini >> characteristics tm:occurrence << reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		Assert.assertEquals(execute(new TMQLQuery("o:La_Boheme")).first()
				.first(), set.first().first());

		query = prefix + " o:La_Boheme >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testSetReifierToName() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics tm:name << reifier";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " o:La_Boheme >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE reifier SET o:La_Boheme WHERE o:Puccini >> characteristics tm:name [ . >> atomify == \"Puccini\" ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> characteristics tm:name << reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		Assert.assertEquals(execute(new TMQLQuery("o:La_Boheme")).first()
				.first(), set.first().first());

		query = prefix + " o:La_Boheme >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testSetReifierToAssociation() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini << players o:Composer << reifier";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " o:Web >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE reifier SET o:Web WHERE o:Puccini << players o:Composer [ . >> players o:Work == o:La_Boheme ]";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "  o:Puccini << players o:Composer << reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		Assert.assertEquals(execute(new TMQLQuery("o:Web")).first().first(),
				set.first().first());

		query = prefix + " o:Web >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testSetTopicAsReifierToOccurrences() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics o:date_of_birth << reifier";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " o:Web >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE reifier SET o:Puccini >> characteristics o:date_of_birth [0] WHERE o:Web";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ "  o:Puccini >> characteristics o:date_of_birth << reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		Assert.assertEquals(execute(new TMQLQuery("o:Web")).first().first(),
				set.first().first());

		query = prefix + " o:Web >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testSetTopicAsReifierToNames() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix
				+ " o:Puccini >> characteristics tm:name << reifier";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " o:Web >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE reifier SET o:Puccini >> characteristics tm:name [0] WHERE o:Web";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "  o:Puccini >> characteristics tm:name << reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		Assert.assertEquals(execute(new TMQLQuery("o:Web")).first().first(),
				set.first().first());

		query = prefix + " o:Web >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testSetTopicAsReifierToAssociation() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini << players o:Composer << reifier";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " o:Web >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ " UPDATE reifier SET o:Puccini << players o:Composer [0] WHERE o:Web";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + "  o:Puccini << players o:Composer << reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		Assert.assertEquals(execute(new TMQLQuery("o:Web")).first().first(),
				set.first().first());

		query = prefix + " o:Web >> reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testMultipleUpdates() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini >> locators";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " o:Puccini / tm:name ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());

		query = prefix + " o:Puccini >> reifier ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix + " o:Franco_Alfano << players o:Composer << reifier ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ "UPDATE locators ADD \"http://locators.ontopia.net/\" , names ADD \"Puccini, the Composer\" , reifier SET o:Franco_Alfano << players o:Composer [0] WHERE o:Puccini";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> locators";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini / tm:name ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(4, set.size());

		query = prefix + " o:Puccini >> reifier ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Franco_Alfano << players o:Composer << reifier ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testExtMultipleUpdates() throws Exception {
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = prefix + " o:Puccini / tm:name ";
		SimpleResultSet set = execute(new TMQLQuery(query));
		Assert.assertEquals(3, set.size());

		query = prefix
				+ " o:Puccini  >> characteristics tm:name [ @ o:short_name ] ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix + " o:Puccini >> characteristics tm:name << reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(0, set.size());

		query = prefix
				+ "UPDATE scope ADD o:short_name , reifier SET o:Web WHERE o:Puccini >> characteristics tm:name [ @ o:normal ] ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());

		query = prefix
				+ " o:Puccini  >> characteristics tm:name [ @ o:short_name ] ";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(2, set.size());

		query = prefix + " o:Puccini >> characteristics tm:name << reifier";
		set = execute(new TMQLQuery(query));
		Assert.assertEquals(1, set.size());
	}

	public void testAddTopic() throws Exception {

		int size = topicMap.getTopics().size();

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		SimpleResultSet set = null;

		/*
		 * default identifier type
		 */
		query = prefix
				+ " UPDATE topics ADD \"http://psi.example.org/topicDefaultIdentifier\"";
		set = execute(new TMQLQuery(query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap
				.getTopicBySubjectIdentifier(topicMap
						.createLocator("http://psi.example.org/topicDefaultIdentifier")));

		/*
		 * canonical subject-identifier
		 */
		query = prefix
				+ " UPDATE topics ADD \"http://psi.example.org/topicSI\" << indicators";
		set = execute(new TMQLQuery(query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator("http://psi.example.org/topicSI")));

		/*
		 * non-canonical subject-identifier
		 */
		query = prefix
				+ " UPDATE topics ADD \"http://psi.example.org/topicSINCL\" ~";
		set = execute(new TMQLQuery(query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator("http://psi.example.org/topicSINCL")));

		/*
		 * canonical subject-locator
		 */
		query = prefix
				+ " UPDATE topics ADD \"http://psi.example.org/topicSL\" << locators";
		set = execute(new TMQLQuery(query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectLocator(topicMap
				.createLocator("http://psi.example.org/topicSL")));

		/*
		 * non-canonical subject-locator
		 */
		query = prefix
				+ " UPDATE topics ADD \"http://psi.example.org/topicSLNCL\" =";
		set = execute(new TMQLQuery(query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getTopicBySubjectLocator(topicMap
				.createLocator("http://psi.example.org/topicSLNCL")));

		/*
		 * canonical item-identifier
		 */
		query = prefix
				+ " UPDATE topics ADD \"http://psi.example.org/topicII\" << item";
		set = execute(new TMQLQuery(query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getConstructByItemIdentifier(topicMap
				.createLocator("http://psi.example.org/topicII")));

		/*
		 * non-canonical item-identifier
		 */
		query = prefix
				+ " UPDATE topics ADD \"http://psi.example.org/topicIINCL\" !";
		set = execute(new TMQLQuery(query));
		size++;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getConstructByItemIdentifier(topicMap
				.createLocator("http://psi.example.org/topicIINCL")));

		/*
		 * create multiple count of topics
		 */
		query = prefix
				+ " UPDATE topics ADD \"http://psi.example.org/1\" ! , topics ADD \"http://psi.example.org/2\" = , topics ADD \"http://psi.example.org/3\" ~";
		set = execute(new TMQLQuery(query));
		size += 3;

		assertEquals(1, set.size());
		assertEquals(size, topicMap.getTopics().size());
		assertNotNull(topicMap.getConstructByItemIdentifier(topicMap
				.createLocator("http://psi.example.org/1")));
		assertNotNull(topicMap.getTopicBySubjectLocator(topicMap
				.createLocator("http://psi.example.org/2")));
		assertNotNull(topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator("http://psi.example.org/3")));
	}

	public void testAddAssociation() throws Exception {
		TopicMap topicMap = TopicMapSystemFactory.newInstance()
				.newTopicMapSystem().createTopicMap("http://psi.example.org");

		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(
				topicMap);
		runtime.getProperties().enableLanguageExtensionTmqlUl(true);

		int size = topicMap.getAssociations().size();
		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		IResultSet<?> set = null;

		query = prefix
				+ " UPDATE associations ADD http://psi.example.org/association-type ( http://psi.example.org/role-type-1 : http://psi.example.org/player1 , http://psi.example.org/role-type-2 : http://psi.example.org/player2 )";
		set = runtime.run(query).getResults();
		size++;

		Topic associationType = topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator("http://psi.example.org/association-type"));
		Topic roleType1 = topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator("http://psi.example.org/role-type-1"));
		Topic player1 = topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator("http://psi.example.org/player1"));
		Topic roleType2 = topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator("http://psi.example.org/role-type-2"));
		Topic player2 = topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator("http://psi.example.org/player2"));

		assertNotNull(associationType);
		assertNotNull(roleType1);
		assertNotNull(player1);
		assertNotNull(roleType2);
		assertNotNull(player2);

		assertEquals(size, topicMap.getAssociations().size());
		Association association = topicMap.getAssociations().iterator().next();
		assertEquals(associationType, association.getType());
		assertEquals(2, association.getRoles().size());
		assertEquals(1, association.getRoles(roleType1).size());
		assertEquals(player1, association.getRoles(roleType1).iterator().next()
				.getPlayer());
		assertEquals(1, association.getRoles(roleType2).size());
		assertEquals(player2, association.getRoles(roleType2).iterator().next()
				.getPlayer());

		Topic t = UpdateExpressionTest.topicMap
				.getTopicBySubjectIdentifier(UpdateExpressionTest.topicMap
						.createLocator("http://psi.ontopedia.net/Puccini"));
		int roles = t.getRolesPlayed().size();
		size = UpdateExpressionTest.topicMap.getAssociations().size();

		query = prefix
				+ " UPDATE associations ADD http://psi.example.org/association-type ( http://psi.example.org/role-type-1 : $composer , http://psi.example.org/role-type-2 : http://psi.example.org/player2 ) WHERE $composer ISA http://psi.ontopedia.net/Composer ";
		set = execute(query);
		size += 16;
		roles++;

		assertEquals(roles, t.getRolesPlayed().size());
		assertEquals(size, UpdateExpressionTest.topicMap.getAssociations()
				.size());

		query = prefix
				+ " http://psi.example.org/association-type ( http://psi.example.org/role-type-1 : http://psi.example.org/player1 , http://psi.example.org/role-type-2 : http://psi.example.org/player2 )";
		set = runtime.run(query).getResults();
	}

	public void testUpdateAssociationsByDef() throws Exception {

		TopicMap topicMap = TopicMapSystemFactory.newInstance()
				.newTopicMapSystem().createTopicMap("http://psi.example.org");

		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(
				topicMap);
		runtime.getProperties().enableLanguageExtensionTmqlUl(true);

		final String prefix = "%prefix o http://psi.ontopedia.net/ ";
		String query = null;
		IResultSet<?> set = null;

		query = prefix
				+ " UPDATE associations ADD http://psi.example.org/association-type ( http://psi.example.org/role-type-1 : http://psi.example.org/player1 , http://psi.example.org/role-type-2 : http://psi.example.org/player2 )";
		set = runtime.run(query).getResults();

		query = prefix
				+ " http://psi.example.org/association-new-type ( http://psi.example.org/role-type-1 : http://psi.example.org/player1 , http://psi.example.org/role-type-2 : http://psi.example.org/player2 )";
		set = runtime.run(query).getResults();
		assertEquals(0, set.size());

		query = prefix
				+ " UPDATE types SET http://psi.example.org/association-new-type WHERE http://psi.example.org/association-type ( http://psi.example.org/role-type-1 : http://psi.example.org/player1 , http://psi.example.org/role-type-2 : http://psi.example.org/player2 )";
		set = runtime.run(query).getResults();

		query = prefix
				+ " http://psi.example.org/association-new-type ( http://psi.example.org/role-type-1 : http://psi.example.org/player1 , http://psi.example.org/role-type-2 : http://psi.example.org/player2 )";
		set = runtime.run(query).getResults();
		assertEquals(1, set.size());
	}

}
