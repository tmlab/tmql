/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator;

import java.lang.reflect.ParameterizedType;

import de.topicmapslab.tmql4j.grammar.productions.IExpression;

/**
 * @author Sven Krosse
 *
 */
public abstract class TmqlSqlTranslatorImpl<T extends IExpression> implements ISqlTranslator<T> {
	
	private Class<T> type;
	
	/**
	 * constructor
	 */
	@SuppressWarnings("unchecked")
	public TmqlSqlTranslatorImpl() {
		Class<?> clazz = getClass();
		while ( !(clazz.getGenericSuperclass() instanceof ParameterizedType)){
			clazz = clazz.getSuperclass();
		}
		type = (Class<T>) ((ParameterizedType) clazz
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Class<T> getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isApplicable(IExpression expression) {
		return getType().isAssignableFrom(expression.getClass());
	}
}
