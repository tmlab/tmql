package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model.ConstructReader;
import de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.result.SimpleJtmqrResultSet;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
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
	public IResultSet<IResult> readResultSet() throws JsonParseException, IOException{

				
		SimpleJtmqrResultSet resultSet = new SimpleJtmqrResultSet();
		boolean first = true;
		IResult result = resultSet.createResult();
		
		while (this.jParser.nextToken() != null) {
			
			JsonToken token = this.jParser.getCurrentToken();
			String text = this.jParser.getText();
			
			if(token.equals(JsonToken.FIELD_NAME)){
				
				if(text.equals("aliases")){
					
					// read column labels
					Map<String, Integer> aliases = ConstructReader.readAliases(this.jParser);
					Map<Integer, String> indexes = createIndexes(aliases);
					
					resultSet.setAlias(aliases);
					resultSet.setIndexes(indexes);
					
				}else if(text.equals("t")){
					
					// new token
					
					if(first){
						first = false;
					}else{
						resultSet.addResult(result);
						result = resultSet.createResult();
					}
				}else if(text.equals("s")){

					// read string result
					token = this.jParser.nextToken();
					text = this.jParser.getText();
					result.add(text);
					
				}else if(text.equals("n")){

					// read number result
					token = this.jParser.nextToken();
					text = this.jParser.getText();

					if(token.equals(JsonToken.VALUE_NUMBER_FLOAT)){
						result.add(Double.parseDouble(text));
					}else if(token.equals(JsonToken.VALUE_NUMBER_INT)){
						result.add(Integer.parseInt(text));
					}
					
				}else if(text.equals("item_type")){

					// read object result
					token = this.jParser.nextToken();
					text = this.jParser.getText();

					if(text.equals("topic")){
						result.add(ConstructReader.readTopic(this.jParser));
					}else if(text.equals("name")){
						result.add(ConstructReader.readName(this.jParser, null));
					}else if(text.equals("occurrence")){
						result.add(ConstructReader.readOccurrence(this.jParser, null));
					}else if(text.equals("association")){
						result.add(ConstructReader.readAssociation(this.jParser));
					}else if(text.equals("role")){
						result.add(ConstructReader.readRole(this.jParser, null));
					}else if(text.equals("variant")){
						result.add(ConstructReader.readVariant(this.jParser, null));
					}
				}
			}
		}
		
		if(!first)
			resultSet.addResult(result);

		return resultSet;
	}
	
	/**
	 * creates an index from an alias
	 * @param aliases - the alias
	 * @return the index
	 */
	private Map<Integer, String> createIndexes(Map<String, Integer> aliases){
	
		Map<Integer, String> indexes = new HashMap<Integer, String>();
		
		for(Map.Entry<String, Integer>alias:aliases.entrySet())
			indexes.put(alias.getValue(), alias.getKey());
		
		return indexes;
	}

}
