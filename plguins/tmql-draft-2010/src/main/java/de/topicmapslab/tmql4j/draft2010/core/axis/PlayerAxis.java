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
import de.topicmapslab.tmql4j.draft2010.tokens.Player;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class PlayerAxis implements IAxis {

	
	public IToken getIdentifier() {
		return new Player();
	}

	
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Topic> players = HashUtil.getHashSet();
		if (source instanceof Association) {
			Association association = (Association) source;
			Set<Topic> types = HashUtil.getHashSet();
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
