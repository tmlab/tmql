/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.path.grammar.lexical.WhiteSpace;

/**
 * @author Sven Krosse
 *
 */
public abstract class FunctionImpl implements IQueryPart {

	private List<IQueryPart> arguments;
	
	/**
	 * Adding an argument
	 * @param argument the argument
	 */
	protected void addArgument(IQueryPart argument){
		if ( arguments == null ){
			arguments = new ArrayList<IQueryPart>();
		}
		arguments.add(argument);
	}
	
	/**
	 * Returns all arguments
	 * @return the arguments
	 */
	List<IQueryPart> getArguments() {
		if ( arguments == null ){
			return Collections.emptyList();
		}
		return arguments;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		StringBuilder builder = new StringBuilder();
		builder.append(WhiteSpace.TOKEN);
		builder.append(getFunctionName());
		builder.append(WhiteSpace.TOKEN);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(WhiteSpace.TOKEN);
		boolean first = true;
		for ( IQueryPart argument : getArguments()){
			if ( !first ){
				builder.append(Comma.TOKEN);
				builder.append(WhiteSpace.TOKEN);
			}
			builder.append(argument.toTmql());
			builder.append(WhiteSpace.TOKEN);
			first = false;
		}
		builder.append(BracketRoundClose.TOKEN);
		builder.append(WhiteSpace.TOKEN);
		return builder.toString();
	}

	
	/**
	 * Returns the name of the function
	 * @return the function name
	 */
	protected abstract String getFunctionName();
}
