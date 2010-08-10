package de.topicmapslab.tmql4j.draft2010.interpreter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.AssociationPattern;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.model.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.interpreter.model.context.IVariableSet;
import de.topicmapslab.tmql4j.lexer.token.BracketRoundOpen;
import de.topicmapslab.tmql4j.lexer.token.Element;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisPlayersMoveForward;

/**
 * The interpreter implementation of the production 'association-pattern'.
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
public class AssociationPatternInterpeter extends
		ExpressionInterpreterImpl<AssociationPattern> {

	/**
	 * constructor
	 * @param ex the expression to interpret
	 */
	public AssociationPatternInterpeter(AssociationPattern ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {

		/*
		 * get queried topic map
		 */
		TopicMap topicMap = (TopicMap) runtime.getRuntimeContext()
				.peek().getValue(VariableNames.CURRENT_MAP);

		/*
		 * get association type by identifier if it isn't *
		 */
		Topic associationType = null;
		if (getTmqlTokens().get(0).equals(Element.class)) {
			final String assocIdentifier = getTokens().get(0);
			try {
				Construct construct = runtime.getDataBridge()
						.getConstructResolver().getConstructByIdentifier(
								runtime, assocIdentifier, topicMap);
				if (construct instanceof Topic) {
					associationType = (Topic) construct;
				}
			} catch (Exception e) {
				throw new TMQLRuntimeException(e);
			}
		}

		/*
		 * read left-hand role by identifier if exists and isn't *
		 */
		Topic roleType = null;
		int index = getTmqlTokens().indexOf(BracketRoundOpen.class) + 1;
		if (getTmqlTokens().get(index).equals(Element.class)) {
			String roleIdentifier = getTokens().get(index);
			try {
				Construct construct = runtime.getDataBridge()
						.getConstructResolver().getConstructByIdentifier(
								runtime, roleIdentifier, topicMap);
				if (construct instanceof Topic) {
					roleType = (Topic) construct;
				}
			} catch (Exception e) {
				throw new TMQLRuntimeException(e);
			}
		}

		/*
		 * read right-hand role by identifier if exists and isn't *
		 */
		Topic otherRoleType = null;
		index = getTmqlTokens().indexOf(ShortcutAxisPlayersMoveForward.class) + 1;
		if (getTmqlTokens().get(index).equals(Element.class)) {
			String otherRoleIdentifier = getTokens().get(index);
			try {
				Construct construct = runtime.getDataBridge()
						.getConstructResolver().getConstructByIdentifier(
								runtime, otherRoleIdentifier, topicMap);
				if (construct instanceof Topic) {
					otherRoleType = (Topic) construct;
				}
			} catch (Exception e) {
				throw new TMQLRuntimeException(e);
			}
		}

		/*
		 * extract context from variable layer
		 */
		IVariableSet set = runtime.getRuntimeContext().peek();
		if (!set.contains(VariableNames.ITERATED_BINDINGS)) {
			throw new TMQLRuntimeException("Missing navigation context.");
		}
		QueryMatches context = (QueryMatches) set
				.getValue(VariableNames.ITERATED_BINDINGS);

		List<Topic> topics = new LinkedList<Topic>();
		for (Object o : context.getPossibleValuesForVariable()) {
			if (o instanceof Topic) {
				topics.addAll(getTraversalPlayers((Topic) o, associationType,
						roleType, otherRoleType));
			}
		}

		/*
		 * convert results to query-matches and store at the variable layer
		 */
		QueryMatches matches = new QueryMatches(runtime);
		matches.convertToTuples(topics);
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, matches);

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
	private Collection<Topic> getTraversalPlayers(final Topic topic,
			final Topic associationType, final Topic roleType,
			final Topic otherRoleType) {
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
			associationTypes = TypeHierarchyUtils.getSubtypes(associationType,
					true);
			associationTypes.add(associationType);
		}

		for (Role role : roles) {
			Association association = role.getParent();
			/*
			 * filter associations by type
			 */
			if (associationTypes == null
					|| associationTypes.contains(association.getType())) {
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
