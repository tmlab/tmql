package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import de.topicmapslab.jtm.writer.IJTMConstants;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model.ConstructReader;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model.LocatorStub;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.result.SimpleJtmqrResultSet;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.writer.v1.IJtmQrKeys;
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
		/*
		 * is alias node
		 */
		if (text.equals(IJtmQrKeys.ALIASES)) {
			/*
			 * read aliases
			 */
			Map<String, Integer> aliases = ConstructReader.readAliases(this.jParser);
			Map<Integer, String> indexes = createIndexes(aliases);
			resultSet.setAlias(aliases);
			resultSet.setIndexes(indexes);
		}
		/*
		 * is result tuple
		 */
		else if (text.equals(IJtmQrKeys.TUPLE)) {
			/*
			 * is not first tuple
			 */
			if (result != null) {
				resultSet.addResult(result);
			}
			result = resultSet.createResult();
		}
		/*
		 * is tuple content
		 */
		else {
			Object value = getValueFromField(text);
			if (value != null) {
				result.add(value);
			}
		}
		return result;
	}

	/**
	 * Internal method to get the value for the field
	 * 
	 * @param text
	 *            the field name
	 * @return the value of field and never <code>null</code>
	 * @throws JsonParseException
	 *             thrown if parsing process fails
	 * @throws IOException
	 *             thrown if any I/O error occur
	 */
	protected Object getValueFromField(String text) throws JsonParseException, IOException {
		/*
		 * is string value
		 */
		if (text.equals(IJtmQrKeys.STRING)) {
			this.jParser.nextToken();
			return this.jParser.getText();
		}
		/*
		 * is locator value
		 */
		else if (text.equals(IJtmQrKeys.LOCATOR)) {
			this.jParser.nextToken();
			text = this.jParser.getText();
			return new LocatorStub(text);
		}
		/*
		 * is boolean value
		 */
		else if (text.equals(IJtmQrKeys.BOOLEAN)) {
			this.jParser.nextToken();
			return this.jParser.getBooleanValue();
		}
		/*
		 * is numeric value
		 */
		else if (text.equals(IJtmQrKeys.NUMBER)) {
			JsonToken token = this.jParser.nextToken();
			/*
			 * is floating point number
			 */
			if (token.equals(JsonToken.VALUE_NUMBER_FLOAT)) {
				return BigDecimal.valueOf(jParser.getDoubleValue());
			}
			/*
			 * is integer number
			 */
			else if (token.equals(JsonToken.VALUE_NUMBER_INT)) {
				return BigInteger.valueOf(jParser.getLongValue());
			}
		}
		/*
		 * is construct value
		 */
		else if (text.equals(IJTMConstants.ITEM_TYPE)) {
			this.jParser.nextToken();
			text = this.jParser.getText();
			/*
			 * is topic
			 */
			if (text.equals(IJTMConstants.TOPIC)) {
				return ConstructReader.readTopic(this.jParser);
			}
			/*
			 * is name
			 */
			else if (text.equals(IJTMConstants.NAME)) {
				return ConstructReader.readName(this.jParser, null);
			}
			/*
			 * is occurrence
			 */
			else if (text.equals(IJTMConstants.OCCURRENCE)) {
				return ConstructReader.readOccurrence(this.jParser, null);
			}
			/*
			 * is association
			 */
			else if (text.equals(IJTMConstants.ASSOCIATION)) {
				return ConstructReader.readAssociation(this.jParser);
			}
			/*
			 * is role
			 */
			else if (text.equals(IJTMConstants.ROLE)) {
				return ConstructReader.readRole(this.jParser, null);
			}
			/*
			 * is variant
			 */
			else if (text.equals(IJTMConstants.VARIANT)) {
				return ConstructReader.readVariant(this.jParser, null);
			}
		}
		/*
		 * is array of values
		 */
		else if (text.equals(IJtmQrKeys.ARRAY)) {
			List<Object> list = new ArrayList<Object>();
			handleArray(list);
			return list;
		}
		return null;
	}

	/**
	 * Internal method to handle a field array value of the JSON result
	 * 
	 * @param resultSet
	 *            the result set
	 * @param list
	 *            the list to add value
	 * @param text
	 *            the text
	 * @throws JsonParseException
	 * @throws IOException
	 */
	protected void handleArray(List<Object> list) throws JsonParseException, IOException {
		/*
		 * iterate over next tokens
		 */
		while (this.jParser.nextToken() != null) {
			/*
			 * get current token
			 */
			JsonToken token = this.jParser.getCurrentToken();
			/*
			 * is end of array
			 */
			if (JsonToken.END_ARRAY.equals(token)) {
				break;
			}
			/*
			 * is field name
			 */
			else if (JsonToken.FIELD_NAME.equals(token)) {
				/*
				 * get field value
				 */
				Object value = getValueFromField(this.jParser.getText());
				/*
				 * add to list
				 */
				if (value != null) {
					list.add(value);
				}
			}
		}
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
