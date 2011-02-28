package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import de.topicmapslab.IJTMConstants;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model.ConstructReader;
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
		boolean first = true;
		IResult result = resultSet.createResult();

		while (this.jParser.nextToken() != null) {

			JsonToken token = this.jParser.getCurrentToken();
			String text = this.jParser.getText();

			if (token.equals(JsonToken.FIELD_NAME)) {

				if (text.equals(IJtmQrKeys.ALIASES)) {

					// read column labels
					Map<String, Integer> aliases = ConstructReader.readAliases(this.jParser);
					Map<Integer, String> indexes = createIndexes(aliases);

					resultSet.setAlias(aliases);
					resultSet.setIndexes(indexes);

				} else if (text.equals(IJtmQrKeys.TUPLE)) {

					// new token

					if (first) {
						first = false;
					} else {
						resultSet.addResult(result);
						result = resultSet.createResult();
					}
				} else if (text.equals(IJtmQrKeys.STRING)) {

					// read string result
					token = this.jParser.nextToken();
					text = this.jParser.getText();
					result.add(text);

				} else if (text.equals(IJtmQrKeys.NUMBER)) {

					// read number result
					token = this.jParser.nextToken();
					text = this.jParser.getText();

					if (token.equals(JsonToken.VALUE_NUMBER_FLOAT)) {
						result.add(Double.parseDouble(text));
					} else if (token.equals(JsonToken.VALUE_NUMBER_INT)) {
						result.add(Integer.parseInt(text));
					}

				} else if (text.equals(IJTMConstants.ITEM_TYPE)) {

					// read object result
					token = this.jParser.nextToken();
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
			}
		}

		if (!first) {
			resultSet.addResult(result);
		}

		return resultSet;
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

}
