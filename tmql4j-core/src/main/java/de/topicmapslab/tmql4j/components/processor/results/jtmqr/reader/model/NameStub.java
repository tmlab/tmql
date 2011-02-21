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

public class NameStub extends CharacteristicStub implements Name {

	private Topic parent;
	private Set<Variant> variants;

	
	protected NameStub(Topic parent) {
		this.parent = parent;
		this.variants = Collections.emptySet();
	}

	protected void setVariants(Set<Variant> variants){
		this.variants = variants;
	}
	
	// --[ TMAPI methods ]------------------------------------------------------------------------
	
	@Override
	public Topic getType() {
		return this.type;
	}

	@Override
	public void setType(Topic arg0) {
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
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeItemIdentifier(Locator arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addTheme(Topic arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Topic> getScope() {
		return this.scope;
	}

	@Override
	public void removeTheme(Topic arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Topic getReifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReifier(Topic arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Variant createVariant(String arg0, Topic... arg1) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Variant createVariant(String arg0, Collection<Topic> arg1) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Variant createVariant(Locator arg0, Topic... arg1) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Variant createVariant(Locator arg0, Collection<Topic> arg1) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Variant createVariant(String arg0, Locator arg1, Topic... arg2)	throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Variant createVariant(String arg0, Locator arg1,	Collection<Topic> arg2) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Topic getParent() {
		if(this.parent != null)
			return this.parent;
		throw new UnsupportedOperationException();
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public Set<Variant> getVariants() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(String arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

}
