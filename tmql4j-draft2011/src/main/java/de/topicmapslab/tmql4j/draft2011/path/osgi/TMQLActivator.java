/**
 * 
 */
package de.topicmapslab.tmql4j.draft2011.path.osgi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topicmapslab.tmql4j.extension.IExtensionPoint;
import de.topicmapslab.tmql4j.query.IQueryProcessor;

/**
 * The activator is used to start the OSGi bundle. If the TMQL4J bundle is started, every 
 * bundle will be checked for extension services which will be added to a registry.
 * 
 * 
 * @author Hannes Niederhausen
 *
 */
public class TMQLActivator implements BundleActivator {

	private static final Logger logger = LoggerFactory.getLogger(TMQLActivator.class);
	private static TMQLActivator plugin;
	
	private BundleContext context;
	
	@Override
	public void start(BundleContext context) throws Exception {
		TMQLActivator.plugin = this;
		this.context = context;
	}


	public List<IExtensionPoint> getExtensionPoints() {
		try {
			List<IExtensionPoint> result = new ArrayList<IExtensionPoint>();
			ServiceReference[] refs = context.getServiceReferences(
					IExtensionPoint.class.getName(), null);
			if (refs == null)
				return Collections.emptyList();
			for (ServiceReference ref : refs) {
				result.add((IExtensionPoint) context.getService(ref));
			}
			return result;
		} catch (InvalidSyntaxException e) {
			// only logging 'cause should not happen
			logger.error("Tried loading extension points: ", e);
		}
		return Collections.emptyList();
	}
	
	public List<IQueryProcessor> getQueryProcessors() {
		try {
			List<IQueryProcessor> result = new ArrayList<IQueryProcessor>();
			ServiceReference[] refs = context.getServiceReferences(
					IQueryProcessor.class.getName(), null);
			if (refs == null)
				return Collections.emptyList();
			for (ServiceReference ref : refs) {
				result.add((IQueryProcessor) context.getService(ref));
			}
			return result;
		} catch (InvalidSyntaxException e) {
			// only logging 'cause should not happen
			logger.error("Tried loading query processors: ", e);
		}
		return Collections.emptyList();
	}

	public static TMQLActivator getDefault() {
		return plugin;
	}


	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
