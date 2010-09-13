/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.lexer.core;

import java.util.Set;

import de.topicmapslab.tmql4j.common.core.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.extensions.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.Ako;
import de.topicmapslab.tmql4j.lexer.token.And;
import de.topicmapslab.tmql4j.lexer.token.Asc;
import de.topicmapslab.tmql4j.lexer.token.At;
import de.topicmapslab.tmql4j.lexer.token.AxisAtomify;
import de.topicmapslab.tmql4j.lexer.token.AxisCharacteristics;
import de.topicmapslab.tmql4j.lexer.token.AxisId;
import de.topicmapslab.tmql4j.lexer.token.AxisIndicators;
import de.topicmapslab.tmql4j.lexer.token.AxisInstances;
import de.topicmapslab.tmql4j.lexer.token.AxisItem;
import de.topicmapslab.tmql4j.lexer.token.AxisLocators;
import de.topicmapslab.tmql4j.lexer.token.AxisPlayers;
import de.topicmapslab.tmql4j.lexer.token.AxisReifier;
import de.topicmapslab.tmql4j.lexer.token.AxisRoles;
import de.topicmapslab.tmql4j.lexer.token.AxisScope;
import de.topicmapslab.tmql4j.lexer.token.AxisSubtypes;
import de.topicmapslab.tmql4j.lexer.token.AxisSupertypes;
import de.topicmapslab.tmql4j.lexer.token.AxisTraverse;
import de.topicmapslab.tmql4j.lexer.token.AxisTypes;
import de.topicmapslab.tmql4j.lexer.token.BracketAngleClose;
import de.topicmapslab.tmql4j.lexer.token.BracketAngleOpen;
import de.topicmapslab.tmql4j.lexer.token.BracketRoundClose;
import de.topicmapslab.tmql4j.lexer.token.BracketRoundOpen;
import de.topicmapslab.tmql4j.lexer.token.BracketSquareClose;
import de.topicmapslab.tmql4j.lexer.token.BracketSquareOpen;
import de.topicmapslab.tmql4j.lexer.token.By;
import de.topicmapslab.tmql4j.lexer.token.Colon;
import de.topicmapslab.tmql4j.lexer.token.Combination;
import de.topicmapslab.tmql4j.lexer.token.Comma;
import de.topicmapslab.tmql4j.lexer.token.Comment;
import de.topicmapslab.tmql4j.lexer.token.Datatype;
import de.topicmapslab.tmql4j.lexer.token.DatatypedElement;
import de.topicmapslab.tmql4j.lexer.token.Desc;
import de.topicmapslab.tmql4j.lexer.token.Dollar;
import de.topicmapslab.tmql4j.lexer.token.Dot;
import de.topicmapslab.tmql4j.lexer.token.DoubleDot;
import de.topicmapslab.tmql4j.lexer.token.Element;
import de.topicmapslab.tmql4j.lexer.token.Ellipsis;
import de.topicmapslab.tmql4j.lexer.token.Else;
import de.topicmapslab.tmql4j.lexer.token.Equality;
import de.topicmapslab.tmql4j.lexer.token.Every;
import de.topicmapslab.tmql4j.lexer.token.Exists;
import de.topicmapslab.tmql4j.lexer.token.For;
import de.topicmapslab.tmql4j.lexer.token.From;
import de.topicmapslab.tmql4j.lexer.token.Function;
import de.topicmapslab.tmql4j.lexer.token.GreaterEquals;
import de.topicmapslab.tmql4j.lexer.token.GreaterThan;
import de.topicmapslab.tmql4j.lexer.token.If;
import de.topicmapslab.tmql4j.lexer.token.In;
import de.topicmapslab.tmql4j.lexer.token.Isa;
import de.topicmapslab.tmql4j.lexer.token.Least;
import de.topicmapslab.tmql4j.lexer.token.Limit;
import de.topicmapslab.tmql4j.lexer.token.Literal;
import de.topicmapslab.tmql4j.lexer.token.LowerEquals;
import de.topicmapslab.tmql4j.lexer.token.LowerThan;
import de.topicmapslab.tmql4j.lexer.token.Minus;
import de.topicmapslab.tmql4j.lexer.token.Modulo;
import de.topicmapslab.tmql4j.lexer.token.Most;
import de.topicmapslab.tmql4j.lexer.token.MoveBackward;
import de.topicmapslab.tmql4j.lexer.token.MoveForward;
import de.topicmapslab.tmql4j.lexer.token.Not;
import de.topicmapslab.tmql4j.lexer.token.Null;
import de.topicmapslab.tmql4j.lexer.token.Offset;
import de.topicmapslab.tmql4j.lexer.token.Or;
import de.topicmapslab.tmql4j.lexer.token.Order;
import de.topicmapslab.tmql4j.lexer.token.Percent;
import de.topicmapslab.tmql4j.lexer.token.Plus;
import de.topicmapslab.tmql4j.lexer.token.Pragma;
import de.topicmapslab.tmql4j.lexer.token.Prefix;
import de.topicmapslab.tmql4j.lexer.token.RegularExpression;
import de.topicmapslab.tmql4j.lexer.token.Return;
import de.topicmapslab.tmql4j.lexer.token.Satisfies;
import de.topicmapslab.tmql4j.lexer.token.Scope;
import de.topicmapslab.tmql4j.lexer.token.Select;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisAtomifyMoveBackward;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisAtomifyMoveForward;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisIndicators;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisInstances;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisItem;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisLocators;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisPlayersMoveBackward;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisPlayersMoveForward;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisReifierMoveBackward;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisReifierMoveForward;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisTraverse;
import de.topicmapslab.tmql4j.lexer.token.ShortcutAxisTypes;
import de.topicmapslab.tmql4j.lexer.token.ShortcutCondition;
import de.topicmapslab.tmql4j.lexer.token.Some;
import de.topicmapslab.tmql4j.lexer.token.Star;
import de.topicmapslab.tmql4j.lexer.token.Substraction;
import de.topicmapslab.tmql4j.lexer.token.Then;
import de.topicmapslab.tmql4j.lexer.token.TripleQuote;
import de.topicmapslab.tmql4j.lexer.token.Unique;
import de.topicmapslab.tmql4j.lexer.token.Variable;
import de.topicmapslab.tmql4j.lexer.token.Where;
import de.topicmapslab.tmql4j.lexer.token.WhiteSpace;
import de.topicmapslab.tmql4j.lexer.token.XmlEndTag;
import de.topicmapslab.tmql4j.lexer.token.XmlStartTag;

/**
 * Registry class to handle all tokens of different languages extensions and the
 * core implementation.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TokenRegistry {

	/**
	 * the encapsulated runtime
	 */
	private final ITMQLRuntime runtime;
	/**
	 * a token type store
	 */
	private final Set<Class<? extends IToken>> tokenClasses = HashUtil
			.getHashSet();
	/**
	 * the token store
	 */
	private final Set<IToken> tokens = HashUtil.getHashSet();
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
	public TokenRegistry(final ITMQLRuntime runtime)
			throws TMQLInitializationException {
		this.runtime = runtime;
		init();
	}

	/**
	 * Initialization method to register all tokens of the core language
	 * 
	 * @throws TMQLInitializationException
	 *             thrown if initialization failed
	 */
	private final void init() throws TMQLInitializationException {
		/*
		 * add all predefined language tokens of the TMQL core
		 */
		try {
			register(Ako.class);
			register(And.class);
			register(Asc.class);
			register(At.class);
			register(AxisAtomify.class);
			register(AxisCharacteristics.class);
			register(AxisIndicators.class);
			register(AxisInstances.class);
			register(AxisId.class);
			register(AxisItem.class);
			register(AxisLocators.class);
			register(AxisPlayers.class);
			register(AxisReifier.class);
			register(AxisRoles.class);
			register(AxisScope.class);
			register(AxisSubtypes.class);
			register(AxisSupertypes.class);
			register(AxisTraverse.class);
			register(AxisTypes.class);
			register(BracketAngleClose.class);
			register(BracketAngleOpen.class);
			register(BracketRoundClose.class);
			register(BracketRoundOpen.class);
			register(BracketSquareOpen.class);
			register(BracketSquareClose.class);
			register(By.class);
			register(Colon.class);
			register(Combination.class);
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
			register(Every.class);
			register(Exists.class);
			register(For.class);
			register(From.class);
			register(Function.class);
			register(GreaterEquals.class);
			register(GreaterThan.class);
			register(If.class);
			register(In.class);
			register(Isa.class);
			register(Least.class);
			register(Limit.class);
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
			register(Offset.class);
			register(Or.class);
			register(Order.class);
			register(Percent.class);
			register(Plus.class);
			register(Pragma.class);
			register(Prefix.class);
			register(RegularExpression.class);
			register(Return.class);
			register(Satisfies.class);
			register(Scope.class);
			register(Select.class);
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
			register(Unique.class);
			register(Variable.class);
			register(Where.class);
			register(WhiteSpace.class);
			register(XmlEndTag.class);
			register(XmlStartTag.class);
		} catch (TMQLExtensionRegistryException e) {
			throw new TMQLInitializationException(e);
		}
	}

	/**
	 * Registration method to add a new token to the internal store
	 * 
	 * @param tokenClass
	 *            the class representing the token
	 * @throws TMQLExtensionRegistryException
	 *             throw if instantiation failed
	 */
	public final void register(final Class<? extends IToken> tokenClass)
			throws TMQLExtensionRegistryException {
		try {
			register(tokenClass.newInstance());
		} catch (InstantiationException e) {
			throw new TMQLExtensionRegistryException(e);
		} catch (IllegalAccessException e) {
			throw new TMQLExtensionRegistryException(e);
		}
	}

	/**
	 * Registration method to add a new token to the internal store
	 * 
	 * @param token
	 *            the token to add
	 * 
	 */
	public final void register(final IToken token) {
		if (!tokenClasses.contains(token.getClass())) {
			tokenClasses.add(token.getClass());
			tokens.add(token);
		}
	}

	/**
	 * Returns the token representing the given string literal. If no token
	 * matches to the given literal the default token 'ELEMENT' will returned.
	 * 
	 * @param literal
	 *            the string literal
	 * @return the token representing the literal or 'ELEMENT' if no token
	 *         matches.
	 */
	public final IToken getTokenByLiteral(final String literal) {
		/*
		 * iterate over tokens
		 */
		for (IToken token : tokens) {
			/*
			 * check if token is known for literal
			 */
			if (token.isToken(runtime, literal)) {
				/*
				 * return token
				 */
				return token;
			}
		}

		/*
		 * return default
		 */
		return elementToken;
	}

	/**
	 * Returns the token class representing the given string literal. If no
	 * token matches to the given literal the default token 'ELEMENT' will
	 * returned.
	 * 
	 * @param literal
	 *            literal the string literal
	 * @return the class of the token representing the literal or 'ELEMENT' if
	 *         no token matches.
	 */
	public final Class<? extends IToken> getTokenClassByLiteral(
			final String literal) {
		return getTokenByLiteral(literal).getClass();
	}

}
