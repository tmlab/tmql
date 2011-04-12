package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.tmapi.core.Locator;

import de.topicmapslab.jtm.writer.IJTMConstants;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model.JTMQRConstructReader;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model.LocatorStub;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.result.SimpleJtmqrResultSet;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v2.IJtmQr2Keys;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;

public class JTMQR2Reader {

	private final JsonParser jParser;

	protected JTMQR2Reader(JsonParser jParser) {
		this.jParser = jParser;
	}

	protected IResultSet<IResult> readResultSet() throws JsonParseException, IOException {

		SimpleJtmqrResultSet resultSet = new SimpleJtmqrResultSet();

		while (this.jParser.nextToken() != null) {

			JsonToken token = this.jParser.getCurrentToken();
			String text = this.jParser.getText();

			if (token.equals(JsonToken.FIELD_NAME)) {
				handleField(resultSet, text);
			}
		}

		return resultSet;
	}

	private void handleField(SimpleJtmqrResultSet resultSet, String text) throws JsonParseException, IOException {
		/*
		 * read header
		 */
		if (text.equals(IJtmQr2Keys.HEADERS)) {

			Map<String, Integer> header = JTMQRConstructReader.readHeader(jParser);
			Map<Integer, String> indexes = createIndexes(header);
			resultSet.setAlias(header);
			resultSet.setIndexes(indexes);
		}

		/*
		 * is result tuples
		 */
		else if (text.equals(IJtmQr2Keys.TUPLES)) {

			jParser.nextToken();

			while (jParser.nextToken() != null) {
				readTuple(jParser, resultSet);
			}
		}
	}

	private void readTuple(JsonParser jParser, SimpleJtmqrResultSet resultSet) throws JsonParseException, IOException {

		IResult result = resultSet.createResult();

		while (jParser.nextToken() != null) {
			JsonToken token = jParser.getCurrentToken();

			if (token.equals(JsonToken.END_ARRAY)) {
				resultSet.addResult(result);
				return;
			}

			if (token.equals(JsonToken.START_ARRAY)) {
				List<Object> list = readList(jParser);
				result.add((Object) list);
			} else if (token.equals(JsonToken.VALUE_STRING)) {
				result.add(jParser.getText());
			} else if (token.equals(JsonToken.VALUE_NUMBER_INT)) {
				result.add(BigInteger.valueOf(jParser.getLongValue()));
			} else if (token.equals(JsonToken.VALUE_NUMBER_FLOAT)) {
				result.add(BigDecimal.valueOf(jParser.getDoubleValue()));
			} else if (token.equals(JsonToken.VALUE_TRUE)) {
				result.add(true);
			} else if (token.equals(JsonToken.VALUE_FALSE)) {
				result.add(false);
			} else if (token.equals(JsonToken.START_OBJECT)) {
				result.add(readObject(jParser));
			}
		}
	}

	private Object readObject(JsonParser jParser) throws JsonParseException, IOException {

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.END_OBJECT)) {
				return null; // null object
			}

			if (text.equals("jtm")) {
				return readJTM(jParser);
			} else if (text.equals("ref")) {
				return readLocator(jParser);
			}

		}

		return null;
	}

	private Object readJTM(JsonParser jParser) throws JsonParseException, IOException {

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.VALUE_STRING)) {

				if (text.equals(IJTMConstants.TOPIC)) {
					return JTMQRConstructReader.readTopic(jParser);
				} else if (text.equals(IJTMConstants.NAME)) {
					return JTMQRConstructReader.readName(jParser, null);
				} else if (text.equals(IJTMConstants.OCCURRENCE)) {
					return JTMQRConstructReader.readOccurrence(jParser, null);
				} else if (text.equals(IJTMConstants.ASSOCIATION)) {
					return JTMQRConstructReader.readAssociation(jParser);
				} else if (text.equals(IJTMConstants.ROLE)) {
					return JTMQRConstructReader.readRole(jParser, null);
				} else if (text.equals(IJTMConstants.VARIANT)) {
					return JTMQRConstructReader.readVariant(jParser, null);
				}
			}
		}

		return null;

	}

	private Locator readLocator(JsonParser jParser) throws JsonParseException, IOException {

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.VALUE_STRING)) {
				return new LocatorStub(text);
			} else if (token.equals(JsonToken.END_OBJECT)) {
				return null;
			}
		}

		return null;
	}

	private List<Object> readList(JsonParser jParser) throws JsonParseException, IOException {

		List<Object> list = new ArrayList<Object>();

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();

			if (token.equals(JsonToken.END_ARRAY)) {
				return list;
			} else if (token.equals(JsonToken.VALUE_STRING)) {
				list.add(jParser.getText());
			} else if (token.equals(JsonToken.VALUE_NUMBER_INT)) {
				list.add(BigInteger.valueOf(jParser.getLongValue()));
			} else if (token.equals(JsonToken.VALUE_NUMBER_FLOAT)) {
				list.add(BigDecimal.valueOf(jParser.getDoubleValue()));
			} else if (token.equals(JsonToken.VALUE_TRUE)) {
				list.add(true);
			} else if (token.equals(JsonToken.VALUE_FALSE)) {
				list.add(false);
			} else if (token.equals(JsonToken.START_OBJECT)) {
				list.add(readObject(jParser));
			}
		}

		return list;

	}

	private Map<Integer, String> createIndexes(Map<String, Integer> aliases) {

		Map<Integer, String> indexes = new HashMap<Integer, String>();

		for (Map.Entry<String, Integer> alias : aliases.entrySet()) {
			indexes.put(alias.getValue(), alias.getKey());
		}

		return indexes;
	}

}
