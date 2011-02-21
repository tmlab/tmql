package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader;

import java.io.IOException;
import java.io.InputStream;

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
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;

/**
 * class which reads a JTMQR result into a TMQL result set
 * @author Christian Haß
 *
 */
public class JTMQRReader {

	private JsonParser jParser;
	
	/**
	 * constructor
	 * @param in - the input stream
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public JTMQRReader(InputStream in) throws JsonParseException, IOException {
		JsonFactory jFactory = new JsonFactory();
		this.jParser = jFactory.createJsonParser(in);
	}
	
	/**
	 * @return the {@link IResultSet} filled with the information from the input stream
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public IResultSet<?> readResultSet() throws JsonParseException, IOException{

		while (this.jParser.nextToken() != null) {
			
			JsonToken token = this.jParser.getCurrentToken();
			String text = this.jParser.getText();

			System.out.println("readResultSet " + token + " --> " + text);
			
			if(token.equals(JsonToken.FIELD_NAME)){
				
				if(text.equals("item_type")){
					
					token = this.jParser.nextToken();
					text = this.jParser.getText();

					System.out.println("readResultSet " + token + " --> " + text);
					
					if(text.equals("topic")){
						Topic topic = ConstructReader.readTopic(this.jParser);
					}else if(text.equals("name")){
						Name name = ConstructReader.readName(this.jParser, null);
					}else if(text.equals("occurrence")){
						Occurrence occurrence = ConstructReader.readOccurrence(this.jParser, null);
					}else if(text.equals("association")){
						Association association = ConstructReader.readAssociation(this.jParser);
					}else if(text.equals("role")){
						Role role = ConstructReader.readRole(this.jParser, null);
					}else if(text.equals("variant")){
						Variant variant = ConstructReader.readVariant(this.jParser, null);
					}
				}
			}
		}

		return null;
	}
	
}
