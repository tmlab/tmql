/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.util;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * @author Sven Krosse
 * 
 */
public class ConstructCopy {

	/**
	 * Utility method to copy all construct into the topic map
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param constructs
	 *            the constructs to copy
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public static final void copyIn(TopicMap topicMap, Collection<Construct> constructs) throws TMQLRuntimeException {
		try {
			for (Construct c : constructs) {
				/*
				 * is a construct
				 */
				if (c instanceof Topic) {
					Topic source = (Topic) c;
					Topic copy = createTopicStub(topicMap, source);
					/*
					 * copy subject-identifiers
					 */
					for (Locator l : source.getSubjectIdentifiers()) {
						copy.addSubjectIdentifier(topicMap.createLocator(l.getReference()));
					}
					/*
					 * copy subject-locators
					 */
					for (Locator l : source.getSubjectLocators()) {
						copy.addSubjectLocator(topicMap.createLocator(l.getReference()));
					}
					/*
					 * copy item-identifiers
					 */
					for (Locator l : source.getItemIdentifiers()) {
						copy.addItemIdentifier(topicMap.createLocator(l.getReference()));
					}
					/*
					 * copy types
					 */
					for (Topic type : createTopicStubs(topicMap, source.getTypes())) {
						copy.addType(type);
					}
					/*
					 * copy names
					 */
					for (Name name : source.getNames()) {
						Name nameCopy = copy.createName(createTopicStub(topicMap, name.getType()), name.getValue(), createTopicStubs(topicMap, name.getScope()));
						for (Variant variant : name.getVariants()) {
							nameCopy.createVariant(variant.getValue(), topicMap.createLocator(variant.getDatatype().getReference()), createTopicStubs(topicMap, variant.getScope()));
						}
					}
					/*
					 * copy occurrences
					 */
					for (Occurrence occurrence : source.getOccurrences()) {
						copy.createOccurrence(createTopicStub(topicMap, occurrence.getType()), occurrence.getValue(), topicMap.createLocator(occurrence.getDatatype().getReference()),
								createTopicStubs(topicMap, occurrence.getScope()));
					}
				}
				/*
				 * is a construct
				 */
				else if (c instanceof Association) {
					Association source = (Association) c;
					Association copy = topicMap.createAssociation(createTopicStub(topicMap, source.getType()), createTopicStubs(topicMap, source.getScope()));
					/*
					 * copy roles
					 */
					for (Role role : source.getRoles()) {
						copy.createRole(createTopicStub(topicMap, role.getType()), createTopicStub(topicMap, role.getPlayer()));
					}
				}
			}
		} catch (Exception e) {
			throw new TMQLRuntimeException("At least one consturct cannot be copied to the topic map!", e);
		}
	}

	/**
	 * Utility method to create topic stubs for a given topic
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param source
	 *            the topic
	 * @return the topic stub
	 */
	private static Topic createTopicStub(TopicMap topicMap, Topic source) {
		/*
		 * check subject-identifier
		 */
		Set<Locator> locators = source.getSubjectIdentifiers();
		if (!locators.isEmpty()) {
			return topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(locators.iterator().next().getReference()));
		}
		/*
		 * check subject-locator
		 */
		locators = source.getSubjectLocators();
		if (!locators.isEmpty()) {
			return topicMap.createTopicBySubjectLocator(topicMap.createLocator(locators.iterator().next().getReference()));
		}
		/*
		 * check item-identifier
		 */
		locators = source.getItemIdentifiers();
		if (!locators.isEmpty()) {
			return topicMap.createTopicByItemIdentifier(topicMap.createLocator(locators.iterator().next().getReference()));
		}
		return topicMap.createTopic();
	}

	/**
	 * Utility method to create topic stubs for each topic contained in the
	 * collection
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param sources
	 *            the topic collection
	 * @return the collection of topic stubs
	 */
	private static Collection<Topic> createTopicStubs(TopicMap topicMap, Collection<Topic> sources) {
		Collection<Topic> topics = HashUtil.getHashSet();
		for (Topic source : sources) {
			topics.add(createTopicStub(topicMap, source));
		}
		return topics;
	}

	// static class LocatorStruct{
	// public Locator locator;
	// public int type;
	//
	// private LocatorStruct(Locator loc, int type){
	// this.locator = loc;
	// this.type = type;
	// }
	//
	// public static LocatorStruct createSubjectIdentifier(Locator loc){
	// return new LocatorStruct(loc, SUBJECT_IDENTIFIER);
	// }
	//
	// public static LocatorStruct createSubjectLocator(Locator loc){
	// return new LocatorStruct(loc, SUBJECT_LOCATOR);
	// }
	//
	// public static LocatorStruct createItemIdentifier(Locator loc){
	// return new LocatorStruct(loc, ITEM_IDENTIFIER);
	// }
	//
	// boolean isSubjectIdentifier(){
	// return type == SUBJECT_IDENTIFIER;
	// }
	//
	// boolean isSubjectLocator(){
	// return type == SUBJECT_LOCATOR;
	// }
	//
	// boolean isItemIdentifier(){
	// return type == ITEM_IDENTIFIER;
	// }
	//
	// public static final int SUBJECT_IDENTIFIER = 1;
	// public static final int SUBJECT_LOCATOR = 2;
	// public static final int ITEM_IDENTIFIER = 3;
	// }

}
