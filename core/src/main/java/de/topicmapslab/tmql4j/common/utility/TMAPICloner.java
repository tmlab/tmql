/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.common.utility;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;

public class TMAPICloner {

	private final TopicMap map;

	private Map<Topic, Topic> clones = new HashMap<Topic, Topic>();

	public TMAPICloner(TopicMap map) {
		this.map = map;
	}

	public TopicMap clone(Collection<?> sequence) throws Exception {
		Collection<Topic> topics = new HashSet<Topic>();
		Collection<Occurrence> occurrences = new HashSet<Occurrence>();
		Collection<Name> names = new HashSet<Name>();
		Collection<Variant> variants = new HashSet<Variant>();
		Collection<Role> roles = new HashSet<Role>();
		Collection<Association> associations = new HashSet<Association>();

		for (Object obj : sequence) {
			if (obj instanceof Topic) {
				topics.add((Topic) obj);
			} else if (obj instanceof Occurrence) {
				occurrences.add((Occurrence) obj);
			} else if (obj instanceof Name) {
				names.add((Name) obj);
			} else if (obj instanceof Variant) {
				variants.add((Variant) obj);
			} else if (obj instanceof Role) {
				roles.add((Role) obj);
			} else if (obj instanceof Association) {
				associations.add((Association) obj);
			}
		}

		for (Object obj : topics) {
			clone((Topic) obj, false);
		}

		for (Object obj : names) {
			Name name = (Name) obj;
			clone(clone(name.getParent()), name);
		}

		for (Object obj : occurrences) {
			Occurrence occurrence = (Occurrence) obj;
			clone(clone(occurrence.getParent()), occurrence);
		}

		for (Object obj : variants) {
			Variant variant = (Variant) obj;
			clone(clone(clone(variant.getParent().getParent()), variant
					.getParent()), variant);
		}

		for (Object obj : associations) {
			clone((Association) obj);
		}

		for (Object obj : roles) {
			Role role = (Role) obj;
			clone(clone(role.getParent()), role);
		}

		return this.map;
	}

	public void clone(TopicMap source) throws Exception {

		for (Topic topic : source.getTopics()) {
			clone(topic);
		}

		for (Association association : source.getAssociations()) {
			clone(association);
		}
	}

	public TopicMap clone(Construct construct) throws Exception {
		Collection<Construct> sequence = new HashSet<Construct>();
		sequence.add(construct);
		return clone(sequence);
	}

	/**
	 * TODO fix it WORKAROUND
	 * 
	 * @param topic
	 * @return
	 * @throws TMQLRuntimeException
	 */
	public Topic clone(Topic topic) throws Exception {
		// System.out.println(topic.getSubjectIdentifiers().isEmpty()?"":topic.getSubjectIdentifiers().iterator().next().toExternalForm());
		return clone(topic, false);
	}

	public Topic clone(Topic topic, boolean isScope) throws Exception {

		if (clones.containsKey(topic)) {
			return clones.get(topic);
		}

		Topic clone = map.createTopic();

		clones.put(topic, clone);

		for (Locator l : topic.getSubjectIdentifiers()) {
			clone.addSubjectIdentifier(map.createLocator(l.getReference()));
		}
		for (Locator l : topic.getSubjectLocators()) {
			clone.addSubjectLocator(map.createLocator(l.getReference()));
		}
		for (Locator l : topic.getItemIdentifiers()) {
			clone.addItemIdentifier(map.createLocator(l.getReference()));
		}

		if (!isScope) {

			for (Occurrence occurrence : topic.getOccurrences()) {
				clone(clone, occurrence);
			}

			for (Name name : topic.getNames()) {
				clone(clone, name);
			}
		}

		return clone;
	}

	public Occurrence clone(Topic topic, Occurrence occurrence)
			throws Exception {

		Topic type = clone(occurrence.getType());

		Occurrence clone = topic.createOccurrence(type, occurrence.getValue(),
				map.createLocator(occurrence.getDatatype().getReference()),
				new Topic[0]);

		for (Locator l : occurrence.getItemIdentifiers()) {
			clone.addItemIdentifier(map.createLocator(l.getReference()));
		}

		Topic reifier = occurrence.getReifier();
		if (reifier != null) {
			clone.setReifier(clone(reifier));
		}

		return clone;

	}

	public Name clone(Topic topic, Name name) throws Exception {

		Topic type = name.getType();
		if (type == null) {
			try {
				type = topic.getTopicMap().getTopicBySubjectIdentifier(
						topic.getTopicMap().createLocator(
								TmdmSubjectIdentifier.TMDM_DEFAULT_NAME_TYPE));
				if (type == null) {
					topic
							.getTopicMap()
							.createTopicBySubjectIdentifier(
									topic
											.getTopicMap()
											.createLocator(
													TmdmSubjectIdentifier.TMDM_DEFAULT_NAME_TYPE));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			type = clone(type);
		}

		Name clone = topic.createName(type, name.getValue(), new Topic[0]);

		for (Locator l : name.getItemIdentifiers()) {
			clone.addItemIdentifier(map.createLocator(l.getReference()));
		}

		Topic reifier = name.getReifier();
		if (reifier != null) {
			clone.setReifier(clone(reifier));
		}

		return clone;
	}

	public Variant clone(Name name, Variant variant) throws Exception {

		Variant clone = name.createVariant(variant.getValue(), map
				.createLocator(variant.getDatatype().getReference()),
				new Topic[0]);

		for (Locator l : variant.getItemIdentifiers()) {
			clone.addItemIdentifier(map.createLocator(l.getReference()));
		}

		Topic reifier = variant.getReifier();
		if (reifier != null) {
			clone.setReifier(clone(reifier));
		}

		return clone;

	}

	public Association clone(Association association) throws Exception {

		Topic type = clone(association.getType());

		Association clone = map.createAssociation(type, new Topic[0]);

		for (Locator l : association.getItemIdentifiers()) {
			clone.addItemIdentifier(map.createLocator(l.getReference()));
		}

		for (Topic scope : association.getScope()) {
			Topic scope_ = clone(scope, true);
			clone.addTheme(scope_);
		}

		for (Role role : association.getRoles()) {
			clone(clone, role);
		}

		Topic reifier = association.getReifier();
		if (reifier != null) {
			clone.setReifier(clone(reifier));
		}

		return clone;

	}

	public Role clone(Association association, Role role) throws Exception {

		Topic type = clone(role.getType());
		Topic player = clone(role.getPlayer());

		Role clone = association.createRole(type, player);

		for (Locator l : role.getItemIdentifiers()) {
			clone.addItemIdentifier(map.createLocator(l.getReference()));
		}

		Topic reifier = role.getReifier();
		if (reifier != null) {
			clone.setReifier(clone(reifier));
		}

		return clone;
	}

	public TopicMap getMap() {
		return map;
	}

}
