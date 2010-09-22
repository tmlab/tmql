package de.topicmapslab.tmql4j.training;

import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.common.utility.HashUtil;

public class TopologyUtils {

	static TopicMap map;
	static final String baseIRI = "http://psi.ontopia.net/";

	static Locator locator(String ref) {
		if (ref.startsWith("http://")) {
			return map.createLocator(ref);
		} else {
			return map.createLocator(baseIRI + ref);
		}
	}

	static Topic si(String ref) {
		return map.getTopicBySubjectIdentifier(locator(ref));
	}

	static Topic sl(String ref) {
		return map.getTopicBySubjectLocator(locator(ref));
	}

	static Topic topicBySI(String ref) {
		return map.createTopicBySubjectIdentifier(locator(ref));
	}

	static Topic topicBySL(String ref) {
		return map.createTopicBySubjectLocator(locator(ref));
	}

	static Name name(Topic parent, String value, String... themes) {
		Set<Topic> topics = HashUtil.getHashSet();
		for (String theme : themes) {
			Topic t = si(theme);
			if (t == null) {
				t = topicBySI(theme);
			}
			topics.add(t);
		}
		return parent.createName(value, topics);
	}

	static Name nameT(Topic parent, String typeRef, String value,
			String... themes) {
		Topic type = si(typeRef);
		if (type == null) {
			type = topicBySI(typeRef);
		}
		Set<Topic> topics = HashUtil.getHashSet();
		for (String theme : themes) {
			Topic t = si(theme);
			if (t == null) {
				t = topicBySI(theme);
			}
			topics.add(t);
		}
		return parent.createName(type, value, topics);
	}

	static Occurrence occurrence(Topic parent, String typeRef, String value,
			String datatype, String... themes) {
		Topic type = si(typeRef);
		if (type == null) {
			type = topicBySI(typeRef);
		}
		Locator d = map.createLocator(datatype);
		Set<Topic> topics = HashUtil.getHashSet();
		for (String theme : themes) {
			Topic t = si(theme);
			if (t == null) {
				t = topicBySI(theme);
			}
			topics.add(t);
		}
		return parent.createOccurrence(type, value, d, topics);
	}

	static Variant variant(Name parent, String value, String datatype,
			String... themes) {
		Locator d = map.createLocator(datatype);
		Set<Topic> topics = HashUtil.getHashSet();
		for (String theme : themes) {
			Topic t = si(theme);
			if (t == null) {
				t = topicBySI(theme);
			}
			topics.add(t);
		}
		return parent.createVariant(value, d, topics);
	}

	static Association association(String typeRef, String... themes) {
		Topic type = si(typeRef);
		if (type == null) {
			type = topicBySI(typeRef);
		}
		Set<Topic> topics = HashUtil.getHashSet();
		for (String theme : themes) {
			Topic t = si(theme);
			if (t == null) {
				t = topicBySI(theme);
			}
			topics.add(t);
		}
		return map.createAssociation(type, topics);
	}

	static void ako(Topic supertype, Topic subtype) {
		Association ass = association("http://psi.topicmaps.org/iso13250/model/supertype-subtype");
		Topic type = si("http://psi.topicmaps.org/iso13250/model/supertype");
		if (type == null) {
			type = topicBySI("http://psi.topicmaps.org/iso13250/model/supertype");
		}
		ass.createRole(type, supertype);
		type = si("http://psi.topicmaps.org/iso13250/model/subtype");
		if (type == null) {
			type = topicBySI("http://psi.topicmaps.org/iso13250/model/subtype");
		}
		ass.createRole(type, subtype);
	}
}
