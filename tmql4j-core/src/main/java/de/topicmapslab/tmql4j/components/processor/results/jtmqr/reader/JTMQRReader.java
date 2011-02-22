package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model.ConstructReader;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResult;
import de.topicmapslab.tmql4j.components.processor.results.tmdm.SimpleResultSet;

/**
 * class which reads a JTMQR result into a TMQL result set
 * @author Christian Haß
 *
 */
public class JTMQRReader {

	private JsonParser jParser;
	private Stack<State> stack;
	
	
	private Number currentNumber;
	private String currentString;
	private Object currentObject;
	
	private enum State{
		NUMBER, STRING, OBJECT
	}
	
	/**
	 * constructor
	 * @param in - the input stream
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public JTMQRReader(InputStream in) throws JsonParseException, IOException {
		JsonFactory jFactory = new JsonFactory();
		this.jParser = jFactory.createJsonParser(in);
		this.stack = new Stack<JTMQRReader.State>();
	}
	
	/**
	 * @return the {@link IResultSet} filled with the information from the input stream
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public IResultSet<IResult> readResultSet() throws JsonParseException, IOException{

				
		while (this.jParser.nextToken() != null) {
			
			JsonToken token = this.jParser.getCurrentToken();
			String text = this.jParser.getText();

			System.out.println("readResultSet " + token + " --> " + text);
			
			
			if(!this.stack.isEmpty() && token.equals(JsonToken.VALUE_STRING) && this.stack.peek().equals(State.STRING)){
				System.out.println("Found stirng value: " + text);
				
				this.currentString = text;
				
			}else if(!this.stack.isEmpty() && token.equals(JsonToken.VALUE_NUMBER_FLOAT) && this.stack.peek().equals(State.NUMBER)){
				System.out.println("Found number value: " + text);
				
				this.currentNumber = Float.parseFloat(text);
				
			}else if(!this.stack.isEmpty()&& token.equals(JsonToken.VALUE_NUMBER_INT) && this.stack.peek().equals(State.NUMBER)){
				System.out.println("Found number value: " + text);
				
				this.currentNumber = Integer.parseInt(text);
			
			}else if(token.equals(JsonToken.FIELD_NAME)){
				
				if(text.equals("t")){
					
					System.out.println("NEW TOKEN");
					
					if(!this.stack.isEmpty()){
						
					
						
						
					}
					
					
					stack.clear(); // clear stack if new token
					
					
					
				}else if(text.equals("s") && this.stack.isEmpty()){
					stack.push(State.STRING);
					System.out.println("...set sate to string");
					
				}else if(text.equals("n") && this.stack.isEmpty()){
					stack.push(State.NUMBER);
					System.out.println("...set sate to number");
				}else if(text.equals("i") && this.stack.isEmpty()){
					stack.push(State.OBJECT);
					System.out.println("...set sate to object");
				
				
				
					
				}else if(text.equals("item_type") && this.stack.peek().equals(State.OBJECT)){
					
					token = this.jParser.nextToken();
					text = this.jParser.getText();
					

					System.out.println("readResultSet " + token + " --> " + text);
					
					if(text.equals("topic")){
						System.out.println("START READ TOPIC");
						Topic topic = ConstructReader.readTopic(this.jParser);
						
					}else if(text.equals("name")){

						System.out.println("START READ NAME");
						Name name = ConstructReader.readName(this.jParser, null);
						
					}else if(text.equals("occurrence")){
						
						System.out.println("START READ OCCURRENCE");
						Occurrence occurrence = ConstructReader.readOccurrence(this.jParser, null);
					}else if(text.equals("association")){
						
						System.out.println("START READ ASSOCIATION");
						Association association = ConstructReader.readAssociation(this.jParser);
					}else if(text.equals("role")){
						
						System.out.println("START READ ROLE");
						Role role = ConstructReader.readRole(this.jParser, null);
					}else if(text.equals("variant")){
						
						System.out.println("START READ VARIANT");
						Variant variant = ConstructReader.readVariant(this.jParser, null);
					}
				}
			
			}
		}

		return null;
	}
	
}
