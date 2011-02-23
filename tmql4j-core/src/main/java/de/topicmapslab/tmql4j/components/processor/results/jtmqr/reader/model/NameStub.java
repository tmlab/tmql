package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.tmapi.core.Locator;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

/**
 * name implementation for jtmqr result set
 * @author Christian Haß
 *
 */
public class NameStub extends CharacteristicStub implements Name {

	private Topic parent;
	private Set<Variant> variants;

	/**
	 * constructor
	 * @param parent - the parent topic or <code>null</code>
	 */
	protected NameStub(Topic parent) {
		this.parent = parent;
		this.variants = Collections.emptySet();
	}

	/**
	 * sets the variants of the name
	 * @param variants - set of variants
	 */
	protected void setVariants(Set<Variant> variants){
		this.variants = variants;
	}
	
	// --[ TMAPI methods ]------------------------------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Topic getType() {
		return this.type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setType(Topic arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addItemIdentifier(Locator arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Locator> getItemIdentifiers() {
		return this.itemIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TopicMap getTopicMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeItemIdentifier(Locator arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTheme(Topic arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Topic> getScope() {
		return this.scope;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeTheme(Topic arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Topic getReifier() {
		return this.reifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setReifier(Topic arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variant createVariant(String arg0, Topic... arg1) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variant createVariant(String arg0, Collection<Topic> arg1) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variant createVariant(Locator arg0, Topic... arg1) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variant createVariant(Locator arg0, Collection<Topic> arg1) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variant createVariant(String arg0, Locator arg1, Topic... arg2)	throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variant createVariant(String arg0, Locator arg1,	Collection<Topic> arg2) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Topic getParent() {
		return this.parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variant> getVariants() {
		return this.variants;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		Topic type = getType();
		return "Topic-Name{Parent:" + (getParent() == null ? "null" : getParent().toString()) + ";Type:" + (type == null ? "null" : type.toString())
				+ ";Value:" + getValue() + "}";
	}
	
}
