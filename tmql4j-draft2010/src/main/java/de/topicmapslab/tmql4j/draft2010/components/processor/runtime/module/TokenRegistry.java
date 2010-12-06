/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2010.components.processor.runtime.module;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.TokenRegistryImpl;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.And;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Arrow;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Association;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Axis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.AxisReifier;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.AxisScope;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketAngleClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketAngleOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketRoundOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketSquareClose;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.BracketSquareOpen;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Comma;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Datatype;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DirectInstance;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DirectSubtype;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DirectSupertype;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DirectType;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Div;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Dot;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DoubleColon;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Element;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Equality;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Equals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Function;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.GreaterEquals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.GreaterThan;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Instance;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Intersect;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.ItemIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.LowerEquals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.LowerThan;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.MatchesRegExp;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Minus;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Modulo;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Name;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Not;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Occurrence;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Or;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Parent;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Player;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Plus;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Pragma;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Prefix;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.RegularExpression;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Reified;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Role;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Scope;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Scoped;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Star;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.SubjectIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.SubjectLocator;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Substraction;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Subtype;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Supertype;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Topic;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Type;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Unequals;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Union;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Value;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Variable;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Variant;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.WhiteSpace;
import de.topicmapslab.tmql4j.exception.TMQLInitializationException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * @author Sven Krosse
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
	public TokenRegistry(ITMQLRuntime runtime) throws TMQLInitializationException {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	public IToken getDefaultToken() {
		return elementToken;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void initialize() {
		register(And.class);
		register(Arrow.class);
		register(Association.class);
		register(Axis.class);
		register(AxisReifier.class);
		register(AxisScope.class);
		register(BracketAngleClose.class);
		register(BracketAngleOpen.class);
		register(BracketRoundClose.class);
		register(BracketRoundOpen.class);
		register(BracketSquareClose.class);
		register(BracketSquareOpen.class);
		register(Comma.class);
		register(Datatype.class);
		register(DirectInstance.class);
		register(DirectSubtype.class);
		register(DirectSupertype.class);
		register(DirectType.class);
		register(Div.class);
		register(Dot.class);
		register(DoubleColon.class);
		register(Element.class);
		register(Equality.class);
		register(Equals.class);
		register(Function.class);
		register(GreaterEquals.class);
		register(GreaterThan.class);
		register(Instance.class);
		register(Intersect.class);
		register(ItemIdentifier.class);
		register(LowerEquals.class);
		register(LowerThan.class);
		register(MatchesRegExp.class);
		register(Minus.class);
		register(Modulo.class);
		register(Name.class);
		register(Not.class);
		register(Occurrence.class);
		register(Or.class);
		register(Parent.class);
		register(Player.class);
		register(Plus.class);
		register(Pragma.class);
		register(Prefix.class);
		register(RegularExpression.class);
		register(Reified.class);
		register(Role.class);
		register(Scope.class);
		register(Scoped.class);
		register(Star.class);
		register(SubjectIdentifier.class);
		register(SubjectLocator.class);
		register(Substraction.class);
		register(Subtype.class);
		register(Supertype.class);
		register(Topic.class);
		register(Type.class);
		register(Unequals.class);
		register(Union.class);
		register(Value.class);
		register(Variable.class);
		register(Variant.class);
		register(WhiteSpace.class);
	}

}
