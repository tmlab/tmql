package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Locator;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

/**
 * association implementation for jtmqr result set
 * @author Christian Haß
 *
 */
public class AssociationStub extends ScopedStub implements Association {

	
	private Set<Role> roles;
	
	/**
	 * constructor
	 */
	protected AssociationStub() {
		this.roles = Collections.emptySet();
	}
	
	/**
	 * adds the roles
	 * @param roles - the roles
	 */
	protected void _setRoles(Set<Role> roles){
		this.roles = roles;
	}
	
	// --[ TMAPI methods ]-------------------------------------------------------
	
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
	public Role createRole(Topic arg0, Topic arg1) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TopicMap getParent() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Topic> getRoleTypes() {
		
		Set<Topic> types = new HashSet<Topic>();
		
		for(Role r:this.roles)
			types.add(r.getType());
		
		return types;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Role> getRoles() {
		return this.roles;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Role> getRoles(Topic arg0) {
		
		Set<Role> roles = new HashSet<Role>();
		
		for(Role r:this.roles)
			if(r.getType().equals(arg0))
				roles.add(r);

		return roles;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		try {
			Topic type = getType();
			return "Association{Type:" + (type == null ? "null" : type.toString()) + ";Roles:" + getRoles().toString() + "}";
		} catch (Exception e) {
			return "Association{ID:" + getId() + "}";
		}
	}

}
