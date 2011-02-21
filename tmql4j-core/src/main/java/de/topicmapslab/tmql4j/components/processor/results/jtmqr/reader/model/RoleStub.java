package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Locator;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

public class RoleStub extends ReifieableStub implements Role {

	private Association parent;
	private Topic player;
	
	protected RoleStub(Association parent) {
		this.parent = parent;
	}
	
	protected void _setPlayer(Topic player){
		this.player = player;
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
	public Association getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topic getPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPlayer(Topic arg0) {
		// TODO Auto-generated method stub
		
	}

}
