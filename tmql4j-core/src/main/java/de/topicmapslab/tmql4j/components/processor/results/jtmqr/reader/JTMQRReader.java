package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.tmapi.core.Name;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model.ConstructReader;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;

public class JTMQRReader {

	private JsonParser jParser;

	private IResultSet<?> resultSet;
	
	
	private enum State {
		STRING, NUMBER, CONSTRUCT, TOPIC, NAME , OCCURRENCE
	}
	
	public JTMQRReader(InputStream in, IResultSet<?> resultSet) throws JsonParseException, IOException {
		JsonFactory jFactory = new JsonFactory();
		this.jParser = jFactory.createJsonParser(in);
		this.resultSet = resultSet;
	}
	
	public IResultSet<?> readResultSet() throws JsonParseException, IOException{

		while (this.jParser.nextToken() != null) {
			
			JsonToken token = this.jParser.getCurrentToken();
			String text = this.jParser.getText();

			if(token.equals(JsonToken.FIELD_NAME)){
				
				if(text.equals("item_type")){
					
					this.jParser.nextToken();
					String constructType = this.jParser.getText();
					
					
					if(constructType.equals("topic")){
					
						System.out.println("\nRead topic...");
						
					}else if(constructType.equals("name")){

						System.out.println("\nRead name...");
						Name n = ConstructReader.readName(this.jParser, null);
						
					}else if(constructType.equals("occurrence")){

						System.out.println("\nRead occurrence...");
					}
				}
			}
			
			//System.out.println(token + " - " + text);
			//System.out.println(this.state);
		}
		
		
		
		
		return null;
	}


	
}
