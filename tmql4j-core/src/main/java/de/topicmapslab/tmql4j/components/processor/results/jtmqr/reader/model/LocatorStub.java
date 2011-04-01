/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.net.URI;
import java.net.URISyntaxException;

import org.tmapi.core.Locator;
import org.tmapi.core.MalformedIRIException;

/**
 * @author Sven Krosse
 * 
 */
public class LocatorStub implements Locator {

	private final String reference;

	/**
	 * constructor
	 * 
	 * @param value
	 *            the value
	 */
	public LocatorStub(final String value) {
		this.reference = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getReference() {
		return reference;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Locator resolve(String arg0) throws MalformedIRIException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toExternalForm() {
		try {
			return new URI(reference).toASCIIString();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof LocatorStub) {
			return getReference().equalsIgnoreCase(((LocatorStub) obj).getReference());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getReference().hashCode();
	}

}
