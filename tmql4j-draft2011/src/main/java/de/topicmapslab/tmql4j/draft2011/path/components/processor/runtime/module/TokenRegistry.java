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
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.*;
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
			register(AxisById.class);
			register(AxisByItemIdentifier.class);
			register(AxisBySubjectIdentifier.class);
			register(AxisBySubjectLocator.class);
			register(AxisByRegularExpression.class);
			register(AxisByValue.class);
			register(AxisValue.class);
			register(AxisDatatype.class);
			register(AxisDatatyped.class);
			register(AxisSubjectIdentifiers.class);
			register(AxisInstances.class);
			register(AxisId.class);
			register(AxisOccurrences.class);
			register(AxisNames.class);
			register(AxisItemIdentifiers.class);
			register(AxisSubjectLocators.class);
			register(AxisPlayers.class);
			register(AxisReifier.class);
			register(AxisReified.class);
			register(AxisPlayedRoles.class);
			register(AxisRoles.class);
			register(AxisRoleTypes.class);
			register(AxisScope.class);
			register(AxisScoped.class);
			register(AxisSubtypes.class);
			register(AxisSupertypes.class);
			register(AxisTraverse.class);
			register(AxisTypes.class);
			register(AxisTyped.class);
			register(AxisVariants.class);
			register(AxisPlayedAssociations.class);
			register(AxisParent.class);
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
			register(Slash.class);
			register(ShortcutAxisBySubjectIdentifier.class);
			register(ShortcutAxisInstances.class);
			register(ShortcutAxisByItemIdentifier.class);
			register(ShortcutAxisBySubjectLocator.class);
			register(ShortcutAxisPlayedRoles.class);
			register(ShortcutAxisPlayers.class);
			register(ShortcutAxisReifier.class);
			register(ShortcutAxisReified.class);
			register(ShortcutAxisTraverse.class);
			register(ShortcutAxisTypes.class);
			register(ShortcutCondition.class);
			register(Some.class);
			register(Star.class);
			register(Except.class);
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
	@Override
	public IToken getDefaultToken() {
		return elementToken;
	}

}
