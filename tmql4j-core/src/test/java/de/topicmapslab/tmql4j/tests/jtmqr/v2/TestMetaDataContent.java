/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.tests.jtmqr.v2;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v2.IJtmQr2Keys;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;


public class TestMetaDataContent extends with_JTMQR2Writer{

	@Test
	public void testColumns() throws Exception{
		JsonNode node = writeAndRead(buildResult());
		assertTrue(node.has(IJtmQr2Keys.METADATA));
		JsonNode md = node.get(IJtmQr2Keys.METADATA);
		assertTrue(md.has(IJtmQr2Keys.COLUMNS));
		assertEquals(3, md.get(IJtmQr2Keys.COLUMNS).getLongValue());
	}
	
	@Test
	public void testRows() throws Exception{
		JsonNode node = writeAndRead(buildResult());
		assertTrue(node.has(IJtmQr2Keys.METADATA));
		JsonNode md = node.get(IJtmQr2Keys.METADATA);
		assertTrue(md.has(IJtmQr2Keys.ROWS));
		assertEquals(4, md.get(IJtmQr2Keys.ROWS).getLongValue());
	}
	
	@Test
	public void testHeader() throws Exception{
		JsonNode node = writeAndRead(buildResult());
		
		System.out.println(node.toString());
		
		assertTrue(node.has(IJtmQr2Keys.METADATA));
		JsonNode md = node.get(IJtmQr2Keys.METADATA);
		assertTrue(md.has(IJtmQr2Keys.HEADERS));
		JsonNode as = md.get(IJtmQr2Keys.HEADERS);
		assertEquals("new", as.get(0).getValueAsText());
		assertEquals("col", as.get(1).getValueAsText());
		assertTrue(as.get(2).isNull());
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

