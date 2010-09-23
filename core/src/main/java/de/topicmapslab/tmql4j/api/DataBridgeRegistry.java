/* 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.api;

import de.topicmapslab.tmql4j.api.impl.tmapi.TMAPIDataBridge;
import de.topicmapslab.tmql4j.api.model.IDataBridge;
import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;

/**
 * Class definition of a registry handles the {@link IDataBridge}
 * implementation.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class DataBridgeRegistry {

	/**
	 * singleton instance
	 */
	private static DataBridgeRegistry instance = null;
	/**
	 * {@link IDataBridge} implementation class
	 */
	private Class<? extends IDataBridge> dataBridgeClass;

	/**
	 * private and hidden constructor
	 */
	private DataBridgeRegistry() {
		dataBridgeClass = TMAPIDataBridge.class;
	}

	/**
	 * Method returns the internal reference to the {@link IDataBridge}
	 * implementation class
	 * 
	 * @return the dataBridgeClass the class object
	 */
	public Class<? extends IDataBridge> getDataBridgeClass() {
		return dataBridgeClass;
	}

	/**
	 * Method set the internal reference to the {@link IDataBridge}
	 * implementation class
	 * 
	 * @param dataBridgeClass
	 *            the dataBridgeClass to set
	 */
	public void setDataBridgeClass(Class<? extends IDataBridge> dataBridgeClass) {
		this.dataBridgeClass = dataBridgeClass;
	}

	/**
	 * method gets access to the singleton registry instance
	 * 
	 * @return the singleton instance
	 */
	public static DataBridgeRegistry getRegistry() {
		if (instance == null) {
			instance = new DataBridgeRegistry();
		}
		return instance;
	}

	/**
	 * Method create a new instance of the {@link IDataBridge} by using java
	 * reflection.
	 * 
	 * @return the new instance of {@link IDataBridge}
	 * @throws TMQLRuntimeException
	 *             thrown is instance can not be instantiate
	 */
	public IDataBridge newDataBridgeInstance() throws TMQLRuntimeException {
		try {
			return getDataBridgeClass().newInstance();
		} catch (InstantiationException e) {
			throw new TMQLRuntimeException("Data bridge cannot be instantiate",
					e);
		} catch (IllegalAccessException e) {
			throw new TMQLRuntimeException("Data bridge cannot be instantiate",
					e);
		}
	}

}
