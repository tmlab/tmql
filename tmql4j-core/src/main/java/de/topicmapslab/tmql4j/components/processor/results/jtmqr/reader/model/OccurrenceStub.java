package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import org.tmapi.core.Locator;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.majortom.core.LocatorImpl;

/**
 * occurrence implementation for JTMQR result set
 * 
 * @author Christian Haß
 * 
 */
public class OccurrenceStub extends CharacteristicStub implements Occurrence {

	private final Topic parent;
	private Locator datatype;

	/**
	 * constructor
	 * 
	 * @param parent
	 *            - the parent topic or <code>null</code>
	 */
	protected OccurrenceStub(Topic parent) {
		this.parent = parent;
		this.datatype = new LocatorImpl("http://www.w3.org/2001/XMLSchema#string");
	}

	/**
	 * sets the datatype
	 * 
	 * @param datatype
	 *            - the datatype
	 */
	protected void _setDatatype(String datatype) {
		this.datatype = new LocatorImpl(datatype);
	}

	// --[ TMAPI methods
	// ]-------------------------------------------------------

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
	public BigDecimal decimalValue() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger integerValue() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Locator locatorValue() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long longValue() {
		// TODO Auto-generated method stub
		return 0;
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
	public Topic getParent() {
		return this.parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		Topic type = getType();
		return "Occurrence{Parent:" + (getParent() == null ? "null" : getParent().toString()) + ";Type:" + (type == null ? "null" : type.toString()) + ";Value:" + getValue() + ";Datatype:"
				+ getDatatype().toExternalForm() + "}";
	}

}
