/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.majortom;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import de.topicmapslab.majortom.model.core.IOccurrence;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetDates;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetDatesAfter;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetDatesBefore;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetDatesInRange;
import de.topicmapslab.tmql4j.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;

/**
 * @author Sven Krosse
 * 
 */
public class TestTemporalFunctions extends Tmql4JTestCase {

	@Test
	public void testGetDates() throws Exception {
		Calendar calenderWithoutTime = new GregorianCalendar(2010, 9, 10);

		Calendar calenderWithTime = new GregorianCalendar(2010, 9, 10, 10, 10, 10);

		IOccurrence occurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), "V");
		occurrence.setValue(calenderWithoutTime);
		IOccurrence otherOccurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), "V2");
		otherOccurrence.setValue(calenderWithTime);

		String query;
		SimpleResultSet set = null;

		query = "SELECT " + GetDates.GetDatesIdentifier + "( 2010, 10, 10, 0, 0, 0 )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = "SELECT " + GetDates.GetDatesIdentifier + "( 2010, 10, 10 )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = "SELECT " + GetDates.GetDatesIdentifier + "( \"2010-10-10T00:00:00\")";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = "SELECT " + GetDates.GetDatesIdentifier + "( 2010, 10, 10,10,10,10 )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(otherOccurrence, set.first().first());

		query = "SELECT " + GetDates.GetDatesIdentifier + "( \"2010-10-10T10:10:10\")";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(otherOccurrence, set.first().first());
	}

	@Test
	public void testGetDatesAfter() throws Exception {
		Calendar calenderWithoutTime = new GregorianCalendar(2010, 9, 10);

		Calendar calenderWithTime = new GregorianCalendar(2010, 10, 10, 10, 10, 10);

		IOccurrence occurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), "V");
		occurrence.setValue(calenderWithoutTime);
		IOccurrence otherOccurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), "V2");
		otherOccurrence.setValue(calenderWithTime);

		String query;
		SimpleResultSet set = null;

		query = "SELECT " + GetDatesAfter.GetDatesAfterIdentifier + "( 2010, 10, 10, 0, 0, 0 )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(otherOccurrence, set.first().first());

		query = "SELECT " + GetDatesAfter.GetDatesAfterIdentifier + "( 2010, 10, 10 )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(otherOccurrence, set.first().first());

		query = "SELECT " + GetDatesAfter.GetDatesAfterIdentifier + "( \"2010-10-10T00:00:00\")";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(otherOccurrence, set.first().first());

		query = "SELECT " + GetDatesAfter.GetDatesAfterIdentifier + "( 2010, 8, 10,0,0,0 )";
		set = execute(query);
		assertEquals(2, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(otherOccurrence.equals(r.first()) || occurrence.equals(r.first()));
		}

		query = "SELECT " + GetDatesAfter.GetDatesAfterIdentifier + "( \"2010-08-10T10:10:10\")";
		set = execute(query);
		assertEquals(2, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(otherOccurrence.equals(r.first()) || occurrence.equals(r.first()));
		}
	}

	@Test
	public void testGetDatesBefore() throws Exception {
		Calendar calenderWithoutTime = new GregorianCalendar(2010, 9, 10);

		Calendar calenderWithTime = new GregorianCalendar(2010, 10, 10, 10, 10, 10);

		IOccurrence occurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), "V");
		occurrence.setValue(calenderWithoutTime);
		IOccurrence otherOccurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), "V2");
		otherOccurrence.setValue(calenderWithTime);

		String query;
		SimpleResultSet set = null;

		query = "SELECT " + GetDatesBefore.GetDatesBeforeIdentifier + "( 2010, 11, 10, 0, 0, 0 )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = "SELECT " + GetDatesBefore.GetDatesBeforeIdentifier + "( 2010, 11, 10 )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = "SELECT " + GetDatesBefore.GetDatesBeforeIdentifier + "( \"2010-11-10T00:00:00\")";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = "SELECT " + GetDatesBefore.GetDatesBeforeIdentifier + "( 2010, 12, 10,0,0,0 )";
		set = execute(query);
		assertEquals(2, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(otherOccurrence.equals(r.first()) || occurrence.equals(r.first()));
		}

		query = "SELECT " + GetDatesBefore.GetDatesBeforeIdentifier + "( \"2010-12-10T10:10:10\")";
		set = execute(query);
		assertEquals(2, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(otherOccurrence.equals(r.first()) || occurrence.equals(r.first()));
		}
	}

	@Test
	public void testGetDatesInRange() throws Exception {
		Calendar calenderWithoutTime = new GregorianCalendar(2010, 8, 10);

		Calendar calenderWithTime = new GregorianCalendar(2010, 9, 10, 10, 10, 10);

		IOccurrence occurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), "V");
		occurrence.setValue(calenderWithoutTime);
		IOccurrence otherOccurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), "V2");
		otherOccurrence.setValue(calenderWithTime);

		String query;
		SimpleResultSet set = null;

		query = "SELECT " + GetDatesInRange.GetDatesInRangeIdentifier + "( 2010, 10, 10, 0, 0, 0 ,  2010, 10, 11, 0, 0, 0 )";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(otherOccurrence, set.first().first());

		query = "SELECT " + GetDatesInRange.GetDatesInRangeIdentifier + "( 2010, 10, 10, 2010, 10, 11)";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(otherOccurrence, set.first().first());

		query = "SELECT " + GetDatesInRange.GetDatesInRangeIdentifier + "( \"2010-10-10T00:00:00\" , \"2010-10-11T00:00:00\")";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(otherOccurrence, set.first().first());

		query = "SELECT " + GetDatesInRange.GetDatesInRangeIdentifier + "( 2010, 9, 9, 23, 59, 0 ,  2010,9,10,0,0,1)";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = "SELECT " + GetDatesInRange.GetDatesInRangeIdentifier + "( 2010, 9, 9, 2010, 9, 11)";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = "SELECT " + GetDatesInRange.GetDatesInRangeIdentifier + "( \"2010-09-09T23:59:59\" , \"2010-09-10T00:00:01\")";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = "SELECT " + GetDatesInRange.GetDatesInRangeIdentifier + "( 2010, 9, 9, 2010, 10 , 11 )";
		set = execute(query);
		assertEquals(2, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(otherOccurrence.equals(r.first()) || occurrence.equals(r.first()));
		}

		query = "SELECT " + GetDatesInRange.GetDatesInRangeIdentifier + "( 2010, 9, 9, 23,59,59, 2010, 10 , 10,10,10,11 )";
		set = execute(query);
		assertEquals(2, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(otherOccurrence.equals(r.first()) || occurrence.equals(r.first()));
		}

		query = "SELECT " + GetDatesInRange.GetDatesInRangeIdentifier + "( \"2010-09-09T23:59:59\", \"2010-10-10T10:10:11\" )";
		set = execute(query);
		assertEquals(2, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(otherOccurrence.equals(r.first()) || occurrence.equals(r.first()));
		}
	}
}
