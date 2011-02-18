package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.io.IOException;
import java.util.Set;
import java.util.Stack;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;

public class ConstructReader {

	private enum State {
		OBJECT, ARRAY
	}
	
	
	/*
	 * reads a name object
	 */
	public static Name readName(JsonParser jParser, Topic parent) throws JsonParseException, IOException{
		
		Stack<State> stack = new Stack<ConstructReader.State>();
		NameStub name = new NameStub(parent);
		
		while (jParser.nextToken() != null) {
			
			JsonToken token = jParser.getCurrentToken();
			String text = jParser.getText();
		
			if(token.equals(JsonToken.END_OBJECT)){
				
				if(stack.isEmpty()){
				
					System.out.println("End of processing");
					return name;
				}
				
				stack.pop();
				
			}else if(token.equals(JsonToken.START_OBJECT)){
				stack.push(State.OBJECT);
			}else if(token.equals(JsonToken.START_ARRAY)){
				stack.push(State.ARRAY);
			}else if(token.equals(JsonToken.END_ARRAY)){
				stack.pop();
				
			}else if(token.equals(JsonToken.FIELD_NAME)){
				
				if(text.equals("value")){
					name._setValue(jParser.getText());
				}else if(text.equals("type")){
					name._setType(readTopicReference(jParser));
				}else if(text.equals("scope")){
					name.setScope(readScope(jParser));
				}else if(text.equals("variants")){
					
				}else if(text.equals("reifier")){
					
				}
			}
		}
		
		return name;
	}
	
	/*
	 * read a topic reference
	 */
	public static Topic readTopicReference(JsonParser jParser) throws JsonParseException, IOException{

		JsonToken token = jParser.nextToken();
		
		if(!token.equals(JsonToken.VALUE_STRING))
			throw new RuntimeException("Unexpected token.");
		
		String text = jParser.getText();
		TopicStub topic = new TopicStub(); 
		
		if(text.startsWith("ii:")){
			topic.addItemIdentifier(text.substring(3));
		}else if(text.startsWith("si:")){
			topic.addSubjectIdentifier(text.substring(3));
		}else if(text.startsWith("sl:")){
			topic.addSubjectLocator(text.substring(3));
		}else
			throw new RuntimeException("Unable to specify identifier type");
		
		return topic;
	}

	public static Set<Topic> readScope(JsonParser jParser){
		
		
		
		
		return null;
	}
	
}
