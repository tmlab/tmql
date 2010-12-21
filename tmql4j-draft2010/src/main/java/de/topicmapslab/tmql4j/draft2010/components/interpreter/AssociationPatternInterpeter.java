package de.topicmapslab.tmql4j.draft2010.components.interpreter;

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
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Arrow;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Element;
import de.topicmapslab.tmql4j.draft2010.grammar.productions.AssociationPattern;
import de.topicmapslab.tmql4j.draft2010.util.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
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
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (context.getContextBindings() == null) {
			logger.warn("Missing context to execute conjunction!");
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
		index = getTmqlTokens().indexOf(Arrow.class) + 1;
		if (getTmqlTokens().get(index).equals(Element.class)) {
			final Construct construct = runtime.getConstructResolver().getConstructByIdentifier(context, LiteralUtils.asString(getTokens().get(index)));
			if (!(construct instanceof Topic)) {
				logger.warn("Association type should be a topic but was '" + construct + "'.");
				return QueryMatches.emptyMatches();
			}
			otherRoleType = (Topic) construct;
		}
		
		List<Topic> topics = new LinkedList<Topic>();
		for (Object o : context.getContextBindings().getPossibleValuesForVariable()) {
			if (o instanceof Topic) {
				topics.addAll(getTraversalPlayers((Topic) o, associationType, roleType, otherRoleType));
			}
		}
		return QueryMatches.asQueryMatchNS(runtime, topics.toArray());
	}

	/**
	 * Method extract the traversal players of the given topic. The
	 * associationType represents the type of considered association items. The
	 * roleType represents the type of the role the given topic has to play, if
	 * it is <code>null</code> the roleType will be ignored. The otherRoleType
	 * is the role type the traversal player has to play, if it is
	 * <code>null</code> it will be ignored.
	 * 
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
	public static Collection<Topic> getTraversalPlayers(final Topic topic, final Topic associationType, final Topic roleType, final Topic otherRoleType) {
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
		List<Topic> associationTypes = null;
		if (associationType != null) {
			associationTypes = TypeHierarchyUtils.getSubtypes(associationType, true);
			associationTypes.add(associationType);
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
