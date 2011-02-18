/**
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Reifiable;
import org.tmapi.core.Role;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Typed;
import org.tmapi.core.Variant;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisCharacteristics;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisIndicators;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisInstances;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisItem;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisLocators;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisPlayers;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisReifier;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisRoles;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisScope;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisSubtypes;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisSupertypes;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisTypes;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisVariants;
import de.topicmapslab.tmql4j.update.components.results.IUpdateAlias;
import de.topicmapslab.tmql4j.update.exception.UpdateException;
import de.topicmapslab.tmql4j.update.grammar.tokens.Add;
import de.topicmapslab.tmql4j.update.grammar.tokens.Names;
import de.topicmapslab.tmql4j.update.grammar.tokens.Occurrences;
import de.topicmapslab.tmql4j.update.grammar.tokens.Remove;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Utility class to handle the update of topic map items. Class handles all
 * operation which will be proceeded during the interpretation of a
 * update-expression.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class UpdateHandler {

	/**
	 * the topic map containing the items to delete
	 */
	private final TopicMap topicMap;
	/**
	 * the runtime
	 */
	private ITMQLRuntime runtime;
	/**
	 * the context
	 */
	private final IContext context;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @param context
	 *            the context
	 * @throws UpdateException
	 *             thrown if topic map cannot be extracted from stack
	 */
	public UpdateHandler(ITMQLRuntime runtime, IContext context) throws UpdateException {
		try {
			this.topicMap = context.getQuery().getTopicMap();
			this.runtime = runtime;
			this.context = context;
		} catch (TMQLRuntimeException e) {
			throw new UpdateException(e);
		}
	}

	public QueryMatches update(Object entry, Object value, Class<? extends IToken> anchor, Object optionalType, Class<? extends IToken> operator, Locator optionalDatatype) throws UpdateException {
		if (anchor.equals(AxisLocators.class)) {
			return updateLocators(entry, value, optionalType, operator);
		} else if (anchor.equals(AxisIndicators.class)) {
			return updateIndicators(entry, value, optionalType, operator);
		} else if (anchor.equals(AxisItem.class)) {
			return updateItem(entry, value, optionalType, operator);
		} else if (anchor.equals(Names.class)) {
			return updateNames(entry, value, optionalType, operator);
		} else if (anchor.equals(AxisVariants.class)) {
			return updateVariants(entry, value, optionalType, operator, optionalDatatype);
		} else if (anchor.equals(Occurrences.class)) {
			return updateOccurrences(entry, value, optionalType, operator, optionalDatatype);
		} else if (anchor.equals(AxisCharacteristics.class)) {
			if (entry instanceof Name) {
				return updateNames(entry, value, optionalType, operator);
			} else if (entry instanceof Occurrence) {
				return updateOccurrences(entry, value, optionalType, operator, optionalDatatype);
			} else {
				throw new UpdateException("Entry has to be a name or an occurrence.");
			}
		} else if (anchor.equals(AxisScope.class)) {
			return updateScope(entry, value, optionalType, operator);
		} else if (anchor.equals(AxisInstances.class)) {
			return updateType(value, entry, optionalType, operator);
		} else if (anchor.equals(AxisTypes.class)) {
			return updateType(entry, value, optionalType, operator);
		} else if (anchor.equals(AxisSupertypes.class)) {
			return updateSupertype(entry, value, optionalType, operator);
		} else if (anchor.equals(AxisSubtypes.class)) {
			return updateSupertype(value, entry, optionalType, operator);
		} else if (anchor.equals(AxisPlayers.class)) {
			return updatePlayers(entry, value, optionalType, operator);
		} else if (anchor.equals(AxisRoles.class)) {
			return updateRoles(entry, value, optionalType, operator);
		} else if (anchor.equals(AxisReifier.class)) {
			return updateReifier(entry, value, optionalType, operator);
		} else {
			throw new UpdateException("Unsupported anchor for update-expression: " + anchor.toString().toUpperCase());
		}
	}

	/**
	 * Utility method handles the modification of topic identities
	 * 
	 * @param entry
	 *            the entry which has to be a topic
	 * @param value
	 *            the value which has to be a locator
	 * @param methodName
	 *            the method name to call for modification
	 * @return the result
	 * @throws UpdateException
	 */
	private QueryMatches updateIdentity(Object entry, Object value, String methodName) throws UpdateException {
		Topic topic;
		Locator locator;
		/*
		 * check given entry
		 */
		if (entry instanceof Topic) {
			topic = (Topic) entry;
			if (value instanceof Locator) {
				locator = (Locator) value;
			} else {
				try {
					locator = topicMap.createLocator((String) value);
				} catch (Exception e) {
					throw new UpdateException("Invalid value for locator.", e);
				}
			}
		} else {
			throw new UpdateException("Entry has to be a topic.");
		}
		try {
			Method method = Topic.class.getMethod(methodName, Locator.class);
			method.invoke(topic, locator);
		} catch (Exception e) {
			throw new TMQLRuntimeException("An error occurred during the modification of identity.", e);
		}
		/*
		 * modify result processor
		 */
		context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.TOPICS);
		context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
		/*
		 * create result
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		tuple.put(IUpdateAlias.TOPICS, topic.getId());
		return QueryMatches.asQueryMatchNS(runtime, tuple);
	}

	/**
	 * Utility method to extract a topic from given entry
	 * 
	 * @param entry
	 *            the entry
	 * @param createNonExisting
	 *            flag indicates if the topic should be created by given locator
	 *            or string if not exists
	 * @param topicIds
	 *            a collection to add the new topic id or <code>null</code>
	 * @return the topic or <code>null</code> if the topic does not exists and
	 *         should not created.
	 * @throws TMQLRuntimeException
	 *             thrown if the given string is not a valid IRI
	 */
	private Topic getTopic(Object entry, boolean createNonExisting, Collection<String> topicIds) throws TMQLRuntimeException {
		/*
		 * get theme
		 */
		if (entry instanceof Topic) {
			return (Topic) entry;
		} else if (entry instanceof Locator) {
			Topic t = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) entry).getReference()));
			if (t == null && createNonExisting) {
				t = topicMap.createTopicBySubjectIdentifier((Locator) entry);
				if (topicIds != null) {
					topicIds.add(t.getId());
				}
			}
			return t;
		}
		Topic t = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, entry.toString());
		if (t == null && createNonExisting) {
			try {
				t = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(runtime.getConstructResolver().toAbsoluteIRI(context, entry.toString())));
				if (topicIds != null) {
					topicIds.add(t.getId());
				}
			} catch (Exception e) {
				throw new TMQLRuntimeException("The given IRI seems to be invalid!", e);
			}
		}
		return t;
	}

	public QueryMatches updateLocators(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		/*
		 * is add operation
		 */
		if (Add.class.equals(operator)) {
			return updateIdentity(entry, value, "addSubjectLocator");
		}
		/*
		 * is remove operation
		 */
		else if (Remove.class.equals(operator)) {
			return updateIdentity(entry, value, "removeSubjectLocator");
		}
		/*
		 * is set operation
		 */
		else {
			throw new TMQLRuntimeException("Invalid operator found at position 2. Expected ADD or REMOVE but SET was found!");
		}

	}

	public QueryMatches updateIndicators(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		/*
		 * is add operation
		 */
		if (Add.class.equals(operator)) {
			return updateIdentity(entry, value, "addSubjectIdentifier");
		}
		/*
		 * is remove operation
		 */
		else if (Remove.class.equals(operator)) {
			return updateIdentity(entry, value, "removeSubjectIdentifier");
		}
		/*
		 * is set operation
		 */
		else {
			throw new TMQLRuntimeException("Invalid operator found at position 2. Expected ADD or REMOVE but SET was found!");
		}
	}

	public QueryMatches updateItem(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		Construct construct;
		Locator locator;
		/*
		 * check given entry
		 */
		if (entry instanceof Construct) {
			construct = (Construct) entry;
			if (value instanceof Locator) {
				locator = (Locator) value;
			} else {
				try {
					locator = topicMap.createLocator((String) value);
				} catch (Exception e) {
					throw new UpdateException("Invalid value for locator.", e);
				}
			}
		} else {
			throw new UpdateException("Entry has to be a construct.");
		}

		/*
		 * is add operation
		 */
		if (Add.class.equals(operator)) {
			construct.addItemIdentifier(locator);
		}
		/*
		 * is remove operation
		 */
		else if (Remove.class.equals(operator)) {
			construct.removeItemIdentifier(locator);
		}
		/*
		 * is set operation
		 */
		else {
			throw new TMQLRuntimeException("Invalid operator found at position 2. Expected ADD or REMOVE but SET was found!");
		}
		/*
		 * modify result processor
		 */
		context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
		/*
		 * create result
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		if (construct instanceof Topic) {
			tuple.put(IUpdateAlias.TOPICS, construct.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.TOPICS);
		} else if (construct instanceof Association) {
			tuple.put(IUpdateAlias.ASSOCIATIONS, construct.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.ASSOCIATIONS);
		} else if (construct instanceof Name) {
			tuple.put(IUpdateAlias.NAMES, construct.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.NAMES);
		} else if (construct instanceof Occurrence) {
			tuple.put(IUpdateAlias.OCCURRENCES, construct.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.OCCURRENCES);
		} else if (construct instanceof Variant) {
			tuple.put(IUpdateAlias.VARIANTS, construct.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.VARIANTS);
		} else if (construct instanceof Role) {
			tuple.put(IUpdateAlias.ROLES, construct.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.ROLES);
		}
		return QueryMatches.asQueryMatchNS(runtime, tuple);
	}
	
	public QueryMatches updateNames(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		/*
		 * is set operation modifies the value of a name
		 */
		if (de.topicmapslab.tmql4j.update.grammar.tokens.Set.class.equals(operator)) {
			if (entry instanceof Name) {
				((Name) entry).setValue(value.toString());
				/*
				 * modify result processor
				 */
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.NAMES);
				context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
				/*
				 * create result
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(IUpdateAlias.NAMES, ((Name) entry).getId());
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}
			throw new UpdateException("Entry has to be a name.");
		}
		/*
		 * is remove operation removes the name of a topic
		 */
		else if (Remove.class.equals(operator)) {
			if (value instanceof Name) {
				final String id = ((Name) value).getId();
				((Name) value).remove();
				/*
				 * modify result processor
				 */
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.NAMES);
				context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
				/*
				 * create result
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(IUpdateAlias.NAMES, id);
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}
			throw new UpdateException("Entry has to be a name.");
		}
		/*
		 * is add operation adding a new name
		 */
		else {
			if (entry instanceof Topic) {
				Collection<String> topicIds = HashUtil.getHashSet();
				Name name = ((Topic) entry).createName(value.toString(), new Topic[0]);
				if (optionalType != null) {
					Topic type = getTopic(optionalType, true, topicIds);
					name.setType(type);
				}
				/*
				 * modify result processor
				 */
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.NAMES);
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(1, IUpdateAlias.TOPICS);
				context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
				/*
				 * create result
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(IUpdateAlias.NAMES, name.getId());
				if (topicIds.isEmpty()) {
					tuple.put(IUpdateAlias.TOPICS, ((Topic) entry).getId());
				} else {
					topicIds.add(((Topic) entry).getId());
					tuple.put(IUpdateAlias.TOPICS, topicIds);
				}
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}
			throw new UpdateException("Entry has to be a topic.");
		}
	}

	public QueryMatches updateVariants(Object entry, Object value, Object theme, Class<? extends IToken> operator, Locator optionalDatatype) throws UpdateException {
		/*
		 * is set operation modifies the value of a variant
		 */
		if (de.topicmapslab.tmql4j.update.grammar.tokens.Set.class.equals(operator)) {
			if (entry instanceof Variant) {
				Variant variant = (Variant) entry;
				if (optionalDatatype == null) {
					variant.setValue(value.toString());
				} else {
					variant.setValue(value.toString(), optionalDatatype);
				}
				/*
				 * modify result processor
				 */
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.VARIANTS);
				context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
				/*
				 * create result
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(IUpdateAlias.VARIANTS, variant.getId());
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}
			throw new UpdateException("Entry has to be a variant.");
		}
		/*
		 * is remove operation removes the variant of a topic
		 */
		else if (Remove.class.equals(operator)) {
			if (value instanceof Variant) {
				final String id = ((Variant) value).getId();
				((Variant) value).remove();
				/*
				 * modify result processor
				 */
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.VARIANTS);
				context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
				/*
				 * create result
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(IUpdateAlias.VARIANTS, id);
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}
			throw new UpdateException("Entry has to be a variant.");
		}
		/*
		 * is add operation adding a new occurrence
		 */
		else {
			if (entry instanceof Name) {
				Name name = (Name) entry;
				Collection<String> topicIds = HashUtil.getHashSet();
				Topic theme_ = null;
				if (theme == null) {
					throw new UpdateException("Entry type is missing.");
				} else {
					theme_ = getTopic(theme, true, topicIds);
				}
				Variant variant;
				if (optionalDatatype != null) {
					variant = name.createVariant(value.toString(), optionalDatatype, theme_);
				} else {
					variant = name.createVariant(value.toString(), theme_);
				}
				/*
				 * modify result processor
				 */
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.VARIANTS);
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(1, IUpdateAlias.NAMES);
				if ( !topicIds.isEmpty()){
					context.getTmqlProcessor().getResultProcessor().setColumnAlias(2, IUpdateAlias.TOPICS);
				}
				context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
				/*
				 * create result
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(IUpdateAlias.VARIANTS, variant.getId());
				tuple.put(IUpdateAlias.NAMES, name.getId());
				if (!topicIds.isEmpty()) {
					tuple.put(IUpdateAlias.TOPICS, topicIds.iterator().next());
				}
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}
			throw new UpdateException("Entry has to be a name.");
		}
	}

	public QueryMatches updateOccurrences(Object entry, Object value, Object optionalType, Class<? extends IToken> operator, Locator optionalDatatype) throws UpdateException {
		/*
		 * is set operation modifies the value of an occurrence
		 */
		if (de.topicmapslab.tmql4j.update.grammar.tokens.Set.class.equals(operator)) {
			if (entry instanceof Occurrence) {
				Occurrence occurrence = (Occurrence) entry;
				if (optionalDatatype == null) {
					occurrence.setValue(value.toString());
				} else {
					occurrence.setValue(value.toString(), optionalDatatype);
				}
				/*
				 * modify result processor
				 */
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.OCCURRENCES);
				context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
				/*
				 * create result
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(IUpdateAlias.OCCURRENCES, occurrence.getId());
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}
			throw new UpdateException("Entry has to be an occurrence.");
		}
		/*
		 * is remove operation removes the occurrence of a topic
		 */
		else if (Remove.class.equals(operator)) {
			if (value instanceof Occurrence) {
				final String id = ((Occurrence) value).getId();
				((Occurrence) value).remove();
				/*
				 * modify result processor
				 */
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.OCCURRENCES);
				context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
				/*
				 * create result
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(IUpdateAlias.OCCURRENCES, id);
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}
			throw new UpdateException("Entry has to be an occurrence.");
		}
		/*
		 * is add operation adding a new occurrence
		 */
		else {
			if (entry instanceof Topic) {
				Collection<String> topicIds = HashUtil.getHashSet();
				Topic type = null;
				if (optionalType == null) {
					throw new UpdateException("Entry type is missing.");
				} else {
					type = getTopic(optionalType, true, topicIds);
				}
				Occurrence occurrence;
				if (optionalDatatype != null) {
					occurrence = ((Topic) entry).createOccurrence(type, value.toString(), optionalDatatype, new Topic[0]);
				} else {
					occurrence = ((Topic) entry).createOccurrence(type, value.toString(), new Topic[0]);
				}
				/*
				 * modify result processor
				 */
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.OCCURRENCES);
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(1, IUpdateAlias.TOPICS);
				context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
				/*
				 * create result
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(IUpdateAlias.OCCURRENCES, occurrence.getId());
				if (topicIds.isEmpty()) {
					tuple.put(IUpdateAlias.TOPICS, ((Topic) entry).getId());
				} else {
					topicIds.add(((Topic) entry).getId());
					tuple.put(IUpdateAlias.TOPICS, topicIds);
				}
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}
			throw new UpdateException("Entry has to be a topic.");
		}
	}

	public QueryMatches updateScope(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		Set<String> topicIds = HashUtil.getHashSet();
		Topic theme = getTopic(value, Add.class.equals(operator), topicIds);
		Scoped scoped;
		/*
		 * get scoped element
		 */
		if (entry instanceof Scoped) {
			scoped = (Scoped) entry;
		} else {
			throw new UpdateException("Entry has to be a scoped item.");
		}
		/*
		 * is add operation
		 */
		if (Add.class.equals(operator)) {
			scoped.addTheme(theme);
		}
		/*
		 * is remove operation
		 */
		else if (Remove.class.equals(operator)) {
			if (theme != null) {
				try {
					scoped.removeTheme(theme);
				} catch (Exception e) {
					throw new TMQLRuntimeException("An error occur during the modification of scope!", e);
				}
			}
		}
		/*
		 * is set operation
		 */
		else {
			throw new TMQLRuntimeException("Invalid operator found at position 2. Expected ADD or REMOVE but SET was found!");
		}

		/*
		 * modify result processor
		 */
		context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
		/*
		 * create result
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		if (scoped instanceof Association) {
			tuple.put(IUpdateAlias.ASSOCIATIONS, scoped.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.ASSOCIATIONS);
		} else if (scoped instanceof Name) {
			tuple.put(IUpdateAlias.NAMES, scoped.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.NAMES);
		} else if (scoped instanceof Occurrence) {
			tuple.put(IUpdateAlias.OCCURRENCES, scoped.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.OCCURRENCES);
		} else if (scoped instanceof Variant) {
			tuple.put(IUpdateAlias.VARIANTS, scoped.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.VARIANTS);
		}
		if (!topicIds.isEmpty()) {
			tuple.put(IUpdateAlias.TOPICS, topicIds);
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(1, IUpdateAlias.TOPICS);
		}
		return QueryMatches.asQueryMatchNS(runtime, tuple);
	}

	public QueryMatches updateType(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		Set<String> topicIds = HashUtil.getHashSet();
		Topic type = getTopic(value, !Remove.class.equals(operator), topicIds);
		if (de.topicmapslab.tmql4j.update.grammar.tokens.Set.class.equals(operator)) {
			if (entry instanceof Typed) {
				Typed typed = (Typed) entry;
				typed.setType(type);
				/*
				 * modify result processor
				 */
				context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
				/*
				 * create result
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				if (typed instanceof Association) {
					tuple.put(IUpdateAlias.ASSOCIATIONS, typed.getId());
					context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.ASSOCIATIONS);
				} else if (typed instanceof Name) {
					tuple.put(IUpdateAlias.NAMES, typed.getId());
					context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.NAMES);
				} else if (typed instanceof Occurrence) {
					tuple.put(IUpdateAlias.OCCURRENCES, typed.getId());
					context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.OCCURRENCES);
				} else if (typed instanceof Role) {
					tuple.put(IUpdateAlias.ROLES, typed.getId());
					context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.ROLES);
				}
				if (!topicIds.isEmpty()) {
					tuple.put(IUpdateAlias.TOPICS, topicIds);
					context.getTmqlProcessor().getResultProcessor().setColumnAlias(1, IUpdateAlias.TOPICS);
				}
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}

			throw new UpdateException("Entry has to be an association, a role, a name or an occurrence.");
		} else if (entry instanceof Topic) {
			Topic topic = (Topic) entry;
			/*
			 * is add operation
			 */
			if (Add.class.equals(operator)) {
				topic.addType(type);
			}
			/*
			 * is remove operation
			 */
			else if (Remove.class.equals(operator) && type != null) {
				topic.removeType(type);
			}
			topicIds.add(topic.getId());
			/*
			 * modify result processor
			 */
			context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.TOPICS);
			/*
			 * create result
			 */
			Map<String, Object> tuple = HashUtil.getHashMap();
			tuple.put(IUpdateAlias.TOPICS, topicIds);
			return QueryMatches.asQueryMatchNS(runtime, tuple);
		}
		throw new UpdateException("Entry has to be a topic.");
	}

	@SuppressWarnings("unchecked")
	public QueryMatches updateSupertype(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		Set<String> topicIds = HashUtil.getHashSet();
		Topic topic;
		if (entry instanceof Topic) {
			topic = (Topic) entry;
		} else {
			throw new UpdateException("Entry has to be a topic.");
		}
		topicIds.add(topic.getId());
		Topic type = getTopic(value, Add.class.equals(operator), topicIds);
		Map<String, Object> tuple = HashUtil.getHashMap();
		/*
		 * is add operation
		 */
		if (Add.class.equals(operator)) {
			TmdmUtility.addSupertype(topic.getTopicMap(), type, topic, tuple);
		}
		/*
		 * is remove operation
		 */
		else if (Remove.class.equals(operator)) {
			TmdmUtility.removeSupertype(topic.getTopicMap(), type, topic, tuple);
		}
		/*
		 * is set operation
		 */
		else {
			throw new TMQLRuntimeException("Invalid operator found at position 2. Expected ADD or REMOVE but SET was found!");
		}
		/*
		 * modify result processor
		 */
		context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
		context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.TOPICS);
		if (tuple.containsKey(IUpdateAlias.ASSOCIATIONS)) {
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(1, IUpdateAlias.ASSOCIATIONS);
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(2, IUpdateAlias.ROLES);
		}
		/*
		 * create result
		 */
		if (tuple.containsKey(IUpdateAlias.TOPICS)) {
			topicIds.addAll((Collection<String>) tuple.get(IUpdateAlias.TOPICS));
		}
		tuple.put(IUpdateAlias.TOPICS, topicIds);
		return QueryMatches.asQueryMatchNS(runtime, tuple);
	}

	public QueryMatches updateInstances(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		return updateType(value, entry, optionalType, operator);

	}

	public QueryMatches updateSubtype(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		return updateSupertype(value, entry, optionalType, operator);
	}

	public QueryMatches updatePlayers(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		if (!de.topicmapslab.tmql4j.update.grammar.tokens.Set.class.equals(operator)) {
			throw new UpdateException("Error at position 2 because keyword SET was exprected but keyword ADD was found.");
		}

		Set<String> topicIds = HashUtil.getHashSet();
		Set<String> roleIds = HashUtil.getHashSet();
		Topic player = getTopic(value, true, topicIds);
		Association association;
		if (entry instanceof Association) {
			association = (Association) entry;
			for (Role role : association.getRoles()) {
				if (optionalType != null && !role.getType().equals(optionalType)) {
					continue;
				}
				role.setPlayer(player);
				roleIds.add(role.getId());
			}
			/*
			 * modify result processor
			 */
			context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
			/*
			 * create result
			 */
			Map<String, Object> tuple = HashUtil.getHashMap();
			if (!topicIds.isEmpty()) {
				tuple.put(IUpdateAlias.TOPICS, topicIds);
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(1, IUpdateAlias.TOPICS);
			}
			if (!roleIds.isEmpty()) {
				tuple.put(IUpdateAlias.ROLES, roleIds);
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.ROLES);
			}
			if (tuple.isEmpty()) {
				return QueryMatches.emptyMatches();
			}
			return QueryMatches.asQueryMatchNS(runtime, tuple);
		}
		throw new UpdateException("Entry has to be an association.");
	}

	public QueryMatches updateRoles(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		/*
		 * is add operation
		 */
		if (Add.class.equals(operator)) {
			if (entry instanceof Association) {
				return updateAssociation((Association) entry, value, optionalType);
			} else if (entry instanceof Topic) {
				TypeInstanceIndex index = topicMap.getIndex(TypeInstanceIndex.class);
				if (!index.isOpen()) {
					index.open();
				}
				QueryMatches matches = new QueryMatches(runtime);
				for (Association association : index.getAssociations((Topic) entry)) {
					matches.add(updateAssociation(association, value, optionalType).getMatches());
				}
				return matches;
			}
			throw new UpdateException("Entry has to be a topic or an association.");
		}
		/*
		 * is remove operation
		 */
		else if (Remove.class.equals(operator)) {
			if (value instanceof Role) {
				Role role = (Role) value;
				final String id = role.getId();
				role.remove();
				/*
				 * modify result processor
				 */
				context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
				context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.ROLES);

				/*
				 * create results
				 */
				Map<String, Object> tuple = HashUtil.getHashMap();
				tuple.put(IUpdateAlias.ROLES, id);
				QueryMatches matches = new QueryMatches(runtime);
				matches.add(tuple);
				return matches;
			}
			throw new UpdateException("Entry has to be a role.");
		}
		/*
		 * is set operation
		 */
		else {
			throw new UpdateException("Error at position 2 because keyword ADD or REMOVE was exprected but keyword SET was found.");
		}
	}

	public QueryMatches updateReifier(Object entry, Object value, Object optionalType, Class<? extends IToken> operator) throws UpdateException {
		if (!de.topicmapslab.tmql4j.update.grammar.tokens.Set.class.equals(operator)) {
			throw new UpdateException("Error at position 2 because keyword SET was exprected but keyword ADD was found.");
		}
		Set<String> topicIds = HashUtil.getHashSet();
		Reifiable reifiable;
		Topic reifier = null;

		if (entry instanceof Reifiable) {
			reifiable = (Reifiable) entry;
			reifier = value == null ? null : getTopic(value, true, topicIds);
		} else if (entry instanceof Topic) {
			reifier = (Topic) entry;
			if (value instanceof Reifiable) {
				reifiable = (Reifiable) value;
			} else {
				throw new UpdateException("value has to be an instance of name, occurrence or association.");
			}
		} else {
			throw new UpdateException("value has to be an instance of topic, name, occurrence or association.");
		}
		reifiable.setReifier(reifier);
		/*
		 * modify result processor
		 */
		context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);
		/*
		 * create results
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		if (reifiable instanceof Name) {
			tuple.put(IUpdateAlias.NAMES, reifiable.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.NAMES);
		} else if (reifiable instanceof Occurrence) {
			tuple.put(IUpdateAlias.OCCURRENCES, reifiable.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.OCCURRENCES);
		} else if (reifiable instanceof Variant) {
			tuple.put(IUpdateAlias.VARIANTS, reifiable.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.VARIANTS);
		} else if (reifiable instanceof Association) {
			tuple.put(IUpdateAlias.ASSOCIATIONS, reifiable.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.ASSOCIATIONS);
		} else if (reifiable instanceof Role) {
			tuple.put(IUpdateAlias.ROLES, reifiable.getId());
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.ROLES);
		}
		if (!topicIds.isEmpty()) {
			tuple.put(IUpdateAlias.TOPICS, topicIds);
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(1, IUpdateAlias.TOPICS);
		}
		QueryMatches matches = new QueryMatches(runtime);
		matches.add(tuple);
		return matches;
	}

	private QueryMatches updateAssociation(Association association, Object player, Object roleType) throws UpdateException {
		Set<String> topicIds = HashUtil.getHashSet();
		Topic roleType_ = null;
		if (roleType == null) {
			throw new TMQLRuntimeException("Type of role cannot be null!");
		} else {
			roleType_ = getTopic(roleType, true, topicIds);
		}
		getTopic(roleType, true, topicIds);
		Topic player_ = null;
		if (player == null) {
			throw new TMQLRuntimeException("Player of role cannot be null!");
		} else {
			player_ = getTopic(player, true, topicIds);
		}
		/*
		 * modify result processor
		 */
		context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);

		/*
		 * create results
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		Role role = association.createRole(roleType_, (Topic) player_);
		tuple.put(IUpdateAlias.ROLES, role.getId());
		context.getTmqlProcessor().getResultProcessor().setColumnAlias(1, IUpdateAlias.ROLES);
		tuple.put(IUpdateAlias.ASSOCIATIONS, association.getId());
		context.getTmqlProcessor().getResultProcessor().setColumnAlias(0, IUpdateAlias.ASSOCIATIONS);
		if (!topicIds.isEmpty()) {
			tuple.put(IUpdateAlias.TOPICS, topicIds);
			context.getTmqlProcessor().getResultProcessor().setColumnAlias(2, IUpdateAlias.TOPICS);
		}
		QueryMatches matches = new QueryMatches(runtime);
		matches.add(tuple);
		return matches;
	}

}
