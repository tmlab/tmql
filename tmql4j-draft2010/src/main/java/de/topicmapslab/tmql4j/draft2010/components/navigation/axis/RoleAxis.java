package de.topicmapslab.tmql4j.draft2010.components.navigation.axis;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.util.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * The role axis returns all roles of a topic
 * @author Sven Krosse
 *
 */
public class RoleAxis implements IAxis {

	/**
	 * {@inheritDoc}
	 */
	public IToken getIdentifier() {
		return new de.topicmapslab.tmql4j.draft2010.grammar.lexical.Role();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Role> roles = HashUtil.getHashSet();

		if (source instanceof Topic) {
			Topic topic = (Topic) source;
			if (type != null) {
				List<Topic> types = TypeHierarchyUtils.getSupertypes(type, true);
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
				List<Topic> types = TypeHierarchyUtils.getSupertypes(type, true);
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
