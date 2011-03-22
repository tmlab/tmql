package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import de.topicmapslab.jtm.writer.IJTMConstants;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model.ConstructReader;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model.LocatorStub;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.result.SimpleJtmqrResultSet;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.IJtmQrKeys;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;

/**
 * class which reads a JTMQR result into a TMQL result set
 * 
 * @author Christian Haß
 * 
 */
public class JTMQRReader {

	private final JsonParser jParser;

	/**
	 * constructor
	 * 
	 * @param in
	 *            - the input stream
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public JTMQRReader(InputStream in) throws JsonParseException, IOException {
		JsonFactory jFactory = new JsonFactory();
		this.jParser = jFactory.createJsonParser(in);
	}

	/**
	 * @return the {@link IResultSet} filled with the information from the input
	 *         stream
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public IResultSet<IResult> readResultSet() throws JsonParseException, IOException {

		SimpleJtmqrResultSet resultSet = new SimpleJtmqrResultSet();
		IResult result = null;

		JsonToken lastToken = null;
		while (this.jParser.nextToken() != null) {

			JsonToken token = this.jParser.getCurrentToken();
			String text = this.jParser.getText();

			/*
			 * is name field
			 */
			if (token.equals(JsonToken.FIELD_NAME)) {
				result = handleField(resultSet, result, text);
			}
			/*
			 * check if is a null item '{}' in tuple mode
			 */
			else if (result != null && JsonToken.START_OBJECT.equals(lastToken) && token.equals(JsonToken.END_OBJECT)) {
				handleNullField(resultSet, result);
			}
			lastToken = token;
		}

		if (result != null) {
			resultSet.addResult(result);
		}

		return resultSet;
	}

	/**
	 * Method called to handle <code>null</code> values
	 * 
	 * @param resultSet
	 *            the result set
	 * @param result
	 *            the result
	 */
	protected void handleNullField(SimpleJtmqrResultSet resultSet, IResult result) {
		result.add((Object) null);
	}

	/**
	 * Internal method to handle a field of the JSON result
	 * 
	 * @param resultSet
	 *            the result set
	 * @param result
	 *            the result
	 * @param text
	 *            the text
	 * @return the modified result
	 * @throws JsonParseException
	 * @throws IOException
	 */
	protected IResult handleField(SimpleJtmqrResultSet resultSet, IResult result, String text) throws JsonParseException, IOException {
		if (text.equals(IJtmQrKeys.ALIASES)) {
			// read column labels
			Map<String, Integer> aliases = ConstructReader.readAliases(this.jParser);
			Map<Integer, String> indexes = createIndexes(aliases);

			resultSet.setAlias(aliases);
			resultSet.setIndexes(indexes);

		} else if (text.equals(IJtmQrKeys.TUPLE)) {
			// new token
			if (result != null) {
				resultSet.addResult(result);
			}
			result = resultSet.createResult();
		} else if (text.equals(IJtmQrKeys.STRING)) {

			// read string result
			this.jParser.nextToken();
			text = this.jParser.getText();
			result.add(text);

		} else if (text.equals(IJtmQrKeys.LOCATOR)) {

			// read string result
			this.jParser.nextToken();
			text = this.jParser.getText();
			result.add(new LocatorStub(text));

		} else if (text.equals(IJtmQrKeys.BOOLEAN)) {

			// read string result
			this.jParser.nextToken();
			result.add(this.jParser.getBooleanValue());

		} else if (text.equals(IJtmQrKeys.NUMBER)) {

			// read number result
			JsonToken token = this.jParser.nextToken();

			if (token.equals(JsonToken.VALUE_NUMBER_FLOAT)) {
				result.add(BigDecimal.valueOf(jParser.getDoubleValue()));
			} else if (token.equals(JsonToken.VALUE_NUMBER_INT)) {
				result.add(BigInteger.valueOf(jParser.getLongValue()));
			}

		} else if (text.equals(IJTMConstants.ITEM_TYPE)) {

			// read object result
			this.jParser.nextToken();
			text = this.jParser.getText();

			if (text.equals(IJTMConstants.TOPIC)) {
				result.add(ConstructReader.readTopic(this.jParser));
			} else if (text.equals(IJTMConstants.NAME)) {
				result.add(ConstructReader.readName(this.jParser, null));
			} else if (text.equals(IJTMConstants.OCCURRENCE)) {
				result.add(ConstructReader.readOccurrence(this.jParser, null));
			} else if (text.equals(IJTMConstants.ASSOCIATION)) {
				result.add(ConstructReader.readAssociation(this.jParser));
			} else if (text.equals(IJTMConstants.ROLE)) {
				result.add(ConstructReader.readRole(this.jParser, null));
			} else if (text.equals(IJTMConstants.VARIANT)) {
				result.add(ConstructReader.readVariant(this.jParser, null));
			}
		}
		return result;
	}

	/**
	 * creates an index from an alias
	 * 
	 * @param aliases
	 *            - the alias
	 * @return the index
	 */
	private Map<Integer, String> createIndexes(Map<String, Integer> aliases) {

		Map<Integer, String> indexes = new HashMap<Integer, String>();

		for (Map.Entry<String, Integer> alias : aliases.entrySet()) {
			indexes.put(alias.getValue(), alias.getKey());
		}

		return indexes;
	}

	/**
	 * Returns the parser instance
	 * 
	 * @return the parser
	 */
	protected JsonParser getParser() {
		return jParser;
	}

}
