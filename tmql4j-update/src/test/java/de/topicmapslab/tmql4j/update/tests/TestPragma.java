/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import org.junit.Test;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.majortom.model.namespace.Namespaces;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.update.grammar.pragma.DatatypeBindingPragma;
import de.topicmapslab.tmql4j.update.grammar.pragma.DatatypeValidationPragma;

/**
 * @author Sven Krosse
 * 
 */
public class TestPragma extends Tmql4JTestCase {

	@Test
	public void textUpdateOccurrencePragmas() throws Exception {
		Locator xsdString = topicMap.createLocator(Namespaces.XSD.STRING);
		Locator xsdInt = topicMap.createLocator(Namespaces.XSD.INT);
		Locator xsdFloat = topicMap.createLocator(Namespaces.XSD.FLOAT);
		Locator xsdAnyUri = topicMap.createLocator(Namespaces.XSD.ANYURI);

		Topic t = createTopic();
		Occurrence occ = t.createOccurrence(t, "text");
		assertEquals(xsdString, occ.getDatatype());
		assertEquals("text", occ.getValue());

		String query = "";

		/*
		 * for integer
		 */
		occ.setValue(1);
		assertEquals(xsdInt, occ.getDatatype());
		assertEquals("1", occ.getValue());

		query = "UPDATE occurrences SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			fail("Value should be validate!");
		} catch (TMQLRuntimeException e) {
			// VOID
		}

		query = "%pragma " + DatatypeValidationPragma.IDENTIFIER + " false UPDATE occurrences SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals("abcd", occ.getValue());
			assertEquals(xsdInt, occ.getDatatype());
		} catch (TMQLRuntimeException e) {
			fail("Value should not validate!");
		}

		query = "%pragma " + DatatypeBindingPragma.IDENTIFIER + " false UPDATE occurrences SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals("abcd", occ.getValue());
			assertEquals(xsdString, occ.getDatatype());
		} catch (TMQLRuntimeException e) {
			fail("Datatype should not set to string!");
		}

		/*
		 * for floating point number values
		 */
		occ.setValue(123f);
		assertEquals(xsdFloat, occ.getDatatype());
		assertEquals(123f, occ.floatValue());

		query = "UPDATE occurrences SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			fail("Value should be validate!");
		} catch (TMQLRuntimeException e) {
			// VOID
		}

		query = "%pragma " + DatatypeValidationPragma.IDENTIFIER + " false UPDATE occurrences SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals(xsdFloat, occ.getDatatype());
			assertEquals("abcd", occ.getValue());
		} catch (TMQLRuntimeException e) {
			fail("Value should not validate!");
		}

		query = "%pragma " + DatatypeBindingPragma.IDENTIFIER + " false UPDATE occurrences SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals("abcd", occ.getValue());
			assertEquals(xsdString, occ.getDatatype());
		} catch (TMQLRuntimeException e) {
			fail("Datatype should not set to string!");
		}

		/*
		 * for IRI values
		 */
		Locator loc = topicMap.createLocator("http://psi.example.org");
		occ.setValue(loc);
		assertEquals(xsdAnyUri, occ.getDatatype());
		assertEquals(loc, occ.locatorValue());

		query = "UPDATE occurrences SET \"123:123:123\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			fail("Value should be validate!");
		} catch (TMQLRuntimeException e) {
			// VOID
		}

		query = "%pragma " + DatatypeValidationPragma.IDENTIFIER + " false UPDATE occurrences SET \"123:123:123\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals(xsdAnyUri, occ.getDatatype());
			assertEquals("123:123:123", occ.getValue());
		} catch (TMQLRuntimeException e) {
			fail("Value should not validate!");
		}

		query = "%pragma " + DatatypeBindingPragma.IDENTIFIER + " false UPDATE occurrences SET \"123:123:123\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals("123:123:123", occ.getValue());
			assertEquals(xsdString, occ.getDatatype());
		} catch (TMQLRuntimeException e) {
			fail("Datatype should not set to string!");
		}

	}

	@Test
	public void textUpdateVariantPragmas() throws Exception {
		Locator xsdString = topicMap.createLocator(Namespaces.XSD.STRING);
		Locator xsdInt = topicMap.createLocator(Namespaces.XSD.INT);
		Locator xsdFloat = topicMap.createLocator(Namespaces.XSD.FLOAT);
		Locator xsdAnyUri = topicMap.createLocator(Namespaces.XSD.ANYURI);

		Topic t = createTopic();
		Name n = t.createName("Value");
		Variant occ = n.createVariant("text", createTopic());
		assertEquals(xsdString, occ.getDatatype());
		assertEquals("text", occ.getValue());

		String query = "";

		/*
		 * for integer
		 */
		occ.setValue(1);
		assertEquals(xsdInt, occ.getDatatype());
		assertEquals("1", occ.getValue());

		query = "UPDATE variants SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			fail("Value should be validate!");
		} catch (TMQLRuntimeException e) {
			// VOID
		}

		query = "%pragma " + DatatypeValidationPragma.IDENTIFIER + " false UPDATE variants SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals("abcd", occ.getValue());
			assertEquals(xsdInt, occ.getDatatype());
		} catch (TMQLRuntimeException e) {
			fail("Value should not validate!");
		}

		query = "%pragma " + DatatypeBindingPragma.IDENTIFIER + " false UPDATE variants SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals("abcd", occ.getValue());
			assertEquals(xsdString, occ.getDatatype());
		} catch (TMQLRuntimeException e) {
			fail("Datatype should not set to string!");
		}

		/*
		 * for floating point number values
		 */
		occ.setValue(123f);
		assertEquals(xsdFloat, occ.getDatatype());
		assertEquals(123f, occ.floatValue());

		query = "UPDATE variants SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			fail("Value should be validate!");
		} catch (TMQLRuntimeException e) {
			// VOID
		}

		query = "%pragma " + DatatypeValidationPragma.IDENTIFIER + " false UPDATE variants SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals(xsdFloat, occ.getDatatype());
			assertEquals("abcd", occ.getValue());
		} catch (TMQLRuntimeException e) {
			fail("Value should not validate!");
		}

		query = "%pragma " + DatatypeBindingPragma.IDENTIFIER + " false UPDATE variants SET \"abcd\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals("abcd", occ.getValue());
			assertEquals(xsdString, occ.getDatatype());
		} catch (TMQLRuntimeException e) {
			fail("Datatype should not set to string!");
		}

		/*
		 * for IRI values
		 */
		Locator loc = topicMap.createLocator("http://psi.example.org");
		occ.setValue(loc);
		assertEquals(xsdAnyUri, occ.getDatatype());
		assertEquals(loc, occ.locatorValue());

		query = "UPDATE variants SET \"123:123:123\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			fail("Value should be validate!");
		} catch (TMQLRuntimeException e) {
			// VOID
		}

		query = "%pragma " + DatatypeValidationPragma.IDENTIFIER + " false UPDATE variants SET \"123:123:123\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals(xsdAnyUri, occ.getDatatype());
			assertEquals("123:123:123", occ.getValue());
		} catch (TMQLRuntimeException e) {
			fail("Value should not validate!");
		}

		query = "%pragma " + DatatypeBindingPragma.IDENTIFIER + " false UPDATE variants SET \"123:123:123\" WHERE \"" + occ.getId() + "\" << id";
		try {
			execute(query);
			assertEquals("123:123:123", occ.getValue());
			assertEquals(xsdString, occ.getDatatype());
		} catch (TMQLRuntimeException e) {
			fail("Datatype should not set to string!");
		}

	}
}
