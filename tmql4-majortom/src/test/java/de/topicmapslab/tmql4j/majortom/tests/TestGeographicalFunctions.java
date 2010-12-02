/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.geotype.wgs84.Wgs84Coordinate;
import de.topicmapslab.geotype.wgs84.Wgs84Degree;
import de.topicmapslab.majortom.model.core.IOccurrence;
import de.topicmapslab.majortom.util.XmlSchemeDatatypes;
import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetCoordinatesInDistance;
import de.topicmapslab.tmql4j.majortom.grammar.functions.GetDistance;
import de.topicmapslab.tmql4j.path.components.processor.results.SimpleResultSet;

/**
 * @author Sven Krosse
 * 
 */
public class TestGeographicalFunctions extends Tmql4JTestCase {

	@Test
	public void testGetCoordinatesByDistanceAsDouble() throws Exception {
		Wgs84Degree lat = new Wgs84Degree(38.692668);
		Wgs84Degree lng = new Wgs84Degree(-9.177944);
		// Lissabon Tejo Brücke
		Wgs84Coordinate coordinate = new Wgs84Coordinate(lat, lng);

		lat = new Wgs84Degree(52.5164);
		lng = new Wgs84Degree(13.3777);
		// Berlin Brandenburger Tor
		Wgs84Coordinate other = new Wgs84Coordinate(lat, lng);

		IOccurrence occurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), coordinate.toString(), topicMap.createLocator(XmlSchemeDatatypes.WGS84_COORDINATE));
		IOccurrence otherOccurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), other.toString(), topicMap.createLocator(XmlSchemeDatatypes.WGS84_COORDINATE));

		String query;
		SimpleResultSet set = null;

		query = GetCoordinatesInDistance.GetCoordinatesInDistanceIdentifier + " ( " + coordinate.getLatitude() + " , " + coordinate.getLongitude() + " , 0 )";
		System.out.println(query);
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = GetCoordinatesInDistance.GetCoordinatesInDistanceIdentifier + " ( " + other.getLatitude() + " , " + other.getLongitude() + " , 0 )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(otherOccurrence, set.first().first());

		query = GetCoordinatesInDistance.GetCoordinatesInDistanceIdentifier + " ( " + other.getLatitude() + " , " + other.getLongitude() + " , " + coordinate.getDistance(other) + " )";
		set = execute(query);
		assertEquals(2, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(r.first().equals(occurrence) || r.first().equals(otherOccurrence));
		}
	}

	@Test
	public void testGetCoordinatesByDistanceAsString() throws Exception {
		Wgs84Degree lat = new Wgs84Degree(38.692668);
		Wgs84Degree lng = new Wgs84Degree(-9.177944);
		// Lissabon Tejo Brücke
		Wgs84Coordinate coordinate = new Wgs84Coordinate(lat, lng);

		lat = new Wgs84Degree(52.5164);
		lng = new Wgs84Degree(13.3777);
		// Berlin Brandenburger Tor
		Wgs84Coordinate other = new Wgs84Coordinate(lat, lng);

		Topic topic = createTopic();
		IOccurrence occurrence = (IOccurrence) topic.createOccurrence(createTopic(), coordinate.toString(), topicMap.createLocator(XmlSchemeDatatypes.WGS84_COORDINATE));
		Topic otherTopic = createTopic();
		IOccurrence otherOccurrence = (IOccurrence) otherTopic.createOccurrence(createTopic(), other.toString(), topicMap.createLocator(XmlSchemeDatatypes.WGS84_COORDINATE));

		String query;
		SimpleResultSet set = null;

		query = GetCoordinatesInDistance.GetCoordinatesInDistanceIdentifier + " ( \"" + coordinate.toString() + "\" , 0 )";
		System.out.println(query);
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = GetCoordinatesInDistance.GetCoordinatesInDistanceIdentifier + " ( \"" + other.toString() + "\" , 0 )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(otherOccurrence, set.first().first());

		query = GetCoordinatesInDistance.GetCoordinatesInDistanceIdentifier + " ( \"" + coordinate.toString() + "\" , " + coordinate.getDistance(other) + " )";
		set = execute(query);
		assertEquals(2, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(r.first().equals(occurrence) || r.first().equals(otherOccurrence));
		}
	}

	@Test
	public void testGetCoordinatesByDistanceParent() throws Exception {
		Wgs84Degree lat = new Wgs84Degree(38.692668);
		Wgs84Degree lng = new Wgs84Degree(-9.177944);
		// Lissabon Tejo Brücke
		Wgs84Coordinate coordinate = new Wgs84Coordinate(lat, lng);

		lat = new Wgs84Degree(52.5164);
		lng = new Wgs84Degree(13.3777);
		// Berlin Brandenburger Tor
		Wgs84Coordinate other = new Wgs84Coordinate(lat, lng);

		Topic topic = createTopic();
		topic.createOccurrence(createTopic(), coordinate.toString(), topicMap.createLocator(XmlSchemeDatatypes.WGS84_COORDINATE));
		Topic otherTopic = createTopic();
		otherTopic.createOccurrence(createTopic(), other.toString(), topicMap.createLocator(XmlSchemeDatatypes.WGS84_COORDINATE));

		String query;
		SimpleResultSet set = null;

		query = "FOR $c IN " + GetCoordinatesInDistance.GetCoordinatesInDistanceIdentifier + " ( \"" + coordinate.toString() + "\" , " + coordinate.getDistance(other)
				+ " ) RETURN $c << characteristics";
		set = execute(query);
		assertEquals(2, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(r.first().equals(topic) || r.first().equals(otherTopic));
		}

		query = "FOR $c IN " + GetCoordinatesInDistance.GetCoordinatesInDistanceIdentifier + " ( \"" + coordinate.toString() + "\" , " + coordinate.getDistance(other)
				+ " ) RETURN ( $c << characteristics , fn:distance ( \"" + coordinate.toString() + "\" , $c >> atomify [0] ) )";
		set = execute(query);
		assertEquals(2, set.size());
		for (IResult r : set) {
			assertEquals(2, r.size());
			assertTrue(r.first().equals(topic) || r.first().equals(otherTopic));
			assertTrue(r.get(1).equals(0D) || r.get(1).equals(coordinate.getDistance(other)));
		}
		System.out.println(set);
	}

	@Test
	public void testGetDistanceAsDouble() throws Exception {
		Wgs84Degree lat = new Wgs84Degree(38.692668);
		Wgs84Degree lng = new Wgs84Degree(-9.177944);
		// Lissabon Tejo Brücke
		Wgs84Coordinate coordinate = new Wgs84Coordinate(lat, lng);

		lat = new Wgs84Degree(52.5164);
		lng = new Wgs84Degree(13.3777);
		// Berlin Brandenburger Tor
		Wgs84Coordinate other = new Wgs84Coordinate(lat, lng);

		createTopic().createOccurrence(createTopic(), coordinate.toString(), topicMap.createLocator(XmlSchemeDatatypes.WGS84_COORDINATE));
		createTopic().createOccurrence(createTopic(), other.toString(), topicMap.createLocator(XmlSchemeDatatypes.WGS84_COORDINATE));

		String query;
		SimpleResultSet set = null;

		query = GetDistance.GetDistanceIdentifier + " ( " + coordinate.getLatitude() + " , " + coordinate.getLongitude() + " , " + coordinate.getLatitude() + " , "
				+ coordinate.getLongitude() + " )";
		System.out.println(query);
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(0D, set.first().first());

		query = GetDistance.GetDistanceIdentifier + " ( " + other.getLatitude() + " , " + other.getLongitude() + " , " + other.getLatitude() + " , " + other.getLongitude() + " )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(0D, set.first().first());

		query = GetDistance.GetDistanceIdentifier + " ( " + coordinate.getLatitude() + " , " + coordinate.getLongitude() + " , " + other.getLatitude() + " , " + other.getLongitude() + " )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(coordinate.getDistance(other), set.first().first());

		query = GetDistance.GetDistanceIdentifier + " ( " + other.getLatitude() + " , " + other.getLongitude() + " , " + coordinate.getLatitude() + " , " + coordinate.getLongitude() + " )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(coordinate.getDistance(other), set.first().first());

	}

	@Test
	public void testGetDistanceAsString() throws Exception {
		Wgs84Degree lat = new Wgs84Degree(38.692668);
		Wgs84Degree lng = new Wgs84Degree(-9.177944);
		// Lissabon Tejo Brücke
		Wgs84Coordinate coordinate = new Wgs84Coordinate(lat, lng);

		lat = new Wgs84Degree(52.5164);
		lng = new Wgs84Degree(13.3777);
		// Berlin Brandenburger Tor
		Wgs84Coordinate other = new Wgs84Coordinate(lat, lng);

		createTopic().createOccurrence(createTopic(), coordinate.toString(), topicMap.createLocator(XmlSchemeDatatypes.WGS84_COORDINATE));
		createTopic().createOccurrence(createTopic(), other.toString(), topicMap.createLocator(XmlSchemeDatatypes.WGS84_COORDINATE));

		String query;
		SimpleResultSet set = null;

		query = GetDistance.GetDistanceIdentifier + " ( \"" + coordinate.toString() + "\" , \"" + coordinate.toString() + "\" )";
		System.out.println(query);
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(0D, set.first().first());

		query = GetDistance.GetDistanceIdentifier + " ( \"" + other.toString() + "\" , \"" + other.toString() + "\" )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(0D, set.first().first());

		query = GetDistance.GetDistanceIdentifier + " ( \"" + coordinate.toString() + "\" , \"" + other.toString() + "\" )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(coordinate.getDistance(other), set.first().first());

		query = GetDistance.GetDistanceIdentifier + " ( \"" + other.toString() + "\" , \"" + coordinate.toString() + "\" )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(coordinate.getDistance(other), set.first().first());

	}

}
