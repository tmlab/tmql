package de.topicmapslab.tmql4j.tolog.core;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;

import de.topicmapslab.tmql4j.exception.TMQLConverterException;
import de.topicmapslab.tmql4j.tolog.components.lexer.TologLexer;
import de.topicmapslab.tmql4j.tolog.components.parser.TologParser;
import de.topicmapslab.tmql4j.tolog.core.internal.TologSnippetTree;
import de.topicmapslab.tmql4j.tolog.core.internal.TologUtility;

/**
 * Utility class to transform a tolog query to a TMQL query.
 * 
 * @author Sven Krosse
 * 
 */
public class TologConverter {

	private static final TologUtility tologUtility = new TologUtility();

	/**
	 * Transform the given string-representation of a tolog query to its corresponding TMQL query.
	 * 
	 * @param query
	 *            the query
	 * @return the TMQl-query
	 * @throws TMQLConverterException
	 *             thrown if transformation fails
	 */
	public static String convert(String query) throws TMQLConverterException {

		ANTLRStringStream antlrStringStream = new ANTLRStringStream(query);
		CommonTokenStream stream = new CommonTokenStream(new TologLexer(antlrStringStream));
		TologParser parser = new TologParser(stream);
		try {
			TologParser.query_return q = parser.query();
			TologSnippetTree tree = new TologSnippetTree((Tree) q.getTree(), tologUtility);
			// tree.print(System.out);
			return tree.getRoot().toTMQL();
		} catch (RecognitionException e) {
			throw new TMQLConverterException("Failed to parse tolog query.", e);
		} catch (Exception e) {
			throw new TMQLConverterException("Failed to parse tolog query.", e);
		}
	}

}
