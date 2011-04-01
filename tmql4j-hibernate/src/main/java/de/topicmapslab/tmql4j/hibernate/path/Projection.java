/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.path;

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
public class Projection implements IQueryPart {

	private List<IQueryPart> items;
	
	/**
	 * Adding a new item to internal list
	 * @param item the item
	 */
	public void add(IQueryPart item){
		if ( items == null ){
			items = new ArrayList<IQueryPart>();
		}
		items.add(item);
	}
	
	/**
	 * Removes the given item from internal list
	 * @param item the item
	 */
	public void remove(IQueryPart item){
		if ( items != null ){
			items.remove(item);
		}
	}
	
	/**
	 * Returns all items
	 * @return the items
	 */
	List<IQueryPart> getItems() {
		if ( items == null){
			return Collections.emptyList();
		}			
		return items;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		StringBuilder builder = new StringBuilder();
		builder.append(WhiteSpace.TOKEN);
		builder.append(BracketRoundOpen.TOKEN);
		builder.append(WhiteSpace.TOKEN);
		boolean first = true;
		for ( IQueryPart item : getItems()){
			if (!first){
				builder.append(Comma.TOKEN);
				builder.append(WhiteSpace.TOKEN);
			}
			builder.append(item.toTmql());
			builder.append(WhiteSpace.TOKEN);
			first = false;
		}
		builder.append(WhiteSpace.TOKEN);
		builder.append(BracketRoundClose.TOKEN);
		builder.append(WhiteSpace.TOKEN);
		return builder.toString();
	}
	
	/**
	  * {@inheritDoc}
	  */
	@Override
	public Projection clone() throws CloneNotSupportedException {
		Projection clone = new Projection();
		for ( IQueryPart part : getItems()){
			clone.add(part.clone());
		}
		return clone;
	}

}
