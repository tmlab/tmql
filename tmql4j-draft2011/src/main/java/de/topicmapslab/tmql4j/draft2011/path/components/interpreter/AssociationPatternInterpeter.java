package de.topicmapslab.tmql4j.draft2011.path.components.interpreter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.SubtypesAxis;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Element;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisPlayers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.AssociationPattern;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * The interpreter implementation of the production 'association-pattern'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class AssociationPatternInterpeter extends ExpressionInterpreterImpl<AssociationPattern> {

	/**
	 * the logger
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public AssociationPatternInterpeter(AssociationPattern ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (context.getCurrentNode() == null) {
			logger.warn("Missing context to execute association pattern!");
			return QueryMatches.emptyMatches();
		}
		/*
		 * get association type by identifier if it isn't *
		 */
		Topic associationType = null;
		if (getTmqlTokens().get(0).equals(Element.class)) {
			final Construct construct = runtime.getConstructResolver().getConstructByIdentifier(context, LiteralUtils.asString(getTokens().get(0)));
			if (!(construct instanceof Topic)) {
				logger.warn("Association type should be a topic but was '" + construct + "'.");
				return QueryMatches.emptyMatches();
			}
			associationType = (Topic) construct;
		}

		/*
		 * read left-hand role by identifier if exists and isn't *
		 */
		Topic roleType = null;
		int index = getTmqlTokens().indexOf(BracketRoundOpen.class) + 1;
		if (getTmqlTokens().get(index).equals(Element.class)) {
			final Construct construct = runtime.getConstructResolver().getConstructByIdentifier(context, LiteralUtils.asString(getTokens().get(index)));
			if (!(construct instanceof Topic)) {
				logger.warn("Association type should be a topic but was '" + construct + "'.");
				return QueryMatches.emptyMatches();
			}
			roleType = (Topic) construct;
		}

		/*
		 * read right-hand role by identifier if exists and isn't *
		 */
		Topic otherRoleType = null;
		index = getTmqlTokens().indexOf(ShortcutAxisPlayers.class) + 1;
		if (getTmqlTokens().get(index).equals(Element.class)) {
			final Construct construct = runtime.getConstructResolver().getConstructByIdentifier(context, LiteralUtils.asString(getTokens().get(index)));
			if (!(construct instanceof Topic)) {
				logger.warn("Association type should be a topic but was '" + construct + "'.");
				return QueryMatches.emptyMatches();
			}
			otherRoleType = (Topic) construct;
		}

		List<Topic> topics = new LinkedList<Topic>();
		topics.addAll(getTraversalPlayers(context, (Topic) context.getCurrentNode(), associationType, roleType, otherRoleType));
		return QueryMatches.asQueryMatchNS(runtime, topics.toArray());
	}

	/**
	 * Method extract the traversal players of the given topic. The associationType represents the type of considered
	 * association items. The roleType represents the type of the role the given topic has to play, if it is
	 * <code>null</code> the roleType will be ignored. The otherRoleType is the role type the traversal player has to
	 * play, if it is <code>null</code> it will be ignored.
	 * 
	 * @param context
	 *            the context
	 * @param topic
	 *            the topic
	 * @param associationType
	 *            the type of considered associations or <code>null</code>
	 * @param roleType
	 *            the role type of the given topic or <code>null</code>
	 * @param otherRoleType
	 *            the role type of the traversal player or <code>null</code>
	 * @return a collection of all traversal players
	 */
	@SuppressWarnings("unchecked")
	public static Collection<Topic> getTraversalPlayers(final IContext context, final Topic topic, final Topic associationType, final Topic roleType, final Topic otherRoleType) {
		List<Topic> topics = new LinkedList<Topic>();

		/*
		 * extract played roles of the given topic
		 */
		Set<Role> roles;
		if (roleType == null) {
			roles = topic.getRolesPlayed();
		} else {
			roles = topic.getRolesPlayed(roleType);
		}

		/*
		 * extract all subtypes of the given association type if it is not null
		 */
		Set<Topic> associationTypes = null;
		if (associationType != null) {
			associationTypes = HashUtil.getHashSet();
			associationTypes.add(associationType);
			if (context.isTransitive()) {
				SubtypesAxis axis = new SubtypesAxis();
				associationTypes.addAll((Collection<Topic>) axis.navigate(context, associationType, null));
			}
		}

		for (Role role : roles) {
			Association association = role.getParent();
			/*
			 * filter associations by type
			 */
			if (associationTypes == null || associationTypes.contains(association.getType())) {
				/*
				 * extract roles of traversal players
				 */
				Set<Role> otherRoles;
				if (otherRoleType == null) {
					otherRoles = association.getRoles();
				} else {
					otherRoles = association.getRoles(otherRoleType);
				}

				/*
				 * extract players
				 */
				for (Role otherRole : otherRoles) {
					if (!otherRole.equals(role)) {
						topics.add(otherRole.getPlayer());
					}
				}
			}
		}

		return topics;
	}

}
