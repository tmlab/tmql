package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.util.HashSet;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Locator;

/**
 * abstract construct stub
 * 
 * @author Christian Haß
 * 
 */
public abstract class ConstructStub implements Construct {

	/**
	 * set of item identifier
	 */
	protected Set<Locator> itemIdentifier;
	private String id;

	/**
	 * constructor
	 */
	protected ConstructStub() {
		this.itemIdentifier = new HashSet<Locator>();
	}

	/**
	 * adds an item identifier to the construct
	 * 
	 * @param iri
	 *            - the iri as string
	 */
	protected void _addItemIdentifier(String iri) {
		Locator l = new LocatorStub(iri);
		this.itemIdentifier.add(l);
	}

	/**
	 * adds a set of item identifier
	 * 
	 * @param sis
	 *            - set of item identifier as strings
	 */
	protected void _setItemIdentifiers(Set<String> sis) {
		for (String si : sis) {
			_addItemIdentifier(si);
		}
	}

	protected void _setId(String id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}

}
