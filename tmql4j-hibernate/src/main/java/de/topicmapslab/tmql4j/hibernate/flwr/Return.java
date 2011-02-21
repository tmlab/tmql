/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.flwr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.path.grammar.lexical.WhiteSpace;

/**
 * @author Sven Krosse
 *
 */
public class Return implements IQueryPart {

	private List<IQueryPart> items;
	
	
	/**
	 * Add the item to 
	 * @param item
	 */
	public void add(IQueryPart item){
		if ( items == null ){
			items = new ArrayList<IQueryPart>();
		}
		items.add(item);
	}
	
	/**
	 * Remove the item from return list
	 * @param item the item
	 */
	public void remove(IQueryPart item){
		if ( items != null ){
			items.remove(item);
		}
	}
	
	/**
	 * Getting all items
	 * @return the items
	 */
	List<IQueryPart> getItems() {
		if ( items == null ){
			return Collections.emptyList();
		}
		return items;
	}
	
	/**
	  * {@inheritDoc}
	  */
	@Override
	public String toTmql() throws InvalidModelException {
		if ( getItems().isEmpty()){
			throw new InvalidModelException("At least one item should contained!");
		}
		StringBuilder builder = new StringBuilder();
		builder.append(de.topicmapslab.tmql4j.flwr.grammar.lexical.Return.TOKEN);
		builder.append(WhiteSpace.TOKEN);
		/*
		 * is inner query
		 */
		if ( getItems().size() == 1 && getItems().get(0) instanceof Flwr ){
			builder.append(BracketSquareOpen.TOKEN);
			builder.append(WhiteSpace.TOKEN);
		}
		boolean first = true;
		for ( IQueryPart item : getItems()){
			if ( !first){
				builder.append(Comma.TOKEN);
				builder.append(WhiteSpace.TOKEN);
			}
			builder.append(item.toTmql());
			builder.append(WhiteSpace.TOKEN);
			first = false;
		}
		/*
		 * is inner query
		 */
		if ( getItems().size() == 1 && getItems().get(0) instanceof Flwr ){
			builder.append(WhiteSpace.TOKEN);
			builder.append(BracketSquareClose.TOKEN);
		}
		return builder.toString();
	}
	
}
