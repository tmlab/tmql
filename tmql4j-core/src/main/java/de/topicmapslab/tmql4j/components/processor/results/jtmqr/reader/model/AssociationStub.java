package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.util.Collections;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Locator;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

public class AssociationStub extends ScopedStub implements Association {

	
	private Set<Role> roles;
	
	protected AssociationStub() {
		this.roles = Collections.emptySet();
	}
	
	protected void _setRoles(Set<Role> roles){
		this.roles = roles;
	}
	
	// --[ TMAPI methods ]-------------------------------------------------------
	
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
	public Topic getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setType(Topic arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTheme(Topic arg0) throws ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Topic> getScope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTheme(Topic arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Role createRole(Topic arg0, Topic arg1)
			throws ModelConstraintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopicMap getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Topic> getRoleTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Role> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Role> getRoles(Topic arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
