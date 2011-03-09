/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.flwr.tests;

import static junit.framework.Assert.assertEquals;
import de.topicmapslab.tmql4j.components.lexer.TMQLLexer;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.For;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.Return;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.XmlEndTag;
import de.topicmapslab.tmql4j.flwr.grammar.lexical.XmlStartTag;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketAngleClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketAngleOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.path.grammar.lexical.Element;
import de.topicmapslab.tmql4j.path.grammar.lexical.In;
import de.topicmapslab.tmql4j.path.grammar.lexical.Isa;
import de.topicmapslab.tmql4j.path.grammar.lexical.Literal;
import de.topicmapslab.tmql4j.path.grammar.lexical.Minus;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisAtomifyMoveForward;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisInstances;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisTypes;
import de.topicmapslab.tmql4j.path.grammar.lexical.TripleQuote;
import de.topicmapslab.tmql4j.path.grammar.lexical.Variable;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;


/**
 * @author Sven Krosse
 *
 */
public class TestLexicalScannerForCTM extends Tmql4JTestCase {

	@org.junit.Test
	public void testLexicalScanner() throws Exception {
		TMQLQuery query = new TMQLQuery(topicMap, "RETURN '''<http://test(it)> isa { $0 }; <http://psi.de#da>.'''");
		query.beforeQuery(runtime);
		TMQLLexer lexer = new TMQLLexer(runtime, query);
		lexer.execute();
		assertEquals(11, lexer.getTmqlTokens().size());
		assertEquals(Return.class, lexer.getTmqlTokens().get(0));
		assertEquals(Return.TOKEN, lexer.getTokens().get(0));
		
		assertEquals(TripleQuote.class, lexer.getTmqlTokens().get(1));
		assertEquals("'''", lexer.getTokens().get(1));
		
		assertEquals(Element.class, lexer.getTmqlTokens().get(2));
		assertEquals("<http://test(it)>", lexer.getTokens().get(2));
		
		assertEquals(Isa.class, lexer.getTmqlTokens().get(3));
		assertEquals("isa", lexer.getTokens().get(3));
		
		assertEquals(BracketAngleOpen.class, lexer.getTmqlTokens().get(4));
		assertEquals("{", lexer.getTokens().get(4));
		
		assertEquals(Variable.class, lexer.getTmqlTokens().get(5));
		assertEquals("$0", lexer.getTokens().get(5));
		
		assertEquals(BracketAngleClose.class, lexer.getTmqlTokens().get(6));
		assertEquals("}", lexer.getTokens().get(6));
		
		assertEquals(Element.class, lexer.getTmqlTokens().get(7));
		assertEquals(";", lexer.getTokens().get(7));
		
		assertEquals(Element.class, lexer.getTmqlTokens().get(8));
		assertEquals("<http://psi.de#da>", lexer.getTokens().get(8));
		
		assertEquals(Dot.class, lexer.getTmqlTokens().get(9));
		assertEquals(".", lexer.getTokens().get(9));
		
		assertEquals(TripleQuote.class, lexer.getTmqlTokens().get(10));
		assertEquals("'''", lexer.getTokens().get(10));
	}
	
	@org.junit.Test
	public void testLexicalScanner2() throws Exception {
		TMQLQuery query = new TMQLQuery(topicMap, "RETURN '''<http://en.wikipedia.org/wiki/Munich_(district)> - \"some name\" . '''");
		query.beforeQuery(runtime);
		TMQLLexer lexer = new TMQLLexer(runtime, query);
		lexer.execute();
		assertEquals(7, lexer.getTmqlTokens().size());
		assertEquals(Return.class, lexer.getTmqlTokens().get(0));
		assertEquals(Return.TOKEN, lexer.getTokens().get(0));
		
		assertEquals(TripleQuote.class, lexer.getTmqlTokens().get(1));
		assertEquals("'''", lexer.getTokens().get(1));
		
		assertEquals(Element.class, lexer.getTmqlTokens().get(2));
		assertEquals("<http://en.wikipedia.org/wiki/Munich_(district)>", lexer.getTokens().get(2));
		
		assertEquals(Minus.class, lexer.getTmqlTokens().get(3));
		assertEquals(Minus.TOKEN, lexer.getTokens().get(3));
		
		assertEquals(Literal.class, lexer.getTmqlTokens().get(4));
		assertEquals("\"some name\"", lexer.getTokens().get(4));
		
		assertEquals(Dot.class, lexer.getTmqlTokens().get(5));
		assertEquals(".", lexer.getTokens().get(5));
		
		assertEquals(TripleQuote.class, lexer.getTmqlTokens().get(6));
		assertEquals("'''", lexer.getTokens().get(6));
	}
	
	@org.junit.Test
	public void testLexicalScanner3() throws Exception {
		TMQLQuery query = new TMQLQuery(topicMap, "RETURN '''<http://maiana.topicmapslab.de/u/peter/tm/archiv-ostpreussen/#haus> - \"some name\" . '''");
		query.beforeQuery(runtime);
		TMQLLexer lexer = new TMQLLexer(runtime, query);
		lexer.execute();
		assertEquals(7, lexer.getTmqlTokens().size());
		assertEquals(Return.class, lexer.getTmqlTokens().get(0));
		assertEquals(Return.TOKEN, lexer.getTokens().get(0));
		
		assertEquals(TripleQuote.class, lexer.getTmqlTokens().get(1));
		assertEquals("'''", lexer.getTokens().get(1));
		
		assertEquals(Element.class, lexer.getTmqlTokens().get(2));
		assertEquals("<http://maiana.topicmapslab.de/u/peter/tm/archiv-ostpreussen/#haus>", lexer.getTokens().get(2));
		
		assertEquals(Minus.class, lexer.getTmqlTokens().get(3));
		assertEquals(Minus.TOKEN, lexer.getTokens().get(3));
		
		assertEquals(Literal.class, lexer.getTmqlTokens().get(4));
		assertEquals("\"some name\"", lexer.getTokens().get(4));
		
		assertEquals(Dot.class, lexer.getTmqlTokens().get(5));
		assertEquals(".", lexer.getTokens().get(5));
		
		assertEquals(TripleQuote.class, lexer.getTmqlTokens().get(6));
		assertEquals("'''", lexer.getTokens().get(6));
	}
	
	@org.junit.Test
	public void testLexicalScannerFroXML() throws Exception {
		TMQLQuery query = new TMQLQuery(topicMap, "RETURN <letters>{FOR $l IN // tml:letter RETURN <letter>{$l / tm:name}</letter>}</letters>");
		query.beforeQuery(runtime);
		TMQLLexer lexer = new TMQLLexer(runtime, query);
		lexer.execute();
		assertEquals(18, lexer.getTmqlTokens().size());
		int i = 0;
		assertEquals(Return.class, lexer.getTmqlTokens().get(i));
		assertEquals(Return.TOKEN, lexer.getTokens().get(i));
		i++;
		
		assertEquals(XmlStartTag.class, lexer.getTmqlTokens().get(i));
		assertEquals("<letters>", lexer.getTokens().get(i));
		i++;
		
		assertEquals(BracketAngleOpen.class, lexer.getTmqlTokens().get(i));
		assertEquals("{", lexer.getTokens().get(i));
		i++;
		
		assertEquals(For.class, lexer.getTmqlTokens().get(i));
		assertEquals(For.TOKEN, lexer.getTokens().get(i));
		i++;
		
		assertEquals(Variable.class, lexer.getTmqlTokens().get(i));
		assertEquals("$l", lexer.getTokens().get(i));
		i++;
		
		assertEquals(In.class, lexer.getTmqlTokens().get(i));
		assertEquals(In.TOKEN, lexer.getTokens().get(i));
		i++;
		
		assertEquals(ShortcutAxisInstances.class, lexer.getTmqlTokens().get(i));
		assertEquals("//", lexer.getTokens().get(i));
		i++;
		
		assertEquals(Element.class, lexer.getTmqlTokens().get(i));
		assertEquals("tml:letter", lexer.getTokens().get(i));
		i++;
		
		assertEquals(Return.class, lexer.getTmqlTokens().get(i));
		assertEquals(Return.TOKEN, lexer.getTokens().get(i));
		i++;
		
		assertEquals(XmlStartTag.class, lexer.getTmqlTokens().get(i));
		assertEquals("<letter>", lexer.getTokens().get(i));
		i++;
		
		assertEquals(BracketAngleOpen.class, lexer.getTmqlTokens().get(i));
		assertEquals("{", lexer.getTokens().get(i));
		i++;
		
		assertEquals(Variable.class, lexer.getTmqlTokens().get(i));
		assertEquals("$l", lexer.getTokens().get(i));
		i++;
		
		assertEquals(ShortcutAxisAtomifyMoveForward.class, lexer.getTmqlTokens().get(i));
		assertEquals("/", lexer.getTokens().get(i));
		i++;
		
		assertEquals(Element.class, lexer.getTmqlTokens().get(i));
		assertEquals("tm:name", lexer.getTokens().get(i));
		i++;
		
		assertEquals(BracketAngleClose.class, lexer.getTmqlTokens().get(i));
		assertEquals("}", lexer.getTokens().get(i));
		i++;
		
		assertEquals(XmlEndTag.class, lexer.getTmqlTokens().get(i));
		assertEquals("</letter>", lexer.getTokens().get(i));
		i++;
		
		assertEquals(BracketAngleClose.class, lexer.getTmqlTokens().get(i));
		assertEquals("}", lexer.getTokens().get(i));
		i++;
		
		assertEquals(XmlEndTag.class, lexer.getTmqlTokens().get(i));
		assertEquals("</letters>", lexer.getTokens().get(i));
		i++;
	}
	
}
