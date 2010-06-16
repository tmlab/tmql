package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class RoleAxis implements IAxis {

	@Override
	public IToken getIdentifier() {
		return new de.topicmapslab.tmql4j.draft2010.tokens.Role();
	}

	@Override
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Role> roles = HashUtil.getHashSet();

		if (source instanceof Topic) {
			Topic topic = (Topic) source;
			if (type != null) {
				Set<Topic> types = TypeHierarchyUtils.getSupertypes(type, true);
				types.add(type);
				for (Role role : topic.getRolesPlayed()) {
					if (types.contains(role.getType())) {
						roles.add(role);
					}
				}
			} else {
				roles.addAll(topic.getRolesPlayed());
			}
		} else if (source instanceof Association) {
			Association association = (Association) source;
			if (type != null) {
				Set<Topic> types = TypeHierarchyUtils.getSupertypes(type, true);
				types.add(type);
				for (Role role : association.getRoles()) {
					if (types.contains(role.getType())) {
						roles.add(role);
					}
				}
			} else {
				roles.addAll(association.getRoles());
			}

		}

		return roles;
	}

}
