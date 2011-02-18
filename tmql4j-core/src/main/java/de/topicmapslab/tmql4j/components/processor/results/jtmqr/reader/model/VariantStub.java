package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import org.tmapi.core.Locator;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Name;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

public class VariantStub implements Variant {

	@Override
	public BigDecimal decimalValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Locator getDatatype() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigInteger integerValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator locatorValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long longValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setValue(String arg0) throws ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(Locator arg0) throws ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(BigDecimal arg0) throws ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(BigInteger arg0) throws ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(float arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0, Locator arg1)
			throws ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Topic getReifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReifier(Topic arg0) throws ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addItemIdentifier(Locator arg0) throws ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Locator> getItemIdentifiers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopicMap getTopicMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeItemIdentifier(Locator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTheme(Topic arg0) throws ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTheme(Topic arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Name getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Topic> getScope() {
		// TODO Auto-generated method stub
		return null;
	}

}
