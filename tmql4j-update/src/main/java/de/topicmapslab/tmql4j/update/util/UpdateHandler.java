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
import de.topicmapslab.tmql4j.update.components.results.IUpdateAlias;
import de.topicmapslab.tmql4j.update.exception.UpdateException;
import de.topicmapslab.tmql4j.update.grammar.tokens.Names;
import de.topicmapslab.tmql4j.update.grammar.tokens.Occurrences;
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

	public QueryMatches update(Object entry, Object value, Class<? extends IToken> anchor, Topic optionalType, boolean isSetOperation, Locator optionalDatatype) throws UpdateException {
		if (anchor.equals(AxisLocators.class)) {
			return updateLocators(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(AxisIndicators.class)) {
			return updateIndicators(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(AxisItem.class)) {
			return updateItem(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(Names.class)) {
			return updateNames(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(Occurrences.class)) {
			return updateOccurrences(entry, value, optionalType, isSetOperation, optionalDatatype);
		} else if (anchor.equals(AxisScope.class)) {
			return updateScope(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(AxisInstances.class)) {
			return updateType(value, entry, optionalType, isSetOperation);
		} else if (anchor.equals(AxisTypes.class)) {
			return updateType(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(AxisSupertypes.class)) {
			return updateSupertype(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(AxisSubtypes.class)) {
			return updateSupertype(value, entry, optionalType, isSetOperation);
		} else if (anchor.equals(AxisPlayers.class)) {
			return updatePlayers(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(AxisRoles.class)) {
			return updateRoles(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(AxisReifier.class)) {
			return updateReifier(entry, value, optionalType, isSetOperation);
		} else {
			throw new UpdateException("Unsupported anchor for update-expression: " + anchor.toString().toUpperCase());
		}
	}

	public QueryMatches updateLocators(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Topic) {
			Topic topic = (Topic) entry;
			Locator locator;
			if (value instanceof Locator) {
				locator = (Locator) value;
			} else {
				try {
					locator = topicMap.createLocator((String) value);
				} catch (Exception e) {
					throw new UpdateException("Invalid value for locator.", e);
				}
			}
			/*
			 * TODO: check merging and store for transaction management
			 */
			topic.addSubjectLocator(locator);
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

		throw new UpdateException("Entry has to be a topic.");
	}

	public QueryMatches updateIndicators(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Topic) {
			Topic topic = (Topic) entry;
			Locator locator;
			if (value instanceof Locator) {
				locator = (Locator) value;
			} else {
				try {
					locator = topicMap.createLocator((String) value);
				} catch (Exception e) {
					throw new UpdateException("Invalid value for subject-identifier.", e);
				}
			}
			/*
			 * TODO: check merging and store for transaction management
			 */
			topic.addSubjectIdentifier(locator);
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

		throw new UpdateException("Entry has to be a topic.");
	}

	public QueryMatches updateItem(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Construct) {
			Construct construct = (Construct) entry;
			Locator locator;
			if (value instanceof Locator) {
				locator = (Locator) value;
			} else {
				try {
					locator = topicMap.createLocator((String) value);
				} catch (Exception e) {
					throw new UpdateException("Invalid value for item-identifier.", e);
				}
			}
			/*
			 * TODO: check merging and store for transaction management
			 */
			construct.addItemIdentifier(locator);
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

		throw new UpdateException("Entry has to be a construct.");
	}

	public QueryMatches updateNames(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
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
		} else {
			if (entry instanceof Topic) {
				Name name = ((Topic) entry).createName(value.toString(), new Topic[0]);
				if (optionalType != null) {
					name.setType(optionalType);
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
				tuple.put(IUpdateAlias.TOPICS, ((Topic) entry).getId());
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}
			throw new UpdateException("Entry has to be a topic.");
		}
	}

	public QueryMatches updateOccurrences(Object entry, Object value, Topic optionalType, boolean isSetOperation, Locator optionalDatatype) throws UpdateException {
		if (isSetOperation) {
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
		} else {
			if (entry instanceof Topic) {
				Construct type = null;
				if (optionalType == null) {
					throw new UpdateException("Entry type is missing.");
				} else {
					type = optionalType;
				}
				Occurrence occurrence;
				if (optionalDatatype != null) {
					occurrence = ((Topic) entry).createOccurrence((Topic) type, value.toString(), optionalDatatype, new Topic[0]);
				} else {
					occurrence = ((Topic) entry).createOccurrence((Topic) type, value.toString(), new Topic[0]);
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
				tuple.put(IUpdateAlias.TOPICS, ((Topic) entry).getId());
				return QueryMatches.asQueryMatchNS(runtime, tuple);
			}
			throw new UpdateException("Entry has to be a topic.");
		}
	}

	public QueryMatches updateScope(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}
		Set<String> topicIds = HashUtil.getHashSet();
		if (entry instanceof Scoped) {
			Scoped scoped = (Scoped) entry;
			Topic theme;
			if (value instanceof Topic) {
				theme = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					theme = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					theme = topicMap.createTopicBySubjectIdentifier((Locator) value);
					topicIds.add(theme.getId());
				}
			} else {
				try {
					theme = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					theme = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					topicIds.add(theme.getId());
				}
			}
			scoped.addTheme(theme);
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

		throw new UpdateException("Entry has to be a association, name or occurrence.");
	}

	public QueryMatches updateType(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		Set<String> topicIds = HashUtil.getHashSet();
		if (isSetOperation) {
			if (entry instanceof Typed) {
				Typed typed = (Typed) entry;
				Topic type;
				if (value instanceof Topic) {
					type = (Topic) value;
				} else if (value instanceof Locator) {
					try {
						type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
					} catch (Exception e) {
						type = topicMap.createTopicBySubjectIdentifier((Locator) value);
						topicIds.add(type.getId());
					}
				} else {
					try {
						type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
					} catch (Exception e) {
						type = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
						topicIds.add(type.getId());
					}
				}
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
			Topic type;
			if (value instanceof Topic) {
				type = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					type = topicMap.createTopicBySubjectIdentifier((Locator) value);
					topicIds.add(type.getId());
				}
			} else {
				try {
					type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					type = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					topicIds.add(type.getId());
				}
			}
			topic.addType(type);
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

	public QueryMatches updateSupertype(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Topic) {
			Set<String> topicIds = HashUtil.getHashSet();
			Topic topic = (Topic) entry;
			Topic type;
			if (value instanceof Topic) {
				type = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					type = topicMap.createTopicBySubjectIdentifier((Locator) value);
					topicIds.add(type.getId());
				}
			} else {
				try {
					type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					type = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					topicIds.add(type.getId());
				}
			}

			try {
				TmdmUtility.ako(topic.getTopicMap(), type, topic);
				topicIds.add(topic.getId());
			} catch (Exception e) {
				throw new UpdateException(e);
			}
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

	public QueryMatches updateInstances(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}
		Set<String> topicIds = HashUtil.getHashSet();
		if (entry instanceof Topic) {
			Topic topic = (Topic) entry;
			Topic instance;
			if (value instanceof Topic) {
				instance = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					instance = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					instance = topicMap.createTopicBySubjectIdentifier((Locator) value);
					topicIds.add(instance.getId());
				}
			} else {
				try {
					instance = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					instance = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					topicIds.add(instance.getId());
				}
			}
			instance.addType(topic);
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

	public QueryMatches updateSubtype(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}
		Set<String> topicIds = HashUtil.getHashSet();
		if (entry instanceof Topic) {
			Topic topic = (Topic) entry;
			Topic subtype;
			if (value instanceof Topic) {
				subtype = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					subtype = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					subtype = topicMap.createTopicBySubjectIdentifier((Locator) value);
					topicIds.add(subtype.getId());
				}
			} else {
				try {
					subtype = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					subtype = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					topicIds.add(subtype.getId());
				}
			}

			try {
				TmdmUtility.ako(topic.getTopicMap(), topic, subtype);
				topicIds.add(topic.getId());
			} catch (Exception e) {
				throw new UpdateException(e);
			}
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

	public QueryMatches updatePlayers(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (!isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword SET was exprected but keyword ADD was found.");
		}

		Set<String> topicIds = HashUtil.getHashSet();
		Set<String> roleIds = HashUtil.getHashSet();

		if (entry instanceof Association) {
			Association association = (Association) entry;
			Topic player;
			if (value instanceof Topic) {
				player = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					player = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					player = topicMap.createTopicBySubjectIdentifier((Locator) value);
					topicIds.add(player.getId());
				}
			} else {
				try {
					player = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					player = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					topicIds.add(player.getId());
				}
			}

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

	public QueryMatches updateRoles(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

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

	public QueryMatches updateReifier(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (!isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword SET was exprected but keyword ADD was found.");
		}
		Set<String> topicIds = HashUtil.getHashSet();
		Reifiable reifiable;
		Topic reifier;

		if (entry instanceof Reifiable) {
			reifiable = (Reifiable) entry;
			if (value instanceof Topic) {
				reifier = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					reifier = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					reifier = topicMap.createTopicBySubjectIdentifier((Locator) value);
					topicIds.add(reifier.getId());
				}
			} else {
				try {
					reifier = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					reifier = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					topicIds.add(reifier.getId());
				}
			}
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

	private QueryMatches updateAssociation(Association association, Object roleType, Topic optionalPlayer) throws UpdateException {
		Set<String> topicIds = HashUtil.getHashSet();
		Topic roleType_;
		if (roleType instanceof Topic) {
			roleType_ = (Topic) roleType;
		} else if (roleType instanceof Locator) {
			try {
				roleType_ = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) roleType).getReference()));
			} catch (Exception e) {
				roleType_ = topicMap.createTopicBySubjectIdentifier((Locator) roleType);
				topicIds.add(roleType_.getId());
			}
		} else {
			try {
				roleType_ = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, roleType.toString());
			} catch (Exception e) {
				roleType_ = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(roleType.toString()));
				topicIds.add(roleType_.getId());
			}
		}

		Construct player = null;
		if (optionalPlayer == null) {
			player = topicMap.getConstructByItemIdentifier(topicMap.createLocator("tm:subject"));
			if (player == null) {
				player = topicMap.createTopicByItemIdentifier(topicMap.createLocator("tm:subject"));
				topicIds.add(player.getId());
			}
		} else {
			player = optionalPlayer;
		}
		/*
		 * modify result processor
		 */
		context.getTmqlProcessor().getResultProcessor().setAutoReduction(false);

		/*
		 * create results
		 */
		Map<String, Object> tuple = HashUtil.getHashMap();
		Role role = association.createRole(roleType_, (Topic) player);
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
