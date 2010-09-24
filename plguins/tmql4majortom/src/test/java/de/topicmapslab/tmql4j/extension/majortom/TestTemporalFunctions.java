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

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import de.topicmapslab.majortom.model.core.IOccurrence;
import de.topicmapslab.tmql4j.extension.majortom.expression.GetDates;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class TestTemporalFunctions extends Tmql4JTestCase {

	@Test
	public void testGetDates() throws Exception {
		Calendar calenderWithoutTime = new GregorianCalendar(2010, 10, 10);
		Calendar calenderWithTime = new GregorianCalendar(2010, 10, 10, 10, 10, 10);

		IOccurrence occurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), "V");
		occurrence.setValue(calenderWithoutTime);
		IOccurrence otherOccurrence = (IOccurrence) createTopic().createOccurrence(createTopic(), "V2");
		otherOccurrence.setValue(calenderWithTime);

		String query;
		SimpleResultSet set = null;

		query = "SELECT " + GetDates.GetDatesIdentifier + "( 2010, 10, 10,0,0,0 )";
		System.out.println(query);
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());
		
		query = "SELECT " + GetDates.GetDatesIdentifier + "( \"2010-10-10T00:00:00\")";
		System.out.println(query);
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());

		query = "SELECT " + GetDates.GetDatesIdentifier + "( 2010, 10, 10 )";
		System.out.println(query);
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(occurrence, set.first().first());
	}

}
