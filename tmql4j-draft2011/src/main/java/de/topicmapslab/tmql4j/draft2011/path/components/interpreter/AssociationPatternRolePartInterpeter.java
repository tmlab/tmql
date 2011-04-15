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
import de.topicmapslab.tmql4j.components.interpreter.IExpressionInterpreter;
import de.topicmapslab.tmql4j.components.processor.core.Context;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis.SubtypesAxis;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.AssociationPatternRolePart;
import de.topicmapslab.tmql4j.draft2011.path.grammar.productions.FilterPostfix;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * The interpreter implementation of the production 'association-pattern-role-part'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class AssociationPatternRolePartInterpeter extends ExpressionInterpreterImpl<AssociationPatternRolePart> {

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
	public AssociationPatternRolePartInterpeter(AssociationPatternRolePart ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		if (context.getContextBindings() == null) {
			logger.warn("Missing context to execute association pattern role part!");
			return QueryMatches.emptyMatches();
		}
		if (optionalArguments.length == 0) {
			logger.warn("Missing argument to specify behaviour of association pattern role part!");
			return QueryMatches.emptyMatches();
		}
		Collection<Topic> types = HashUtil.getHashSet();
		if (getExpression().contains(Anchor.class)) {
			QueryMatches types_ = extractArguments(runtime, Anchor.class, 0, context, optionalArguments);
			if (types_.isEmpty()) {
				logger.warn("Missing type to execute association pattern role part!");
				return QueryMatches.emptyMatches();
			}
			Object type_ = types_.getFirstValue();
			if (!(type_ instanceof Topic)) {
				logger.warn("Missing type to execute association pattern role part!");
				return QueryMatches.emptyMatches();
			}
			types.add((Topic) type_);
			if (context.isTransitive()) {
				types.addAll((Collection<Topic>) new SubtypesAxis().navigate(context, type_, null));
			}
		}
		Collection<Role> set = HashUtil.getHashSet();
		Object o = optionalArguments[0];
		for (Object association_ : context.getContextBindings().getPossibleValuesForVariable()) {
			if (association_ instanceof Association) {
				Association a = (Association) association_;
				/*
				 * filter out associations
				 */
				if (Association.class.equals(o) && context.getCurrentNode() instanceof Topic) {
					Topic player = (Topic) context.getCurrentNode();
					for (Role r : a.getRoles()) {
						if ((types.isEmpty() || types.contains(r.getType())) && player.equals(r.getPlayer())) {
							set.add(r);
							break;
						}
					}
				}
				/*
				 * get counter players
				 */
				else if (Topic.class.equals(o) && context.getCurrentNode() instanceof Topic) {
					Topic player = (Topic) context.getCurrentNode();
					for (Role r : a.getRoles()) {
						if ((types.isEmpty() || types.contains(r.getType())) && !r.getPlayer().equals(player)) {
							set.add(r);
						}
					}
				}
			}
		}

		/*
		 * handle filter parts
		 */
		Context newContext = new Context(context);
		newContext.setContextBindings(QueryMatches.asQueryMatchNS(runtime, set));
		for (IExpressionInterpreter<FilterPostfix> interpreter : getInterpretersFilteredByEypressionType(runtime, FilterPostfix.class)) {
			QueryMatches iteration = interpreter.interpret(runtime, newContext, optionalArguments);
			if (iteration.isEmpty()) {
				return iteration;
			}
			newContext.setContextBindings(iteration);
		}
		Collection<Construct> results = HashUtil.getHashSet();
		for (Object o2 : newContext.getContextBindings().getPossibleValuesForVariable()) {
			if (o2 instanceof Role) {
				Role r = (Role) o2;
				if (Association.class.equals(o)) {
					results.add(r.getParent());
				} else if (Topic.class.equals(o)) {
					results.add(r.getPlayer());
				}
			}
		}
		return QueryMatches.asQueryMatchNS(runtime, results);
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
