package de.topicmapslab.tmql4j.insert.grammar.productions;

import java.util.Iterator;
import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.insert.grammar.tokens.Delete;
import de.topicmapslab.tmql4j.insert.grammar.tokens.Insert;
import de.topicmapslab.tmql4j.insert.grammar.tokens.Merge;
import de.topicmapslab.tmql4j.insert.grammar.tokens.Update;
import de.topicmapslab.tmql4j.path.grammar.lexical.Pragma;
import de.topicmapslab.tmql4j.path.grammar.lexical.Prefix;
import de.topicmapslab.tmql4j.path.grammar.productions.EnvironmentClause;

public class QueryExpression extends ExpressionImpl {

	/**
	 * grammar type of a query-expression containing a delete-expression
	 */
	public static final int TYPE_DELETE_EXPRESSION = 3;
	/**
	 * grammar type of a query-expression containing an insert-expression
	 */
	public static final int TYPE_INSERT_EXPRESSION = 4;
	/**
	 * grammar type of a query-expression containing an update-expression
	 */
	public static final int TYPE_UPDATE_EXPRESSION = 5;
	/**
	 * grammar type of a query-expression containing a merge-expression
	 */
	public static final int TYPE_MERGE_EXPRESSION = 6;

	public QueryExpression(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * initialize token iterator
		 */
		Iterator<Class<? extends IToken>> chain = tmqlTokens.iterator();
		/*
		 * get key token
		 */
		Class<? extends IToken> key = chain.next();

		/*
		 * create temporary lists of tokens
		 */
		List<Class<? extends IToken>> tmqlTokens_ = tmqlTokens;
		List<String> tokens_ = tokens;

		/*
		 * check if first token is %prefix or %pragma
		 */
		if (key.equals(Pragma.class) || key.equals(Prefix.class)) {
			/*
			 * get last index of %pragma
			 */
			int pragmaIndex = tmqlTokens.lastIndexOf(Pragma.class);
			/*
			 * get last index of %prefix
			 */
			int directiveIndex = tmqlTokens.lastIndexOf(Prefix.class);

			/*
			 * get real index
			 */
			int index = pragmaIndex > directiveIndex ? pragmaIndex + 3 : directiveIndex + 3;

			/*
			 * add environment-clause
			 */
			tmqlTokens_ = tmqlTokens.subList(0, index);
			tokens_ = tokens.subList(0, index);
			checkForExtensions(EnvironmentClause.class, tmqlTokens_, tokens_, runtime);

			tmqlTokens_ = tmqlTokens.subList(index, tmqlTokens.size());
			tokens_ = tokens.subList(index, tokens.size());

			/*
			 * get next key
			 */
			key = tmqlTokens_.iterator().next();
		}

		/*
		 * check if token is keyword DELETE
		 */
		if (tmqlTokens.contains(Delete.class)) {
			checkForExtensions(DeleteExpression.class, tmqlTokens_, tokens_, runtime);
			setGrammarType(TYPE_DELETE_EXPRESSION);
		}
		/*
		 * check if token is keyword INSERT
		 */
		else if (tmqlTokens.contains(Insert.class)) {
			checkForExtensions(InsertExpression.class, tmqlTokens_, tokens_, runtime);
			setGrammarType(TYPE_INSERT_EXPRESSION);
		}
		/*
		 * check if token is keyword UPDATE
		 */
		else if (tmqlTokens.contains(Update.class)) {
			checkForExtensions(UpdateExpression.class, tmqlTokens_, tokens_, runtime);
			setGrammarType(TYPE_UPDATE_EXPRESSION);
		}
		/*
		 * check if token is keyword MERGE
		 */
		else if (tmqlTokens.contains(Merge.class)) {
			checkForExtensions(MergeExpression.class, tmqlTokens_, tokens_, runtime);
			setGrammarType(TYPE_MERGE_EXPRESSION);
		} else {
			throw new TMQLInvalidSyntaxException(tmqlTokens_, tokens_, this.getClass());
		}
	}

	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty()
				&& (getTmqlTokens().contains(Delete.class) || getTmqlTokens().contains(Merge.class) || getTmqlTokens().contains(Insert.class) || getTmqlTokens().contains(Update.class));
	}

}
