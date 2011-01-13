/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator;

import java.util.Map;

import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.AliasValueExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.path.grammar.productions.Content;
import de.topicmapslab.tmql4j.path.grammar.productions.Navigation;
import de.topicmapslab.tmql4j.path.grammar.productions.PathExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.Postfix;
import de.topicmapslab.tmql4j.path.grammar.productions.PostfixedExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.ProjectionPostfix;
import de.topicmapslab.tmql4j.path.grammar.productions.QueryExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.SimpleContent;
import de.topicmapslab.tmql4j.path.grammar.productions.Step;
import de.topicmapslab.tmql4j.path.grammar.productions.StepDefinition;
import de.topicmapslab.tmql4j.path.grammar.productions.TupleExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.AliasValueExpressionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.AnchorTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.ContentTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.NavigationTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.PathExpressionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.PostfixTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.PostfixedExpressionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.ProjectionPostfixTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.QueryExpressionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.SimpleContentTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.StepDefinitionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.StepTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.TupleExpressionTranslator;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.ValueExpressionTranslator;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TranslatorRegistry {

	private final static Map<Class<? extends IExpression>, ITmqlSqlTranslator<?>> translators = HashUtil.getHashMap();

	static {
		translators.put(AliasValueExpression.class, new AliasValueExpressionTranslator());
		translators.put(Anchor.class, new AnchorTranslator());
		translators.put(Content.class, new ContentTranslator());
		translators.put(Navigation.class, new NavigationTranslator());
		translators.put(PathExpression.class, new PathExpressionTranslator());
		translators.put(PostfixedExpression.class, new PostfixedExpressionTranslator());
		translators.put(Postfix.class, new PostfixTranslator());
		translators.put(ProjectionPostfix.class, new ProjectionPostfixTranslator());
		translators.put(QueryExpression.class, new QueryExpressionTranslator());
		translators.put(SimpleContent.class, new SimpleContentTranslator());
		translators.put(StepDefinition.class, new StepDefinitionTranslator());
		translators.put(Step.class, new StepTranslator());
		translators.put(TupleExpression.class, new TupleExpressionTranslator());
		translators.put(ValueExpression.class, new ValueExpressionTranslator());
	}

	public static ITmqlSqlTranslator<?> getTranslator(Class<? extends IExpression> type) {
		return translators.get(type);
	}

}
