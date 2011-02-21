/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.builder;

import de.topicmapslab.tmql4j.hibernate.IQueryPart;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;
import de.topicmapslab.tmql4j.hibernate.path.Navigation;
import de.topicmapslab.tmql4j.hibernate.path.Path;
import de.topicmapslab.tmql4j.hibernate.path.Projection;
import de.topicmapslab.tmql4j.hibernate.path.Step;
import de.topicmapslab.tmql4j.hibernate.path.filter.Filter;
import de.topicmapslab.tmql4j.hibernate.path.function.FunctionImpl;

/**
 * Query Builder class for path style
 * 
 * @author Sven Krosse
 * 
 */
public class PathQueryBuilder extends QueryBuilderImpl {

	private Path path = new Path();
	private Navigation nav = null;
	private Projection tup = null;
	private FunctionImpl fct = null;
	private Step step = null;
	private Projection projection = null;

	/**
	 * Setting a tuple-expression as high level part of path
	 */
	public void tupleExpression() throws InvalidModelException {
		if (nav != null || fct != null) {
			throw new InvalidModelException(
					"Cannot add tuple expression at same level like function or navigation. Only one is allowed.");
		}
		this.tup = new Projection();
		this.path.setContent(tup);
	}

	/**
	 * Setting a function as high level part of path
	 */
	public void function(FunctionImpl fct) throws InvalidModelException {
		if (tup != null || nav != null) {
			throw new InvalidModelException(
					"Cannot add function at same level like navigation or tuple expression. Only one is allowed.");
		}
		this.fct = fct;
		this.path.setContent(fct);
	}

	/**
	 * Setting a navigation as high level part of path
	 */
	public void navigation(final String anchor) throws InvalidModelException {
		if (tup != null || fct != null) {
			throw new InvalidModelException(
					"Cannot add navigation at same level like function or tuple expression. Only one is allowed.");
		}
		this.nav = new Navigation(anchor);
		this.path.setContent(nav);
	}

	/**
	 * Setting a step as part of the navigation
	 * 
	 * @param step
	 *            the step
	 */
	public void step(final Step step) throws InvalidModelException {
		if (nav == null) {
			throw new InvalidModelException("Missing navigation expression for step content!");
		}
		this.step = step;
		this.nav.addStep(step);
	}

	/**
	 * Setting the filter for the last added step
	 * 
	 * @param filter
	 *            the filter
	 */
	public void filter(final Filter filter) throws InvalidModelException {
		if (step == null) {
			throw new InvalidModelException("Missing navigation step  to add filter content!");
		}
		this.step.setFilter(filter);
	}

	/**
	 * Setting the projection for navigation step or a tuple-expression as high level of path
	 * 
	 * @param projection
	 *            the projection
	 */
	public void projection(final Projection projection) throws InvalidModelException {
		if (fct != null || tup != null) {
			throw new InvalidModelException("Cannot add projection for function or tuple expression!");
		}
		if (nav != null) {
			this.nav.setProjection(projection);
		} else {
			this.tup = projection;
			this.path.setContent(tup);
		}
		this.projection = projection;
	}

	/**
	 * Adding a value expression to the projection or tuple expression
	 * 
	 * @param part
	 *            the part
	 */
	public void value(final IQueryPart part) throws InvalidModelException {
		if (projection != null) {
			projection.add(part);
		} else if (tup != null) {
			tup.add(part);
		} else {
			throw new InvalidModelException("Missing tuple expression or projection to add value expression!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toQueryString() {
		if (fct == null && tup == null && nav == null) {
			throw new RuntimeException("Missing content of path expression, nothing was set!");
		}
		return path.toTmql();
	}
	
	/**
	  * {@inheritDoc}
	  */
	@Override
	public PathQueryBuilder clone() throws CloneNotSupportedException {
		PathQueryBuilder clone = new PathQueryBuilder();
		if ( nav != null ){
			clone.nav = nav.clone();
			if ( !nav.getSteps().isEmpty()){
				clone.step = nav.getSteps().get(0);				
			}
		}
		if ( tup != null ){
			clone.tup = tup.clone();
		}
		if ( fct != null ){
			clone.fct = fct.clone();
		}
		if ( projection != null ){
			clone.projection = projection.clone();
		}		
		return clone;
	}

}
