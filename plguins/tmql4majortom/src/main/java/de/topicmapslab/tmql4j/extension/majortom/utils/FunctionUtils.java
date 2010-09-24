/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.extension.majortom.utils;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import de.topicmapslab.geotype.wgs84.Wgs84Coordinate;
import de.topicmapslab.majortom.util.DatatypeAwareUtils;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;

/**
 * @author Sven Krosse
 * 
 */
public class FunctionUtils {

	/**
	 * Convert the given tuple to calendar value
	 * 
	 * @param tuple
	 *            the tuple
	 * @return the calendar
	 * @throws ParseException
	 *             , URISyntaxException, NumberFormatException thrown by
	 *             transformer method
	 */
	public static Calendar getCalendar(Map<String, Object> tuple) throws ParseException, URISyntaxException, NumberFormatException {
		if (tuple.size() == 1) {
			return (Calendar) DatatypeAwareUtils.toValue(tuple.get("$0"), Calendar.class);
		} else if (tuple.size() == 3) {
			int year = (Integer) DatatypeAwareUtils.toValue(tuple.get("$0"), Integer.class);
			int month = ((Integer) DatatypeAwareUtils.toValue(tuple.get("$1"), Integer.class))-1;
			int day = (Integer) DatatypeAwareUtils.toValue(tuple.get("$2"), Integer.class);
			return new GregorianCalendar(year, month, day);
		} else if (tuple.size() == 6) {
			int year = (Integer) DatatypeAwareUtils.toValue(tuple.get("$0"), Integer.class);
			int month = ((Integer) DatatypeAwareUtils.toValue(tuple.get("$1"), Integer.class))-1;
			int day = (Integer) DatatypeAwareUtils.toValue(tuple.get("$2"), Integer.class);
			int hour = (Integer) DatatypeAwareUtils.toValue(tuple.get("$3"), Integer.class);
			int min = (Integer) DatatypeAwareUtils.toValue(tuple.get("$4"), Integer.class);
			int sec = (Integer) DatatypeAwareUtils.toValue(tuple.get("$5"), Integer.class);
			return new GregorianCalendar(year, month, day, hour, min, sec);
		}
		throw new TMQLRuntimeException("Unexpected number of arguments: " + tuple.size());
	}

	/**
	 * Convert the given tuple to calendars value
	 * 
	 * @param tuple
	 *            the tuple
	 * @param count
	 *            number of calendar values
	 * @return the calendars
	 * @throws ParseException
	 *             , URISyntaxException, NumberFormatException thrown by
	 *             transformer method
	 */
	public static Calendar[] getCalendars(Map<String, Object> tuple, int count) throws ParseException, URISyntaxException, NumberFormatException {
		Calendar[] calendars = new Calendar[count];
		for (int i = 0; i < count; i++) {
			if (tuple.size() == count) {
				calendars[i] = (Calendar) DatatypeAwareUtils.toValue(tuple.get("$" + Integer.toString(i)), Calendar.class);
			} else if (tuple.size() == 3 * count) {
				int year = (Integer) DatatypeAwareUtils.toValue(tuple.get("$" + Integer.toString(3 * i)), Integer.class);
				int month = (Integer) DatatypeAwareUtils.toValue(tuple.get("$" + Integer.toString(3 * i + 1)), Integer.class)-1;
				int day = (Integer) DatatypeAwareUtils.toValue(tuple.get("$" + Integer.toString(3 * i + 2)), Integer.class);
				calendars[i] = new GregorianCalendar(year, month, day);
			} else if (tuple.size() == 6 * count) {
				int year = (Integer) DatatypeAwareUtils.toValue(tuple.get("$" + Integer.toString(6 * i)), Integer.class);
				int month = (Integer) DatatypeAwareUtils.toValue(tuple.get("$" + Integer.toString(6 * i + 1)), Integer.class)-1;
				int day = (Integer) DatatypeAwareUtils.toValue(tuple.get("$" + Integer.toString(6 * i + 2)), Integer.class);
				int hour = (Integer) DatatypeAwareUtils.toValue(tuple.get("$" + Integer.toString(6 * i + 3)), Integer.class);
				int min = (Integer) DatatypeAwareUtils.toValue(tuple.get("$" + Integer.toString(6 * i + 4)), Integer.class);
				int sec = (Integer) DatatypeAwareUtils.toValue(tuple.get("$" + Integer.toString(6 * i + 5)), Integer.class);
				calendars[i] = new GregorianCalendar(year, month, day, hour, min, sec);
			} else {
				throw new TMQLRuntimeException("Unexpected number of arguments: " + tuple.size());
			}
		}
		return calendars;
	}

	/**
	 * Convert the given tuple to wgs84 coordinates
	 * 
	 * @param tuple
	 *            the tuple
	 * @param count
	 *            number of coordinate values
	 * @return the wgs84 coordinates
	 * @throws ParseException
	 *             thrown by transformer method
	 */
	public static Wgs84Coordinate[] getWgs84Coordinates(Map<String, Object> tuple, int count) throws ParseException {
		Wgs84Coordinate[] coordinates = new Wgs84Coordinate[count];
		for (int i = 0; i < count; i++) {
			if (tuple.size() == count) {
				Object oCoordinate = tuple.get("$" + Integer.valueOf(i));
				coordinates[i] = new Wgs84Coordinate(oCoordinate.toString());
			} else if (tuple.size() == 2 * count) {
				Object oLatitude = tuple.get("$" + Integer.valueOf(i * 2));
				Object oLongitude = tuple.get("$" + +Integer.valueOf(i * 2 + 1));
				Double latitude = LiteralUtils.asDouble(oLatitude);
				Double longitude = LiteralUtils.asDouble(oLongitude);
				coordinates[i] = new Wgs84Coordinate(latitude, longitude);
			} else {
				throw new TMQLRuntimeException("Unexpected number of arguments: " + tuple.size());
			}
		}
		return coordinates;
	}

	/**
	 * Convert the given tuple to wgs84 coordinate
	 * 
	 * @param tuple
	 *            the tuple
	 * @param additional
	 *            the number of additional arguments
	 * @return the wgs84 coordinate
	 * @throws ParseException
	 *             thrown by transformer method
	 */
	public static Wgs84Coordinate getWgs84Coordinate(Map<String, Object> tuple, int additional) throws ParseException {
		if (tuple.size() == 1 + additional) {
			Object oCoordinate = tuple.get("$0");
			return new Wgs84Coordinate(oCoordinate.toString());
		} else if (tuple.size() == 2 + additional) {
			Object oLatitude = tuple.get("$0");
			Object oLongitude = tuple.get("$1");
			Double latitude = LiteralUtils.asDouble(oLatitude);
			Double longitude = LiteralUtils.asDouble(oLongitude);
			return new Wgs84Coordinate(latitude, longitude);
		}
		throw new TMQLRuntimeException("Unexpected number of arguments: " + tuple.size());
	}

}
