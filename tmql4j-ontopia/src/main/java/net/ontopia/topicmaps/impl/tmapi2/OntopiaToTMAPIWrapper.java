/**
 * TMQL4J Plugin for Ontopia
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * Author: Sven Krosse
 * 
 */
package net.ontopia.topicmaps.impl.tmapi2;

import java.util.List;

import net.ontopia.topicmaps.core.AssociationIF;
import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.topicmaps.core.VariantNameIF;
import net.ontopia.topicmaps.utils.TopicStringifiers;
import net.ontopia.utils.StringifierIF;

import org.tmapi.core.Association;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;

public class OntopiaToTMAPIWrapper {

	public static TMObjectIF toTMObjectIF(Object obj, String tmid) {
		if (obj instanceof Topic) {
			return ((TopicImpl) obj).getWrapped();
		} else if (obj instanceof Name) {
			return ((NameImpl) obj).getWrapped();
		} else if (obj instanceof Association) {
			return ((AssociationImpl) obj).getWrapped();
		} else if (obj instanceof Role) {
			return ((RoleImpl) obj).getWrapped();
		} else if (obj instanceof Occurrence) {
			return ((OccurrenceImpl) obj).getWrapped();
		} else if (obj instanceof Variant) {
			return ((VariantImpl) obj).getWrapped();
		} else if (obj instanceof TopicMap) {
			return ((TopicMapImpl) obj).getWrapped();
		}
		return null;
	}

	public static String toTMObjectIF(ITMQLRuntime runtime, List<?> seq, String tmid) {
		StringifierIF str = TopicStringifiers.getDefaultStringifier();
		StringBuilder sb = new StringBuilder();
		for (Object object : seq) {
			TMObjectIF obj = toTMObjectIF(object, tmid);
			if (obj == null) {
				sb.append((sb.toString().length() != 0 ? "<br />" : "") + object.toString());
			} else if (obj instanceof TopicIF || obj instanceof AssociationIF) {
				StringBuilder builder = new StringBuilder();
				builder.append("<a href=\"../../models/");
				builder.append(obj instanceof TopicIF ? "topic_" : "association_");
				builder.append("complete.jsp?tm=");
				builder.append(tmid);
				builder.append("&id=");
				builder.append(obj.getObjectId());
				builder.append("\">");
				builder.append(obj instanceof TopicIF ? str.toString((TopicIF) obj) : "Association " + obj.getObjectId());
				builder.append("</a>");
				sb.append((sb.toString().length() != 0 ? "<br />" : "") + builder.toString());
			} else if (obj instanceof TopicNameIF) {
				sb.append((sb.toString().length() != 0 ? "<br />" : "") + ((TopicNameIF) obj).getValue());
			} else if (obj instanceof VariantNameIF) {
				sb.append((sb.toString().length() != 0 ? "<br />" : "") + ((VariantNameIF) obj).getValue());
			} else if (obj instanceof OccurrenceIF) {
				sb.append((sb.toString().length() != 0 ? "<br />" : "") + toTMObjectIF((OccurrenceIF) obj, tmid));
			} else {
				sb.append((sb.toString().length() != 0 ? "<br />" : "") + object.toString());
			}
		}
		return sb.toString();
	}

	public static String toTMObjectIF(ITMQLRuntime runtime, OccurrenceIF occurrenceIF, String tmid) {
		StringifierIF str = TopicStringifiers.getDefaultStringifier();
		StringBuilder sb = new StringBuilder();

		StringBuilder builder = new StringBuilder();
		builder.append("<a href=\"../../models/");
		builder.append("topic_");
		builder.append("complete.jsp?tm=");
		builder.append(tmid);
		builder.append("&id=");
		builder.append(occurrenceIF.getType().getObjectId());
		builder.append("\"><b>");
		builder.append(str.toString((TopicIF) occurrenceIF.getType()));
		builder.append("</a>:</b> ");
		builder.append(occurrenceIF.getValue());
		sb.append((sb.toString().length() != 0 ? "<br />" : "") + builder.toString());

		return sb.toString();
	}
}
