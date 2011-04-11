/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.tests.flatstring;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.junit.Test;

import de.topicmapslab.tmql4j.components.parser.IParserTree;
import de.topicmapslab.tmql4j.path.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class FlatStrings extends Tmql4JTestCase {

	@Test
	public void testPredicate() {
		_check("input ( input : input )", "input ( input : input )");
		_check("input ( input : input, other : other )", "input ( input : input , other : other )");
		_check("input ( input : input, other : other,...)", "input ( input : input , other : other , ... )");
	}

	@Test
	public void testNCLAxis() {
		_check("\"input\"~", "\"input\" << indicators");
		_check("\"input\"=", "\"input\" << locators");
		_check("\"input\"!", "\"input\" << item");
		_check("input / argument", "input >> characteristics argument >> atomify");
		_check("\"input\" \\ argument", "\"input\" << atomify << characteristics argument");
		_check("input ~~>", "input >> reifier");
		_check("input <~~", "input << reifier");
		_check("// input", "input << types");
		_check("input -> argument", "input >> players argument");
		_check("input <- argument", "input << players argument");
		_check("input <-> argument", "input >> traverse argument");
	}

	@Test
	public void testBoolean() {
		_check("a >> types [ b AND c ]", "a >> types [ EXISTS b AND EXISTS c ]");
		_check("a >> types [ b OR c ]", "a >> types [ EXISTS b OR EXISTS c ]");
		_check("a >> types [ NOT c ]", "a >> types [ NOT EXISTS c ]");
		_check("a >> types [ EXISTS c ]", "a >> types [ EXISTS c ]");
		_check("a >> types [ AT MOST 4 $c IN alle SATISFIES c ]", "a >> types [ AT MOST 4 $c IN alle SATISFIES EXISTS c ]");
		_check("a >> types [ AT LEAST 4 $c IN alle SATISFIES c ]", "a >> types [ AT LEAST 4 $c IN alle SATISFIES EXISTS c ]");
		_check("a >> types [ SOME $c IN alle SATISFIES c ]", "a >> types [ SOME $c IN alle SATISFIES EXISTS c ]");
		_check("a >> types [ EVERY $c IN alle SATISFIES c ]", "a >> types [ EVERY $c IN alle SATISFIES EXISTS c ]");
		_check("a >> types [ c == 0 ]", "a >> types [ EXISTS c == 0 ]");
		_check("a >> types [ c <= 0 ]", "a >> types [ EXISTS c <= 0 ]");
		_check("a >> types [ c >= 0 ]", "a >> types [ EXISTS c >= 0 ]");
		_check("a >> types [ c < 0 ]", "a >> types [ EXISTS c < 0 ]");
		_check("a >> types [ c > 0 ]", "a >> types [ EXISTS c > 0 ]");
		_check("a >> types [ c =~ 0 ]", "a >> types [ EXISTS c =~ 0 ]");
	}

	@Test
	public void testContent() {
		_check("a+b", "a + b");
		_check("a-b", "a - b");
		_check("a*b", "a * b");
		_check("a % b", "a % b");
		_check("a mod b", "a mod b");
		_check("-b", "- b");
		_check("b UNION b", "b UNION b");
		_check("b MINUS b", "b MINUS b");
		_check("b INTERSECT b", "b INTERSECT b");
	}

	@Test
	public void testFilter() {
		_check("input << typed @argument ", "input << typed [ . >> scope == argument ]");
		_check("input << typed [@argument] ", "input << typed [ . >> scope == argument ]");
		_check("input << typed [^argument] ", "input << typed [ . >> types == argument ]");
		_check("input << typed // argument ", "input << typed [ . >> types == argument ]");
		_check("input << typed [0] ", "input << typed [ $# == 0 ]");
		_check("input << typed [0..2] ", "input << typed [ 0 <= $# AND $# < 2 ]");
	}

	@Test
	public void testProjection() {
		_check("myTopic ( fn:has-variant( . >> characteristics tm:name, \"xsd:any\"))", "myTopic ( fn:has-variant ( . >> characteristics tm:name , \"xsd:any\" ) )");
		_check("( fn:has-variant( . >> characteristics tm:name, \"xsd:any\"))", "fn:has-variant ( . >> characteristics tm:name , \"xsd:any\" )");
		_check("( fn:has-variant( . >> characteristics tm:name, \"xsd:any\") AS \"fct\")", "fn:has-variant ( . >> characteristics tm:name , \"xsd:any\" ) AS \"fct\"");
	}

	public void _check(String input, String output) {
		IParserTree tree = runtime.parse(input);
		assertNotNull(tree);
		String queryString = tree.toQueryString();
		assertEquals(output, queryString);
	}

}
