package de.topicmapslab.tmql4j.draft2010.components.navigation.axis;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Player;
import de.topicmapslab.tmql4j.draft2010.util.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * The player axis returns the player of a role
 * @author Sven Krosse
 *
 */
public class PlayerAxis implements IAxis {

	/**
	 * {@inheritDoc}
	 */
	public IToken getIdentifier() {
		return new Player();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Topic> players = HashUtil.getHashSet();
		if (source instanceof Association) {
			Association association = (Association) source;
			List<Topic> types = HashUtil.getList();
			if (type != null) {
				types = TypeHierarchyUtils.getSupertypes(type, true);
				types.add(type);
			}
			for (Role role : association.getRoles()) {
				Topic player = role.getPlayer();
				if (type != null) {
					for (Topic t : player.getTypes()) {
						if (types.contains(t)) {
							players.add(player);
							break;
						}
					}
				} else {
					players.add(player);
				}
			}
		}
		return players;
	}

}
