/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests.jtmqr.v1;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;
import static junit.framework.Assert.*;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v1.IJtmQrKeys;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * @author Sven Krosse
 *
 */
public class TestMetaDataContent extends with_JTMQRWriter{

	@Test
	public void testColumns() throws Exception{
		JsonNode node = writeAndRead(buildResult());
		assertTrue(node.has(IJtmQrKeys.METADATA));
		JsonNode md = node.get(IJtmQrKeys.METADATA);
		assertTrue(md.has(IJtmQrKeys.COLUMNS));
		assertEquals(3, md.get(IJtmQrKeys.COLUMNS).getLongValue());
	}
	
	@Test
	public void testRows() throws Exception{
		JsonNode node = writeAndRead(buildResult());
		assertTrue(node.has(IJtmQrKeys.METADATA));
		JsonNode md = node.get(IJtmQrKeys.METADATA);
		assertTrue(md.has(IJtmQrKeys.ROWS));
		assertEquals(4, md.get(IJtmQrKeys.ROWS).getLongValue());
	}
	
	@Test
	public void testAlias() throws Exception{
		JsonNode node = writeAndRead(buildResult());
		assertTrue(node.has(IJtmQrKeys.METADATA));
		JsonNode md = node.get(IJtmQrKeys.METADATA);
		assertTrue(md.has(IJtmQrKeys.ALIASES));
		JsonNode as = md.get(IJtmQrKeys.ALIASES);
		assertTrue(as.has("0"));
		assertEquals("new", as.get("0").getValueAsText());
		assertTrue(as.has("1"));
		assertEquals("col", as.get("1").getValueAsText());
		assertTrue(as.has("2"));
		assertTrue(as.get("2").isNull());
	}
	
	public SimpleResultSet buildResult() {
		ResultSet set = new ResultSet();
		SimpleResult r = new SimpleResult(set);
		r.add("1");
		r.add("A");
		r.add("%");
		set.addResult(r);
		r = new SimpleResult(set);
		r.add("2");
		r.add("B");
		r.add("*");
		set.addResult(r);
		r = new SimpleResult(set);
		r.add("3");
		r.add("C");
		r.add("-");
		set.addResult(r);
		r = new SimpleResult(set);
		r.add("4");
		r.add("D");
		r.add("+");
		set.addResult(r);
		return set;
	}
	
	class ResultSet extends SimpleResultSet{

		Map<Integer, String> alias = new HashMap<Integer, String>();
		
		public ResultSet() {
			super(null, null);
			alias.put(0, "new");
			alias.put(1, "col");			
		}
		
		/**
		 * {@inheritDoc}
		 */
		public String getAlias(int index) {			
			return alias.get(index);
		}
		
		/**
		 * {@inheritDoc}
		 */
		public boolean hasAlias() {
			return true;
		}
		
	}
	
	
}

