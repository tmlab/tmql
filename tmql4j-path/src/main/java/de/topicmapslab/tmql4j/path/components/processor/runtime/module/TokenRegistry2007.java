/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.path.components.processor.runtime.module;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.TokenRegistry;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.path.grammar.lexical.*;

/**
 * Class to handle all tokens of different languages extensions and the core
 * implementation.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TokenRegistry2007 extends TokenRegistry {

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
	public TokenRegistry2007(final ITMQLRuntime runtime) throws TMQLInitializationException {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void initialize() {
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
	 * {@inheritDoc}
	 */
	protected IToken getDefaultToken() {
		return elementToken;
	}

}
