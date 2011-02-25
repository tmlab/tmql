package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Locator;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

/**
 * role implementation for jtmqr result set
 * 
 * @author Christian Haß
 * 
 */
public class RoleStub extends ReifieableStub implements Role {

	private final Association parent;
	private Topic player;

	/**
	 * constructor
	 * 
	 * @param parent
	 *            - the parent association or <code>null</code>
	 */
	protected RoleStub(Association parent) {
		this.parent = parent;
	}

	/**
	 * sets the player
	 * 
	 * @param player
	 *            - the player
	 */
	protected void _setPlayer(Topic player) {
		this.player = player;
	}

	// --[ TMAPI methods
	// ]-------------------------------------------------------

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
	public Association getParent() {
		return this.parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Topic getPlayer() {
		return this.player;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlayer(Topic arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		try {
			Topic type = getType();
			Topic player = getPlayer();
			return "Association-Role{Type:" + (type == null ? "null" : type.toString()) + ";Player:" + (player == null ? "null" : player.toString() + "}");
		} catch (Exception e) {
			return "Association-Role{Id:" + getId() + "}";
		}
	}

}
