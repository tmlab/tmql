package de.topicmapslab.tmql4j.draft2010.internal;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.preprocessing.model.ICanonizer;

public class PathCanonizer implements ICanonizer {

	private IQuery query;

	public PathCanonizer(IQuery query) {
		this.query = query;
	}

	@Override
	public void canonize() throws TMQLRuntimeException {
		// VOID
	}

	@Override
	public IQuery getCanonizedQuery() {
		return query;
	}

}
