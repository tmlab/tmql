package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.jtm.writer.IJTMConstants;

public class JTMQRConstructReader {

	
	private enum State {
		OBJECT, ARRAY
	}

	
	/**
	 * reads a topic construct
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @return the topic construct
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Topic readTopic(JsonParser jParser) throws JsonParseException, IOException {

		Stack<State> stack = new Stack<JTMQRConstructReader.State>();
		TopicStub topic = new TopicStub();

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.END_OBJECT)) {

				if (stack.isEmpty()) {
					return topic;
				}

				stack.pop();

			} else if (token.equals(JsonToken.START_OBJECT)) {
				stack.push(State.OBJECT);
			} else if (token.equals(JsonToken.START_ARRAY)) {
				stack.push(State.ARRAY);
			} else if (token.equals(JsonToken.END_ARRAY)) {
				stack.pop();

			} else if (token.equals(JsonToken.FIELD_NAME)) {

				if (text.equals(IJTMConstants.NAMES)) {
					topic._setNames(readNames(jParser, topic));
				} else if (text.equals(IJTMConstants.OCCURRENCES)) {
					topic._setOccurrences(readOccurrences(jParser, topic));
				} else if (text.equals(IJTMConstants.SUBJECT_IDENTIFIERS)) {
					topic._setSubjectIdentifiers(readIdentifier(jParser));
				} else if (text.equals(IJTMConstants.SUBJECT_LOCATORS)) {
					topic._setSubjectLocators(readIdentifier(jParser));
				} else if (text.equals(IJTMConstants.ITEM_IDENTIFIERS)) {
					topic._setItemIdentifiers(readIdentifier(jParser));
				} else if (text.equals(IJTMConstants.ID)) {
					topic._setId(readId(jParser));
				} else if (text.equals(IJTMConstants.INSTANCE_OF)) {
					topic._setTypes(readTypes(jParser));
				}
			}
		}

		return topic;
	}

	/**
	 * reads a set of name construct
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @param parent
	 *            - the parent topic, can be <code>null</code>
	 * @return set of name constructs
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private static Set<Name> readNames(JsonParser jParser, Topic parent) throws JsonParseException, IOException {

		Stack<State> stack = new Stack<JTMQRConstructReader.State>();
		Set<Name> result = new HashSet<Name>();

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();

			if (token.equals(JsonToken.START_OBJECT)) {
				Name name = readName(jParser, parent);
				result.add(name);
			} else if (token.equals(JsonToken.START_ARRAY)) {
				stack.push(State.ARRAY);
			} else if (token.equals(JsonToken.END_ARRAY)) {
				stack.pop();

				if (stack.isEmpty()) {
					return result;
				}
			}
		}

		return result;
	}

	/**
	 * reads all types
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @return the topics
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Set<Topic> readTypes(JsonParser jParser) throws JsonParseException, IOException {
		Set<Topic> topics = new HashSet<Topic>();

		while (jParser.nextToken() != null) {
			JsonToken token = jParser.getCurrentToken();
			if (token.equals(JsonToken.VALUE_STRING)) {
				topics.add(readTopicReference(jParser, false));
			} else if (token.equals(JsonToken.END_ARRAY)) {
				break;
			}
		}
		return topics;
	}

	/**
	 * reads a single name construct
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @param parent
	 *            - the parent topic, can be <code>null</code>
	 * @return the name construct
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Name readName(JsonParser jParser, Topic parent) throws JsonParseException, IOException {

		Stack<State> stack = new Stack<JTMQRConstructReader.State>();
		NameStub name = new NameStub(parent);

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.END_OBJECT)) {

				if (stack.isEmpty()) {
					return name;
				}

				stack.pop();

			} else if (token.equals(JsonToken.START_OBJECT)) {
				stack.push(State.OBJECT);
			} else if (token.equals(JsonToken.START_ARRAY)) {
				stack.push(State.ARRAY);
			} else if (token.equals(JsonToken.END_ARRAY)) {
				stack.pop();

			} else if (token.equals(JsonToken.FIELD_NAME)) {

				if (text.equals(IJTMConstants.VALUE)) {
					name._setValue(readValue(jParser));
				} else if (text.equals(IJTMConstants.TYPE)) {
					name._setType(readTopicReference(jParser));
				} else if (text.equals(IJTMConstants.SCOPE)) {
					name._setScope(readScope(jParser));
				} else if (text.equals(IJTMConstants.VARIANTS)) {
					name.setVariants(readVariants(jParser, name));
				} else if (text.equals(IJTMConstants.REIFIER)) {
					name._setReifier(readTopicReference(jParser));
				} else if (text.equals(IJTMConstants.ITEM_IDENTIFIERS)) {
					name._setItemIdentifiers(readIdentifier(jParser));
				} else if (text.equals(IJTMConstants.ID)) {
					name._setId(readId(jParser));
				}
			}
		}

		return name;
	}

	/**
	 * reads a set of occurrence constructs
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @param parent
	 *            - the parent topic, can be <code>null</code>
	 * @return set of occurrence constructs
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private static Set<Occurrence> readOccurrences(JsonParser jParser, Topic parent) throws JsonParseException, IOException {

		Stack<State> stack = new Stack<JTMQRConstructReader.State>();
		Set<Occurrence> result = new HashSet<Occurrence>();

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();

			if (token.equals(JsonToken.START_OBJECT)) {
				Occurrence occurrence = readOccurrence(jParser, parent);
				result.add(occurrence);
			} else if (token.equals(JsonToken.START_ARRAY)) {
				stack.push(State.ARRAY);
			} else if (token.equals(JsonToken.END_ARRAY)) {
				stack.pop();

				if (stack.isEmpty()) {
					return result;
				}
			}
		}

		return result;
	}

	/**
	 * reads a single occurrence construct
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @param parent
	 *            - the parent topic, can be <code>null</code>
	 * @return the occurrence construct
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Occurrence readOccurrence(JsonParser jParser, Topic parent) throws JsonParseException, IOException {

		Stack<State> stack = new Stack<JTMQRConstructReader.State>();
		OccurrenceStub occurrence = new OccurrenceStub(parent);

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.END_OBJECT)) {

				if (stack.isEmpty()) {
					return occurrence;
				}

				stack.pop();

			} else if (token.equals(JsonToken.START_OBJECT)) {
				stack.push(State.OBJECT);
			} else if (token.equals(JsonToken.START_ARRAY)) {
				stack.push(State.ARRAY);
			} else if (token.equals(JsonToken.END_ARRAY)) {
				stack.pop();

			} else if (token.equals(JsonToken.FIELD_NAME)) {

				if (text.equals(IJTMConstants.VALUE)) {
					occurrence._setValue(readValue(jParser));
				} else if (text.equals(IJTMConstants.TYPE)) {
					occurrence._setType(readTopicReference(jParser));
				} else if (text.equals(IJTMConstants.SCOPE)) {
					occurrence._setScope(readScope(jParser));
				} else if (text.equals(IJTMConstants.REIFIER)) {
					occurrence._setReifier(readTopicReference(jParser));
				} else if (text.equals(IJTMConstants.DATATYPE)) {
					token = jParser.nextToken();
					occurrence._setDatatype(jParser.getText());
				} else if (text.equals(IJTMConstants.ITEM_IDENTIFIERS)) {
					occurrence._setItemIdentifiers(readIdentifier(jParser));
				} else if (text.equals(IJTMConstants.ID)) {
					occurrence._setId(readId(jParser));
				}
			}
		}

		return occurrence;
	}

	/**
	 * read a single association
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @return the association construct
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Association readAssociation(JsonParser jParser) throws JsonParseException, IOException {

		AssociationStub association = new AssociationStub();

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.END_OBJECT)) {

				return association;

			} else if (token.equals(JsonToken.FIELD_NAME)) {

				if (text.equals(IJTMConstants.TYPE)) {
					association._setType(readTopicReference(jParser));
				} else if (text.equals(IJTMConstants.SCOPE)) {
					association._setScope(readScope(jParser));
				} else if (text.equals(IJTMConstants.REIFIER)) {
					association._setReifier(readTopicReference(jParser));
				} else if (text.equals(IJTMConstants.ROLES)) {
					association._setRoles(readRoles(jParser, association));
				} else if (text.equals(IJTMConstants.ITEM_IDENTIFIERS)) {
					association._setItemIdentifiers(readIdentifier(jParser));
				} else if (text.equals(IJTMConstants.ID)) {
					association._setId(readId(jParser));
				}
			}
		}

		return association;
	}

	/**
	 * reads a set of role constructs
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @param parent
	 *            - the parent association, can be <code>null</code>
	 * @return a set of role constructs
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private static Set<Role> readRoles(JsonParser jParser, Association parent) throws JsonParseException, IOException {

		Stack<State> stack = new Stack<JTMQRConstructReader.State>();
		Set<Role> result = new HashSet<Role>();

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();

			if (token.equals(JsonToken.START_OBJECT)) {
				Role role = readRole(jParser, parent);
				result.add(role);
			} else if (token.equals(JsonToken.START_ARRAY)) {
				stack.push(State.ARRAY);
			} else if (token.equals(JsonToken.END_ARRAY)) {
				stack.pop();

				if (stack.isEmpty()) {
					return result;
				}
			}
		}

		return result;
	}

	/**
	 * reads a single role construct
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @param parent
	 *            - the parent association, can be <code>null</code>
	 * @return the role construct
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Role readRole(JsonParser jParser, Association parent) throws JsonParseException, IOException {

		Stack<State> stack = new Stack<JTMQRConstructReader.State>();
		RoleStub role = new RoleStub(parent);

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.END_OBJECT)) {

				if (stack.isEmpty()) {
					return role;
				}

				stack.pop();

			} else if (token.equals(JsonToken.START_OBJECT)) {
				stack.push(State.OBJECT);
			} else if (token.equals(JsonToken.START_ARRAY)) {
				stack.push(State.ARRAY);
			} else if (token.equals(JsonToken.END_ARRAY)) {
				stack.pop();

			} else if (token.equals(JsonToken.FIELD_NAME)) {

				if (text.equals(IJTMConstants.PLAYER)) {
					role._setPlayer(readTopicReference(jParser));
				} else if (text.equals(IJTMConstants.TYPE)) {
					role._setType(readTopicReference(jParser));
				} else if (text.equals(IJTMConstants.REIFIER)) {
					role._setReifier(readTopicReference(jParser));
				} else if (text.equals(IJTMConstants.ITEM_IDENTIFIERS)) {
					role._setItemIdentifiers(readIdentifier(jParser));
				} else if (text.equals(IJTMConstants.ID)) {
					role._setId(readId(jParser));
				}
			}
		}

		return role;
	}

	/**
	 * reads a topic reference
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @return the topic construct
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Topic readTopicReference(JsonParser jParser) throws JsonParseException, IOException {
		return readTopicReference(jParser, true);
	}

	/**
	 * reads a topic reference
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @param nextToken
	 *            flag if first the next token should fetched
	 * @return the topic construct
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Topic readTopicReference(JsonParser jParser, boolean nextToken) throws JsonParseException, IOException {

		JsonToken token;
		if (nextToken) {
			token = jParser.nextToken();
		} else {
			token = jParser.getCurrentToken();
		}

		if (!token.equals(JsonToken.VALUE_STRING)) {
			throw new RuntimeException("Unexpected token.");
		}

		String text = jParser.getText();
		TopicStub topic = new TopicStub();

		if (text.startsWith(IJTMConstants.PREFIX_II)) {
			topic._addItemIdentifier(text.substring(3));
		} else if (text.startsWith(IJTMConstants.PREFIX_SI)) {
			topic._addSubjectIdentifier(text.substring(3));
		} else if (text.startsWith(IJTMConstants.PREFIX_SL)) {
			topic._addSubjectLocator(text.substring(3));
		} else {
			throw new RuntimeException("Unable to specify identifier type");
		}

		return topic;
	}

	/**
	 * reads a scope, i.e. a set of themes
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @return set of themes
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private static Set<Topic> readScope(JsonParser jParser) throws JsonParseException, IOException {

		Stack<State> stack = new Stack<JTMQRConstructReader.State>();

		Set<Topic> scope = new HashSet<Topic>();

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.START_ARRAY)) {
				stack.push(State.ARRAY);
			} else if (token.equals(JsonToken.END_ARRAY)) {
				stack.pop();

				if (stack.isEmpty()) {
					return scope;
				}
			} else if (token.equals(JsonToken.VALUE_STRING)) {
				scope.add(createTopicByIdentifier(text));
			}
		}

		return scope;
	}

	/**
	 * reads a set of variants
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @param parent
	 *            - the parent name, can be <code>null</code>
	 * @return set of variant constructs
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private static Set<Variant> readVariants(JsonParser jParser, Name parent) throws JsonParseException, IOException {

		Stack<State> stack = new Stack<JTMQRConstructReader.State>();

		Set<Variant> result = new HashSet<Variant>();

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();

			if (token.equals(JsonToken.END_ARRAY)) {

				stack.pop();

				if (stack.isEmpty()) {
					return result;
				}

			} else if (token.equals(JsonToken.START_ARRAY)) {
				stack.push(State.ARRAY);
			} else if (token.equals(JsonToken.START_OBJECT)) {
				result.add(readVariant(jParser, parent));
			}
		}

		return result;
	}

	/**
	 * reads a single variant
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @param parent
	 *            - the parent name, can be <code>null</code>
	 * @return the variant construct
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Variant readVariant(JsonParser jParser, Name parent) throws JsonParseException, IOException {

		VariantStub variant = new VariantStub(parent);

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.END_OBJECT)) {

				return variant;

			} else if (token.equals(JsonToken.FIELD_NAME)) {
				if (text.equals(IJTMConstants.SCOPE)) {
					variant.setScope(readScope(jParser));
				} else if (text.equals(IJTMConstants.REIFIER)) {
					variant._setReifier(readTopicReference(jParser));
				} else if (text.equals(IJTMConstants.VALUE)) {
					variant._setValue(readValue(jParser));
				} else if (text.equals(IJTMConstants.DATATYPE)) {
					token = jParser.nextToken();
					variant._setDatatype(jParser.getText());
				} else if (text.equals(IJTMConstants.ITEM_IDENTIFIERS)) {
					variant._setItemIdentifiers(readIdentifier(jParser));
				} else if (text.equals(IJTMConstants.ID)) {
					variant._setId(readId(jParser));
				}
			}
		}

		return variant;
	}

	/**
	 * reads a value
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @return the value as string
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private static String readValue(JsonParser jParser) throws JsonParseException, IOException {

		jParser.nextToken();
		String text = jParser.getText();

		return text;
	}

	/**
	 * reads an id
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @return the id as string
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private static String readId(JsonParser jParser) throws JsonParseException, IOException {

		jParser.nextToken();
		String text = jParser.getText();
		return text;
	}

	/**
	 * reads a number if identifier
	 * 
	 * @param jParser
	 *            - the json parser pointing to the the correct position
	 * @return set of identifier as strings
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private static Set<String> readIdentifier(JsonParser jParser) throws JsonParseException, IOException {

		Set<String> identifier = new HashSet<String>();

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.END_ARRAY)) {
				return identifier;
			}

			if (token.equals(JsonToken.VALUE_STRING)) {
				identifier.add(text);
			}
		}

		return identifier;
	}

	/**
	 * creates a topic from an prefixed identifier, i.e. starting with si:, sl:
	 * or ii:
	 * 
	 * @param prefixedIdentifier
	 *            - the prefixed identifier
	 * @return the topic
	 */
	private static Topic createTopicByIdentifier(String prefixedIdentifier) {

		TopicStub topic = new TopicStub();

		if (prefixedIdentifier.startsWith(IJTMConstants.PREFIX_II)) {
			topic._addItemIdentifier(prefixedIdentifier.substring(3));
		} else if (prefixedIdentifier.startsWith(IJTMConstants.PREFIX_SI)) {
			topic._addSubjectIdentifier(prefixedIdentifier.substring(3));
		} else if (prefixedIdentifier.startsWith(IJTMConstants.PREFIX_SL)) {
			topic._addSubjectLocator(prefixedIdentifier.substring(3));
		} else {
			throw new RuntimeException("Unable to specify identifier type");
		}

		return topic;
	}
	
	/**
	 * reads the aliase from meta data
	 * 
	 * @param jParser
	 * @return map of aliases and indexes
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Map<String, Integer> readAliases(JsonParser jParser) throws JsonParseException, IOException {

		Map<String, Integer> aliases = new HashMap<String, Integer>();

		int currendIndex = -1;

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.END_OBJECT)) {
				return aliases;
			} else if (token.equals(JsonToken.FIELD_NAME)) {
				currendIndex = Integer.parseInt(text);
			} else if (token.equals(JsonToken.VALUE_STRING)) {
				if (text != null) {
					aliases.put(text, currendIndex);
				}
			}
		}

		return aliases;
	}
	
	/**
	 * reads the header from meta data
	 * 
	 * @param jParser
	 * @return map of aliases and indexes
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static Map<String, Integer> readHeader(JsonParser jParser) throws JsonParseException, IOException {

		Map<String, Integer> header = new HashMap<String, Integer>();

		int currendIndex = -1;

		while (jParser.nextToken() != null) {

			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();

			if (token.equals(JsonToken.END_OBJECT)) {
				return header;
			} else if (token.equals(JsonToken.VALUE_STRING)) {
				currendIndex++; 
				header.put(text, currendIndex);
			}
		}

		return header;
	}
	
}
