package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.results.model.IResultSet;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * class which reads a JTMQR result into a TMQL result set
 * 
 * @author Christian Haß
 * 
 */
public class JTMQRReader {

	private JsonParser jParser;
	private final InputStream inStream;
	
	
	/**
	 * constructor
	 * 
	 * @param in
	 *            - the input stream
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public JTMQRReader(InputStream in){
		this.inStream = in;
	}

	/**
	 * @return the {@link IResultSet} filled with the information from the input
	 *         stream
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public IResultSet<IResult> readResultSet() {

		try{
		
			// get version
			JsonFactory jFactory = new JsonFactory();
			this.jParser = jFactory.createJsonParser(this.inStream);
			String version = null;
			
			while (this.jParser.nextToken() != null) {
				
				JsonToken token = this.jParser.getCurrentToken();
				String text = this.jParser.getText();

				if(token.equals(JsonToken.FIELD_NAME) && text.equals("version")){
					this.jParser.nextToken();
					version = this.jParser.getText();
					break;
				}
			}
			
			if(version == null)
				throw new TMQLRuntimeException("Cant get version string.");
			
			if(version.equals("1.0")){
				
				return new JTMQR1Reader(this.jParser).readResultSet();
				
			}else if(version.equals("2.0")){
				
				return new JTMQR2Reader(this.jParser).readResultSet();
				
			}else{
				throw new TMQLRuntimeException("unexpected JTMQR version (" + version + ")");
			}
		
		}catch (Exception e) {
			throw new TMQLRuntimeException("Parse JTMQR failed.", e);
		}
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
