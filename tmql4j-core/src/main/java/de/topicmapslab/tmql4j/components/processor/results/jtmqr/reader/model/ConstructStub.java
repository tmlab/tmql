package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.util.HashSet;
import java.util.Set;

import org.tmapi.core.Locator;

import de.topicmapslab.majortom.core.LocatorImpl;

/**
 * abstract construct stub
 * @author Christian Haß
 *
 */
public abstract class ConstructStub {

	/**
	 * set of item identifier
	 */
	protected Set<Locator> itemIdentifier;
	
	/**
	 * constructor
	 */
	protected ConstructStub() {
		this.itemIdentifier = new HashSet<Locator>();
	}
	
	/**
	 * adds an item identifier to the construct
	 * @param iri - the iri as string
	 */
	protected void _addItemIdentifier(String iri){
		Locator l = new LocatorImpl(iri);
		this.itemIdentifier.add(l);
	}
	
	/**
	 * adds a set of item identifier
	 * @param sis - set of item identifier as strings
	 */
	protected void _setItemIdentifiers(Set<String> sis){
		for(String si:sis)
			_addItemIdentifier(si);
	}
	
}
