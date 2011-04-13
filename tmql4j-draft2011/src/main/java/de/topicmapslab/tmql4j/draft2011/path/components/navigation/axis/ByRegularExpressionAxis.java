/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.draft2011.path.components.navigation.axis;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Pattern;

import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Variant;

import de.topicmapslab.majortom.model.index.ILiteralIndex;
import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.draft2011.path.components.navigation.Axis;
import de.topicmapslab.tmql4j.draft2011.path.exception.InvalidValueException;
import de.topicmapslab.tmql4j.draft2011.path.grammar.lexical.AxisByRegularExpression;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

/**
 * Class definition representing the by-regular-expression axis.
 * <p>
 * If the value is a string, this step returns all names, occurrences and variants matching the given value. The
 * optional argument is interpreted as type of them.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ByRegularExpressionAxis extends Axis {

	/**
	 * base constructor to create an new instance
	 */
	public ByRegularExpressionAxis() {
		super(AxisByRegularExpression.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> navigate(IContext context, Object source, Topic type) throws TMQLRuntimeException {
		if (source instanceof Object) {
			TopicMap map = context.getQuery().getTopicMap();
			/*
			 * create new instance of tuple-sequence
			 */
			Collection<Object> set = new LinkedList<Object>();
			try {
				/*
				 * get literal index
				 */
				ILiteralIndex index = map.getIndex(ILiteralIndex.class);
				if (!index.isOpen()) {
					index.open();
				}
				/*
				 * check if occurrence with literal exists
				 */
				set.addAll(index.getCharacteristicsMatches(source.toString()));
			} catch (IllegalArgumentException e) {
				/*
				 * is not MAJORTOM
				 */
				Pattern p = Pattern.compile(source.toString());
				for (Topic t : map.getTopics()) {
					for (Name n : t.getNames()) {
						if (p.matcher(n.getValue()).matches()) {
							if (type == null || matches(context, n, type)) {
								set.add(n);
							}
						}
						if (type == null) {
							for (Variant v : n.getVariants()) {
								if (p.matcher(v.getValue()).matches()) {
									set.add(v);
								}
							}
						}
					}
					for (Occurrence o : t.getOccurrences()) {
						if (p.matcher(o.getValue()).matches()) {
							if (type == null || matches(context, o, type)) {
								set.add(o);
							}
						}
					}
				}
			}
			return set;
		}
		throw new InvalidValueException();
	}

}
