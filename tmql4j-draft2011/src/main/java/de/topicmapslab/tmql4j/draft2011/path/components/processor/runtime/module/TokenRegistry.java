/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.processor.runtime.module;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.TokenRegistryImpl;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Ako;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.All;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.And;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.As;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Asc;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.At;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisAtomify;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisCharacteristics;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisDatatype;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisId;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisIndicators;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisInstances;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisItem;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisLocators;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisPlayers;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisReifier;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisRoleTypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisRoles;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisScope;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSubtypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisSupertypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTraverse;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTyped;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisTypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisVariants;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketAngleClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketAngleOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.By;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Colon;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Comment;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Datatype;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.DatatypedElement;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Delete;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Desc;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Dollar;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.DoubleDot;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Element;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Ellipsis;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Else;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Equality;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Every;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Exists;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Function;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.GreaterEquals;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Group;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.If;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.In;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Intersect;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Isa;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Least;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Literal;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.LowerEquals;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.LowerThan;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Minus;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Modulo;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Most;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.MoveBackward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.MoveForward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Not;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Null;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Or;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Order;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Percent;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Plus;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Prefix;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.RegularExpression;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Satisfies;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Scope;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisAtomifyMoveBackward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisAtomifyMoveForward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisIndicators;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisInstances;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisItem;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisLocators;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisPlayersMoveBackward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisPlayersMoveForward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisReifierMoveBackward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisReifierMoveForward;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisTraverse;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutAxisTypes;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.ShortcutCondition;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Some;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Star;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Substraction;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Then;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.TripleQuote;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Unequals;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Union;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Variable;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.Where;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.WhiteSpace;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * Class to handle all tokens of different languages extensions and the core implementation.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TokenRegistry extends TokenRegistryImpl {

	/**
	 * the default token
	 */
	private final IToken elementToken = new Element();

	/**
	 * constructor
	 * 
	 * @param runtime
	 *            the encapsulating runtime
	 * @throws TMQLInitializationException
	 *             thrown if initialization failed
	 */
	public TokenRegistry(final ITMQLRuntime runtime) throws TMQLInitializationException {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialize() {
		/*
		 * add all predefined language tokens of the TMQL core
		 */
		try {
			register(Ako.class);
			register(All.class);
			register(And.class);
			register(Asc.class);
			register(As.class);
			register(At.class);
			register(AxisAtomify.class);
			register(AxisCharacteristics.class);
			register(AxisDatatype.class);
			register(AxisIndicators.class);
			register(AxisInstances.class);
			register(AxisId.class);
			register(AxisItem.class);
			register(AxisLocators.class);
			register(AxisPlayers.class);
			register(AxisReifier.class);
			register(AxisRoles.class);
			register(AxisRoleTypes.class);
			register(AxisScope.class);
			register(AxisSubtypes.class);
			register(AxisSupertypes.class);
			register(AxisTraverse.class);
			register(AxisTypes.class);
			register(AxisTyped.class);
			register(AxisVariants.class);
			register(BracketAngleClose.class);
			register(BracketAngleOpen.class);
			register(BracketRoundClose.class);
			register(BracketRoundOpen.class);
			register(BracketSquareOpen.class);
			register(BracketSquareClose.class);
			register(By.class);
			register(Colon.class);
			register(Delete.class);
			register(Union.class);
			register(Comma.class);
			register(Comment.class);
			register(Datatype.class);
			register(DatatypedElement.class);
			register(Desc.class);
			register(Dollar.class);
			register(Dot.class);
			register(DoubleDot.class);
			register(Ellipsis.class);
			register(Else.class);
			register(Equality.class);
			register(Intersect.class);
			register(Unequals.class);
			register(Every.class);
			register(Exists.class);
			register(Function.class);
			register(GreaterEquals.class);
			register(GreaterThan.class);
			register(Group.class);
			register(If.class);
			register(In.class);
			register(Isa.class);
			register(Least.class);
			register(Literal.class);
			register(LowerEquals.class);
			register(LowerThan.class);
			register(Minus.class);
			register(Modulo.class);
			register(Most.class);
			register(MoveBackward.class);
			register(MoveForward.class);
			register(Not.class);
			register(Null.class);
			register(Or.class);
			register(Order.class);
			register(Percent.class);
			register(Plus.class);
			register(Prefix.class);
			register(RegularExpression.class);
			register(Satisfies.class);
			register(Scope.class);
			register(ShortcutAxisAtomifyMoveBackward.class);
			register(ShortcutAxisAtomifyMoveForward.class);
			register(ShortcutAxisIndicators.class);
			register(ShortcutAxisInstances.class);
			register(ShortcutAxisItem.class);
			register(ShortcutAxisLocators.class);
			register(ShortcutAxisPlayersMoveBackward.class);
			register(ShortcutAxisPlayersMoveForward.class);
			register(ShortcutAxisReifierMoveBackward.class);
			register(ShortcutAxisReifierMoveForward.class);
			register(ShortcutAxisTraverse.class);
			register(ShortcutAxisTypes.class);
			register(ShortcutCondition.class);
			register(Some.class);
			register(Star.class);
			register(Substraction.class);
			register(Then.class);
			register(TripleQuote.class);
			register(Variable.class);
			register(Where.class);
			register(WhiteSpace.class);
		} catch (TMQLExtensionRegistryException e) {
			throw new TMQLInitializationException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IToken getDefaultToken() {
		return elementToken;
	}

}
