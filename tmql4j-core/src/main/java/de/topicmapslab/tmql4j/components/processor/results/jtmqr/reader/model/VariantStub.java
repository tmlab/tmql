package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import org.tmapi.core.Locator;
import org.tmapi.core.MalformedIRIException;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * variant implementation for jtmqr result set
 * 
 * @author Christian Haß
 * 
 */
public class VariantStub extends ConstructStub implements Variant {

	private final Name parent;
	private String value;
	private Set<Topic> scope;
	private Topic reifier;
	private Locator datatype;

	/**
	 * constructor
	 * 
	 * @param parent
	 *            - the parent name or <code>null</code>
	 */
	protected VariantStub(Name parent) {
		this.parent = parent;
		this.scope = Collections.emptySet();
		this.datatype = new LocatorStub("http://www.w3.org/2001/XMLSchema#string");
	}

	/**
	 * sets the value
	 * 
	 * @param value
	 *            - the value
	 */
	protected void _setValue(String value) {
		this.value = value;
	}

	/**
	 * sets the scope
	 * 
	 * @param scope
	 *            - the scope
	 */
	protected void setScope(Set<Topic> scope) {
		this.scope = scope;
	}

	/**
	 * sets the reifier
	 * 
	 * @param reifier
	 *            - the reifier
	 */
	protected void _setReifier(Topic reifier) {
		this.reifier = reifier;
	}

	/**
	 * sets the datatype
	 * 
	 * @param datatype
	 */
	protected void _setDatatype(String datatype) {
		this.datatype = new LocatorStub(datatype);
	}

	// --[ TMAPI methods
	// ]----------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal decimalValue() {
		return LiteralUtils.asDecimal(getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float floatValue() {
		return LiteralUtils.asDouble(getValue()).floatValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Locator getDatatype() {
		return this.datatype;
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
	public int intValue() {
		return LiteralUtils.asInteger(getValue()).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger integerValue() {
		return LiteralUtils.asInteger(getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Locator locatorValue() {
		return new Locator() {

			@Override
			public String toExternalForm() {
				try {
					return new URI(getValue()).toASCIIString();
				} catch (URISyntaxException e) {
					throw new TMQLRuntimeException(e);
				}
			}

			@Override
			public Locator resolve(String value) throws MalformedIRIException {
				throw new UnsupportedOperationException();
			}

			@Override
			public String getReference() {
				return getValue();
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long longValue() {
		return LiteralUtils.asInteger(getValue()).longValue();
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
	public void setValue(Locator arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(BigDecimal arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(BigInteger arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(long arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(float arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(int arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String arg0, Locator arg1) throws ModelConstraintException {
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
	public void addItemIdentifier(Locator arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
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
	public void removeTheme(Topic arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Name getParent() {
		return this.parent;
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
	public String toString() {
		return "Topic-Name-Variant{Parent:" + (getParent() == null ? "null" : getParent().toString()) + ";Value:" + getValue() + ";Datatype:" + getDatatype().toExternalForm() + "}";
	}

}
