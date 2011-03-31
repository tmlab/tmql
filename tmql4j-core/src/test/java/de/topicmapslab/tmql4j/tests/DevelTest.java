package de.topicmapslab.tmql4j.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.JTMQRFormat;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.JTMQRWriter;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

public class DevelTest {

	public static void main(String[] args) throws IOException {
		
		SimpleResultSet resultSet = new SimpleResultSet(null, null);

		List<String> list = new ArrayList<String>();

		for (int j = 0; j < 5; j++) {
			list.add(Integer.toString(j));
		}
		
		SimpleResult result = new SimpleResult(resultSet);
		result.add("String");
		resultSet.addResult(result);
		
		result = new SimpleResult(resultSet);
		result.add(list);
		resultSet.addResult(result);
		
		JTMQRWriter writer = new JTMQRWriter(System.out, JTMQRFormat.JTMQR_2);
		writer.write(resultSet);
		
	}
	
}
