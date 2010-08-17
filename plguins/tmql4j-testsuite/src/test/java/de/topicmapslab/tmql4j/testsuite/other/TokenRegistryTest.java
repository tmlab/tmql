package de.topicmapslab.tmql4j.testsuite.other;

import junit.framework.Assert;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Add;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.All;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Cascade;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Delete;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Insert;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Names;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Occurrences;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Set;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Update;
import de.topicmapslab.tmql4j.lexer.core.TokenRegistry;
import de.topicmapslab.tmql4j.lexer.token.Ako;
import de.topicmapslab.tmql4j.lexer.token.And;
import de.topicmapslab.tmql4j.lexer.token.Asc;
import de.topicmapslab.tmql4j.lexer.token.At;
import de.topicmapslab.tmql4j.lexer.token.AxisAtomify;
import de.topicmapslab.tmql4j.lexer.token.AxisCharacteristics;
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
import de.topicmapslab.tmql4j.testsuite.base.BaseTest;

public class TokenRegistryTest extends BaseTest {

	public void testGetTokenClassByLiteral() {
		TokenRegistry registry = runtime.getLanguageContext()
				.getTokenRegistry();

		Assert.assertEquals(Ako.class, registry.getTokenClassByLiteral("AKO"));
		Assert.assertEquals(And.class, registry.getTokenClassByLiteral("AND"));
		Assert.assertEquals(And.class, registry.getTokenClassByLiteral("&"));
		Assert.assertEquals(Asc.class, registry.getTokenClassByLiteral("ASC"));
		Assert.assertEquals(At.class, registry.getTokenClassByLiteral("AT"));
		Assert.assertEquals(AxisAtomify.class, registry
				.getTokenClassByLiteral("atomify"));
		Assert.assertEquals(AxisCharacteristics.class, registry
				.getTokenClassByLiteral("characteristics"));
		Assert.assertEquals(AxisIndicators.class, registry
				.getTokenClassByLiteral("indicators"));
		Assert.assertEquals(AxisInstances.class, registry
				.getTokenClassByLiteral("instances"));
		Assert.assertEquals(AxisItem.class, registry
				.getTokenClassByLiteral("item"));
		Assert.assertEquals(AxisLocators.class, registry
				.getTokenClassByLiteral("locators"));
		Assert.assertEquals(AxisPlayers.class, registry
				.getTokenClassByLiteral("players"));
		Assert.assertEquals(AxisReifier.class, registry
				.getTokenClassByLiteral("reifier"));
		Assert.assertEquals(AxisRoles.class, registry
				.getTokenClassByLiteral("roles"));
		Assert.assertEquals(AxisScope.class, registry
				.getTokenClassByLiteral("scope"));
		Assert.assertEquals(AxisSubtypes.class, registry
				.getTokenClassByLiteral("subtypes"));
		Assert.assertEquals(AxisSupertypes.class, registry
				.getTokenClassByLiteral("supertypes"));
		Assert.assertEquals(AxisTraverse.class, registry
				.getTokenClassByLiteral("traverse"));
		Assert.assertEquals(AxisTypes.class, registry
				.getTokenClassByLiteral("types"));
		Assert.assertEquals(BracketAngleClose.class, registry
				.getTokenClassByLiteral("}"));
		Assert.assertEquals(BracketAngleOpen.class, registry
				.getTokenClassByLiteral("{"));
		Assert.assertEquals(BracketRoundClose.class, registry
				.getTokenClassByLiteral(")"));
		Assert.assertEquals(BracketRoundOpen.class, registry
				.getTokenClassByLiteral("("));
		Assert.assertEquals(BracketSquareClose.class, registry
				.getTokenClassByLiteral("]"));
		Assert.assertEquals(BracketSquareOpen.class, registry
				.getTokenClassByLiteral("["));
		Assert.assertEquals(By.class, registry.getTokenClassByLiteral("BY"));
		Assert.assertEquals(Colon.class, registry.getTokenClassByLiteral(":"));
		Assert.assertEquals(Combination.class, registry
				.getTokenClassByLiteral("++"));
		Assert.assertEquals(Comma.class, registry.getTokenClassByLiteral(","));
		Assert
				.assertEquals(Comment.class, registry
						.getTokenClassByLiteral("#"));
		Assert.assertEquals(Datatype.class, registry
				.getTokenClassByLiteral("^^"));
		Assert.assertEquals(DatatypedElement.class, registry
				.getTokenClassByLiteral("\"1\"^^xsd:integer"));
		Assert
				.assertEquals(Desc.class, registry
						.getTokenClassByLiteral("DESC"));
		Assert.assertEquals(Dollar.class, registry.getTokenClassByLiteral("$"));
		Assert.assertEquals(Dot.class, registry.getTokenClassByLiteral("."));
		Assert.assertEquals(DoubleDot.class, registry
				.getTokenClassByLiteral(".."));
		Assert.assertEquals(Element.class, registry
				.getTokenClassByLiteral("o:puccini"));
		Assert.assertEquals(Ellipsis.class, registry
				.getTokenClassByLiteral("..."));
		Assert
				.assertEquals(Else.class, registry
						.getTokenClassByLiteral("ELSE"));
		Assert.assertEquals(Equality.class, registry
				.getTokenClassByLiteral("=="));
		Assert.assertEquals(Every.class, registry
				.getTokenClassByLiteral("EVERY"));
		Assert.assertEquals(Exists.class, registry
				.getTokenClassByLiteral("EXISTS"));
		Assert.assertEquals(For.class, registry.getTokenClassByLiteral("FOR"));
		Assert
				.assertEquals(From.class, registry
						.getTokenClassByLiteral("FROM"));
		Assert.assertEquals(GreaterEquals.class, registry
				.getTokenClassByLiteral(">="));
		Assert.assertEquals(GreaterThan.class, registry
				.getTokenClassByLiteral(">"));
		Assert.assertEquals(If.class, registry.getTokenClassByLiteral("IF"));
		Assert.assertEquals(In.class, registry.getTokenClassByLiteral("IN"));
		Assert.assertEquals(Isa.class, registry.getTokenClassByLiteral("ISA"));
		Assert.assertEquals(Least.class, registry
				.getTokenClassByLiteral("LEAST"));
		Assert.assertEquals(Limit.class, registry
				.getTokenClassByLiteral("LIMIT"));
		Assert.assertEquals(Literal.class, registry
				.getTokenClassByLiteral("\"ab cd\""));
		Assert.assertEquals(LowerEquals.class, registry
				.getTokenClassByLiteral("<="));
		Assert.assertEquals(LowerThan.class, registry
				.getTokenClassByLiteral("<"));
		Assert.assertEquals(Minus.class, registry.getTokenClassByLiteral("-"));
		Assert.assertEquals(Modulo.class, registry
				.getTokenClassByLiteral("mod"));
		Assert
				.assertEquals(Most.class, registry
						.getTokenClassByLiteral("MOST"));
		Assert.assertEquals(MoveBackward.class, registry
				.getTokenClassByLiteral("<<"));
		Assert.assertEquals(MoveForward.class, registry
				.getTokenClassByLiteral(">>"));
		Assert.assertEquals(Not.class, registry.getTokenClassByLiteral("NOT"));
		Assert
				.assertEquals(Null.class, registry
						.getTokenClassByLiteral("NULL"));
		Assert.assertEquals(Offset.class, registry
				.getTokenClassByLiteral("OFFSET"));
		Assert.assertEquals(Or.class, registry.getTokenClassByLiteral("OR"));
		Assert.assertEquals(Or.class, registry.getTokenClassByLiteral("|"));
		Assert.assertEquals(Order.class, registry
				.getTokenClassByLiteral("ORDER"));
		Assert
				.assertEquals(Percent.class, registry
						.getTokenClassByLiteral("%"));
		Assert.assertEquals(Plus.class, registry.getTokenClassByLiteral("+"));
		Assert.assertEquals(Pragma.class, registry
				.getTokenClassByLiteral("%pragma"));
		Assert.assertEquals(Prefix.class, registry
				.getTokenClassByLiteral("%prefix"));
		Assert.assertEquals(RegularExpression.class, registry
				.getTokenClassByLiteral("=~"));
		Assert.assertEquals(Return.class, registry
				.getTokenClassByLiteral("RETURN"));
		Assert.assertEquals(Satisfies.class, registry
				.getTokenClassByLiteral("SATISFIES"));
		Assert.assertEquals(Scope.class, registry.getTokenClassByLiteral("@"));
		Assert.assertEquals(Select.class, registry
				.getTokenClassByLiteral("SELECT"));
		Assert.assertEquals(ShortcutAxisAtomifyMoveBackward.class, registry
				.getTokenClassByLiteral("\\"));
		Assert.assertEquals(ShortcutAxisAtomifyMoveForward.class, registry
				.getTokenClassByLiteral("/"));
		Assert.assertEquals(ShortcutAxisIndicators.class, registry
				.getTokenClassByLiteral("~"));
		Assert.assertEquals(ShortcutAxisInstances.class, registry
				.getTokenClassByLiteral("//"));
		Assert.assertEquals(ShortcutAxisItem.class, registry
				.getTokenClassByLiteral("!"));
		Assert.assertEquals(ShortcutAxisLocators.class, registry
				.getTokenClassByLiteral("="));
		Assert.assertEquals(ShortcutAxisPlayersMoveBackward.class, registry
				.getTokenClassByLiteral("<-"));
		Assert.assertEquals(ShortcutAxisPlayersMoveForward.class, registry
				.getTokenClassByLiteral("->"));
		Assert.assertEquals(ShortcutAxisReifierMoveBackward.class, registry
				.getTokenClassByLiteral("<~~"));
		Assert.assertEquals(ShortcutAxisReifierMoveForward.class, registry
				.getTokenClassByLiteral("~~>"));
		Assert.assertEquals(ShortcutAxisTraverse.class, registry
				.getTokenClassByLiteral("<->"));
		Assert.assertEquals(ShortcutAxisTypes.class, registry
				.getTokenClassByLiteral("^"));
		Assert.assertEquals(ShortcutCondition.class, registry
				.getTokenClassByLiteral("||"));
		Assert
				.assertEquals(Some.class, registry
						.getTokenClassByLiteral("SOME"));
		Assert.assertEquals(Star.class, registry.getTokenClassByLiteral("*"));
		Assert.assertEquals(Substraction.class, registry
				.getTokenClassByLiteral("--"));
		Assert
				.assertEquals(Then.class, registry
						.getTokenClassByLiteral("THEN"));
		Assert.assertEquals(TripleQuote.class, registry
				.getTokenClassByLiteral("\"\"\""));
		Assert.assertEquals(Unique.class, registry
				.getTokenClassByLiteral("UNIQUE"));
		Assert.assertEquals(Variable.class, registry
				.getTokenClassByLiteral("$var"));
		Assert.assertEquals(Variable.class, registry
				.getTokenClassByLiteral("%var"));
		Assert.assertEquals(Variable.class, registry
				.getTokenClassByLiteral("@var"));
		Assert.assertEquals(Where.class, registry
				.getTokenClassByLiteral("WHERE"));
		Assert.assertEquals(WhiteSpace.class, registry
				.getTokenClassByLiteral(" "));
		Assert.assertEquals(XmlEndTag.class, registry
				.getTokenClassByLiteral("</xml>"));
		Assert.assertEquals(XmlStartTag.class, registry
				.getTokenClassByLiteral("<xml>"));
		Assert.assertEquals(XmlStartTag.class, registry
				.getTokenClassByLiteral("<xml class=\"abc\">"));

	}

	public void testGetTokenClassOfTMMLByLiteral() {
		TokenRegistry registry = runtime.getLanguageContext().getTokenRegistry();

		Assert.assertEquals(Delete.class, registry
				.getTokenClassByLiteral("DELETE"));
		Assert.assertEquals(Cascade.class, registry
				.getTokenClassByLiteral("CASCADE"));
		Assert.assertEquals(Insert.class, registry
				.getTokenClassByLiteral("INSERT"));
		Assert.assertEquals(All.class, registry.getTokenClassByLiteral("ALL"));
		Assert.assertEquals(Update.class, registry
				.getTokenClassByLiteral("UPDATE"));
		Assert.assertEquals(Set.class, registry.getTokenClassByLiteral("SET"));
		Assert.assertEquals(Add.class, registry.getTokenClassByLiteral("ADD"));
		Assert.assertEquals(Names.class, registry
				.getTokenClassByLiteral("names"));
		Assert.assertEquals(Occurrences.class, registry
				.getTokenClassByLiteral("occurrences"));

	}

}
