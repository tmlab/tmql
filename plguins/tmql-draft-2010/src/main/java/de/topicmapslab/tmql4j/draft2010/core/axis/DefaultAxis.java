package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Name;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class DefaultAxis implements IAxis {

	@Override
	public IToken getIdentifier() {
		return null;
	}

	@Override
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Object> results = HashUtil.getHashSet();

		if (source instanceof TopicMap) {
			results.addAll(new TopicAxis().navigate(source, type));
			results.addAll(new AssociationAxis().navigate(source, type));
		} else if (source instanceof Topic) {
			results.addAll(new NameAxis().navigate(source, type));
			results.addAll(new OccurrenceAxis().navigate(source, type));
			results.addAll(new RoleAxis().navigate(source, type));
		} else if (source instanceof Name) {
			results.addAll(new VariantAxis().navigate(source, type));
		} else if (source instanceof Association) {
			results.addAll(new RoleAxis().navigate(source, type));
		} else if (source instanceof Role) {
			results.addAll(new PlayerAxis().navigate(source, type));
		}

		return results;
	}

}
