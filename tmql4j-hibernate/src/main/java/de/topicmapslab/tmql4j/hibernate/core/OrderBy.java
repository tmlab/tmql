/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.hibernate.path.Navigation;
import de.topicmapslab.tmql4j.path.grammar.lexical.Asc;
import de.topicmapslab.tmql4j.path.grammar.lexical.By;
import de.topicmapslab.tmql4j.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.path.grammar.lexical.Desc;
import de.topicmapslab.tmql4j.path.grammar.lexical.Order;
import de.topicmapslab.tmql4j.path.grammar.lexical.WhiteSpace;

/**
 * @author Sven Krosse
 *
 */
public class OrderBy implements IQueryPart {

	private List<OrderByPart> content;
	
	/**
	 * Adding a new navigation as order condition
	 * @param navigation the condition
	 * @param ascending flag for sort order
	 */
	public void add(Navigation navigation, boolean ascending){
		if ( content == null ){
			content = new ArrayList<OrderBy.OrderByPart>();
		}
		OrderByPart part = new OrderByPart();
		part.ascending = ascending;
		part.navigation = navigation;
		content.add(part);
	}
	
	/**
	 * Remove a navigation as order 
	 * @param navigation the condition
	 */
	public void remove(Navigation navigation){
		if ( content != null ){
			OrderByPart part = new OrderByPart();
			part.navigation = navigation;
			content.remove(part);
		}
	}
	
	/**
	 * Returns the content
	 * @return the content
	 */
	List<OrderByPart> getContent() {
		if ( content == null ){
			return Collections.emptyList();
		}
		return content;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toTmql() throws InvalidModelException {
		if ( getContent().isEmpty()){
			throw new InvalidModelException("At least one item should contained!");
		}
		StringBuilder builder = new StringBuilder();
		builder.append(Order.TOKEN);
		builder.append(WhiteSpace.TOKEN);
		builder.append(By.TOKEN);
		builder.append(WhiteSpace.TOKEN);
		boolean first = true;
		for ( OrderByPart part : getContent() ){
			if ( !first){
				builder.append(Comma.TOKEN);
				builder.append(WhiteSpace.TOKEN);
			}
			builder.append(part.navigation.toTmql());
			builder.append(WhiteSpace.TOKEN);
			builder.append(part.ascending?Asc.TOKEN:Desc.TOKEN);
			builder.append(WhiteSpace.TOKEN);
			first = false;
		}
		return builder.toString();
	}
	
	/**
	 * Inner class as order by part
	 */
	class OrderByPart{
		Navigation navigation;
		boolean ascending;
		
		/**
		  * {@inheritDoc}
		  */
		@Override
		public int hashCode() {
			return navigation.hashCode();
		}
		
		/**
		  * {@inheritDoc}
		  */
		@Override
		public boolean equals(Object obj) {
			if ( obj == this){
				return true;
			}
			if ( obj instanceof OrderByPart ){
				return navigation.equals(((OrderByPart) obj).navigation);
			}
			return false;
		}
	}

}
