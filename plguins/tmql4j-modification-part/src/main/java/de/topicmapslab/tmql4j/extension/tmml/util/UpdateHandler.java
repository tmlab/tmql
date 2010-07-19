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
package de.topicmapslab.tmql4j.extension.tmml.util;

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
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.TmdmUtility;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.event.model.EventManager;
import de.topicmapslab.tmql4j.extension.tmml.event.InsertEvent;
import de.topicmapslab.tmql4j.extension.tmml.event.UpdateEvent;
import de.topicmapslab.tmql4j.extension.tmml.exception.UpdateException;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Names;
import de.topicmapslab.tmql4j.extension.tmml.grammar.tokens.Occurrences;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;
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
import de.topicmapslab.tmql4j.lexer.token.AxisTypes;

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
	 * a reference of the event manager of the TMQL4J event model
	 */
	private final EventManager eventManager;
	/**
	 * a reference of the TMQL4J runtime
	 */
	private final TMQLRuntime runtime;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param runtime
	 *            the TMQL4J runtime
	 * @throws UpdateException
	 *             thrown if topic map cannot be extracted from stack
	 */
	public UpdateHandler(TMQLRuntime runtime) throws UpdateException {
		try {
			topicMap = (TopicMap) runtime.getRuntimeContext().peek().getValue(
					VariableNames.CURRENT_MAP);
			eventManager = runtime.getEventManager();
			this.runtime = runtime;
		} catch (TMQLRuntimeException e) {
			throw new UpdateException(e);
		}
	}

	public long update(Object entry, Object value,
			Class<? extends IToken> anchor, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (anchor.equals(AxisLocators.class)) {
			return updateLocators(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(AxisIndicators.class)) {
			return updateIndicators(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(AxisItem.class)) {
			return updateItem(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(Names.class)) {
			return updateNames(entry, value, optionalType, isSetOperation);
		} else if (anchor.equals(Occurrences.class)) {
			return updateOccurrences(entry, value, optionalType, isSetOperation);
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
			throw new UpdateException(
					"Unsupported anchor for update-expression: "
							+ anchor.toString().toUpperCase());
		}
	}

	public long updateLocators(Object entry, Object value, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException(
					"Error at position 2 because keyword ADD was exprected but keyword SET was found.");
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
			eventManager.event(new UpdateEvent(topic, this, AxisLocators.class,
					locator));
			return 1;
		}

		throw new UpdateException("Entry has to be a topic.");
	}

	public long updateIndicators(Object entry, Object value,
			Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException(
					"Error at position 2 because keyword ADD was exprected but keyword SET was found.");
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
					throw new UpdateException(
							"Invalid value for subject-identifier.", e);
				}
			}
			/*
			 * TODO: check merging and store for transaction management
			 */
			topic.addSubjectIdentifier(locator);
			eventManager.event(new UpdateEvent(topic, this,
					AxisIndicators.class, locator));
			return 1;
		}

		throw new UpdateException("Entry has to be a topic.");
	}

	public long updateItem(Object entry, Object value, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException(
					"Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Construct) {
			Topic topic = (Topic) entry;
			Locator locator;
			if (value instanceof Locator) {
				locator = (Locator) value;
			} else {
				try {
					locator = topicMap.createLocator((String) value);
				} catch (Exception e) {
					throw new UpdateException(
							"Invalid value for item-identifier.", e);
				}
			}
			/*
			 * TODO: check merging and store for transaction management
			 */
			topic.addItemIdentifier(locator);
			eventManager.event(new UpdateEvent(topic, this, AxisItem.class,
					locator));
			return 1;
		}

		throw new UpdateException("Entry has to be a construct.");
	}

	public long updateNames(Object entry, Object value, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			if (entry instanceof Name) {
				((Name) entry).setValue(value.toString());
				return 1;
			}
			throw new UpdateException("Entry has to be a name.");
		} else {
			if (entry instanceof Topic) {
				Name name = ((Topic) entry).createName(value.toString(),
						new Topic[0]);
				if (optionalType != null) {
					name.setType(optionalType);
				}
				return 1;
			}
			throw new UpdateException("Entry has to be a topic.");
		}
	}

	public long updateOccurrences(Object entry, Object value,
			Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			if (entry instanceof Occurrence) {
				Occurrence occurrence = (Occurrence) entry;
				Object datatype = LiteralUtils.getDatatypeOfLiterals(runtime,
						value);
				if (datatype instanceof String) {
					occurrence.setValue(value.toString(), occurrence.getTopicMap()
									.createLocator(datatype.toString()));
				} else {
					occurrence.setValue(value.toString());
				}	
				eventManager.event(new UpdateEvent(entry, this,
						Occurrences.class, value));
				return 1;
			}
			throw new UpdateException("Entry has to be an occurrence.");
		} else {
			if (entry instanceof Topic) {
				long count = 1;
				Construct type = null;
				if (optionalType == null) {
					type = topicMap.getConstructByItemIdentifier(topicMap
							.createLocator("tm:occurrence"));
					if (type == null) {
						type = topicMap.createTopicByItemIdentifier(topicMap
								.createLocator("tm:occurrence"));
						eventManager.event(new InsertEvent(type, this));
						count++;
					}
				} else {
					type = optionalType;
				}
				Object datatype = LiteralUtils.getDatatypeOfLiterals(runtime,
						value);

				Occurrence occurrence = null;
				if (datatype instanceof String) {
					occurrence = ((Topic) entry).createOccurrence((Topic) type,
							value.toString(), ((Topic) entry).getTopicMap()
									.createLocator(datatype.toString()),
							new Topic[0]);
				} else {
					occurrence = ((Topic) entry).createOccurrence((Topic) type,
							value.toString(), new Topic[0]);
				}
				eventManager.event(new UpdateEvent(entry, this,
						Occurrences.class, occurrence));
				return count;
			}
			throw new UpdateException("Entry has to be a topic.");
		}
	}

	public long updateScope(Object entry, Object value, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException(
					"Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Scoped) {
			long count = 1;
			Scoped scoped = (Scoped) entry;
			Topic theme;
			if (value instanceof Topic) {
				theme = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					// theme = (Topic) ConstructResolver.getElementByIdentifier(
					// runtime, ((Locator) value).getReference());
					theme = (Topic) runtime.getDataBridge()
							.getConstructByIdentifier(runtime,
									(((Locator) value).getReference()));
				} catch (Exception e) {
					theme = topicMap
							.createTopicBySubjectIdentifier((Locator) value);
					eventManager.event(new InsertEvent(theme, this));
					count++;
				}
			} else {
				try {
					// theme = (Topic) ConstructResolver.getElementByIdentifier(
					// runtime, (value.toString()));
					theme = (Topic) runtime
							.getDataBridge()
							.getConstructByIdentifier(runtime, value.toString());
				} catch (Exception e) {
					theme = topicMap.createTopicBySubjectIdentifier(topicMap
							.createLocator(value.toString()));
					eventManager.event(new InsertEvent(theme, this));
					count++;
				}
			}
			scoped.addTheme(theme);
			eventManager.event(new UpdateEvent(scoped, this, AxisScope.class,
					theme));
			return count;
		}

		throw new UpdateException(
				"Entry has to be a association, name or occurrence.");
	}

	public long updateType(Object entry, Object value, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			if (entry instanceof Typed) {
				long count = 1;
				Typed typed = (Typed) entry;
				Topic type;
				if (value instanceof Topic) {
					type = (Topic) value;
				} else if (value instanceof Locator) {
					try {
						// type = (Topic) ConstructResolver
						// .getElementByIdentifier(runtime,
						// ((Locator) value).getReference());
						type = (Topic) runtime.getDataBridge()
								.getConstructByIdentifier(runtime,
										(((Locator) value).getReference()));
					} catch (Exception e) {
						type = topicMap
								.createTopicBySubjectIdentifier((Locator) value);
						eventManager.event(new InsertEvent(type, this));
						count++;
					}
				} else {
					try {
						// type = (Topic) ConstructResolver
						// .getElementByIdentifier(runtime, (value
						// .toString()));
						type = (Topic) runtime.getDataBridge()
								.getConstructByIdentifier(runtime,
										value.toString());
					} catch (Exception e) {
						type = topicMap.createTopicBySubjectIdentifier(topicMap
								.createLocator(value.toString()));
						eventManager.event(new InsertEvent(type, this));
						count++;
					}
				}
				typed.setType(type);
				eventManager.event(new UpdateEvent(typed, this,
						AxisTypes.class, type));
				return count;
			}

			throw new UpdateException(
					"Entry has to be an association, a role, a name or an occurrence.");
		} else if (entry instanceof Topic) {
			long count = 1;
			Topic topic = (Topic) entry;
			Topic type;
			if (value instanceof Topic) {
				type = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					// type = (Topic) ConstructResolver.getElementByIdentifier(
					// runtime, ((Locator) value).getReference());
					type = (Topic) runtime.getDataBridge()
							.getConstructByIdentifier(runtime,
									(((Locator) value).getReference()));
				} catch (Exception e) {
					type = topicMap
							.createTopicBySubjectIdentifier((Locator) value);
					eventManager.event(new InsertEvent(type, this));
					count++;
				}
			} else {
				try {
					// type = (Topic) ConstructResolver.getElementByIdentifier(
					// runtime, (value.toString()));
					type = (Topic) runtime
							.getDataBridge()
							.getConstructByIdentifier(runtime, value.toString());
				} catch (Exception e) {
					type = topicMap.createTopicBySubjectIdentifier(topicMap
							.createLocator(value.toString()));
					eventManager.event(new InsertEvent(type, this));
					count++;
				}
			}
			topic.addType(type);
			eventManager.event(new UpdateEvent(topic, this, AxisTypes.class,
					type));
			return count;
		}
		throw new UpdateException("Entry has to be a topic.");
	}

	public long updateSupertype(Object entry, Object value, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException(
					"Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Topic) {
			long count = 1;
			Topic topic = (Topic) entry;
			Topic type;
			if (value instanceof Topic) {
				type = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					// type = (Topic) ConstructResolver.getElementByIdentifier(
					// runtime, ((Locator) value).getReference());
					type = (Topic) runtime.getDataBridge()
							.getConstructByIdentifier(runtime,
									(((Locator) value).getReference()));
				} catch (Exception e) {
					type = topicMap
							.createTopicBySubjectIdentifier((Locator) value);
					eventManager.event(new InsertEvent(type, this));
					count++;
				}
			} else {
				try {
					// type = (Topic) ConstructResolver.getElementByIdentifier(
					// runtime, (value.toString()));
					type = (Topic) runtime
							.getDataBridge()
							.getConstructByIdentifier(runtime, value.toString());
				} catch (Exception e) {
					type = topicMap.createTopicBySubjectIdentifier(topicMap
							.createLocator(value.toString()));
					eventManager.event(new InsertEvent(type, this));
					count++;
				}
			}

			try {
				TmdmUtility.ako(topic.getTopicMap(), topic, type);
				eventManager.event(new UpdateEvent(topic, this,
						AxisSubtypes.class, type));
			} catch (Exception e) {
				throw new UpdateException(e);
			}
			return count;
		}

		throw new UpdateException("Entry has to be a topic.");
	}

	public long updateInstances(Object entry, Object value, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException(
					"Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}
		if (entry instanceof Topic) {
			long count = 1;
			Topic topic = (Topic) entry;
			Topic instance;
			if (value instanceof Topic) {
				instance = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					// instance = (Topic) ConstructResolver
					// .getElementByIdentifier(runtime, ((Locator) value)
					// .getReference());
					instance = (Topic) runtime.getDataBridge()
							.getConstructByIdentifier(runtime,
									(((Locator) value).getReference()));
				} catch (Exception e) {
					instance = topicMap
							.createTopicBySubjectIdentifier((Locator) value);
					eventManager.event(new InsertEvent(instance, this));
					count++;
				}
			} else {
				try {
					// instance = (Topic) ConstructResolver
					// .getElementByIdentifier(runtime, (value.toString()));
					instance = (Topic) runtime
							.getDataBridge()
							.getConstructByIdentifier(runtime, value.toString());
				} catch (Exception e) {
					instance = topicMap.createTopicBySubjectIdentifier(topicMap
							.createLocator(value.toString()));
					eventManager.event(new InsertEvent(instance, this));
					count++;
				}
			}
			instance.addType(topic);
			eventManager.event(new UpdateEvent(topic, this,
					AxisInstances.class, instance));
			return count;
		}
		throw new UpdateException("Entry has to be a topic.");

	}

	public long updateSubtype(Object entry, Object value, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException(
					"Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Topic) {
			long count = 1;
			Topic topic = (Topic) entry;
			Topic subtype;
			if (value instanceof Topic) {
				subtype = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					// subtype = (Topic)
					// ConstructResolver.getElementByIdentifier(
					// runtime, ((Locator) value).getReference());
					subtype = (Topic) runtime.getDataBridge()
							.getConstructByIdentifier(runtime,
									(((Locator) value).getReference()));
				} catch (Exception e) {
					subtype = topicMap
							.createTopicBySubjectIdentifier((Locator) value);
					eventManager.event(new InsertEvent(subtype, this));
					count++;
				}
			} else {
				try {
					// subtype = (Topic)
					// ConstructResolver.getElementByIdentifier(
					// runtime, (value.toString()));
					subtype = (Topic) runtime
							.getDataBridge()
							.getConstructByIdentifier(runtime, value.toString());
				} catch (Exception e) {
					subtype = topicMap.createTopicBySubjectIdentifier(topicMap
							.createLocator(value.toString()));
					eventManager.event(new InsertEvent(subtype, this));
					count++;
				}
			}

			try {
				TmdmUtility.ako(topic.getTopicMap(), subtype, topic);
				eventManager.event(new UpdateEvent(topic, this,
						AxisSubtypes.class, subtype));
			} catch (Exception e) {
				throw new UpdateException(e);
			}
			return count;
		}

		throw new UpdateException("Entry has to be a topic.");
	}

	public long updatePlayers(Object entry, Object value, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (!isSetOperation) {
			throw new UpdateException(
					"Error at position 2 because keyword SET was exprected but keyword ADD was found.");
		}

		if (entry instanceof Association) {
			long count = 0;
			Association association = (Association) entry;
			Topic player;
			if (value instanceof Topic) {
				player = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					player = (Topic) runtime.getDataBridge()
							.getConstructByIdentifier(runtime,
									(((Locator) value).getReference()));
				} catch (Exception e) {
					player = topicMap
							.createTopicBySubjectIdentifier((Locator) value);
					eventManager.event(new InsertEvent(player, this));
					count++;
				}
			} else {
				try {
					player = (Topic) runtime
							.getDataBridge()
							.getConstructByIdentifier(runtime, value.toString());
				} catch (Exception e) {
					player = topicMap.createTopicBySubjectIdentifier(topicMap
							.createLocator(value.toString()));
					eventManager.event(new InsertEvent(player, this));
					count++;
				}
			}

			for (Role role : association.getRoles()) {
				if (optionalType != null
						&& !role.getType().equals(optionalType)) {
					continue;
				}

				role.setPlayer(player);
				eventManager.event(new UpdateEvent(role, this,
						AxisPlayers.class, player));
				count++;
			}
			return count;
		}

		throw new UpdateException("Entry has to be an association.");
	}

	public long updateRoles(Object entry, Object value, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException(
					"Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Association) {
			return updateAssociation((Association) entry, value, optionalType);
		} else if (entry instanceof Topic) {
			TypeInstanceIndex index = topicMap
					.getIndex(TypeInstanceIndex.class);
			if (!index.isOpen()) {
				index.open();
			}
			long count = 0;
			for (Association association : index.getAssociations((Topic) entry)) {
				count += updateAssociation(association, value, optionalType);
			}
			return count;
		}

		throw new UpdateException("Entry has to be a topic or an association.");
	}

	public long updateReifier(Object entry, Object value, Topic optionalType,
			boolean isSetOperation) throws UpdateException {
		if (!isSetOperation) {
			throw new UpdateException(
					"Error at position 2 because keyword SET was exprected but keyword ADD was found.");
		}

		Reifiable reifiable;
		Topic reifier;
		long count = 1;

		if (entry instanceof Reifiable) {
			reifiable = (Reifiable) entry;
			if (value instanceof Topic) {
				reifier = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					// reifier = (Topic)
					// ConstructResolver.getElementByIdentifier(
					// runtime, ((Locator) value).getReference());
					reifier = (Topic) runtime.getDataBridge()
							.getConstructByIdentifier(runtime,
									(((Locator) value).getReference()));
				} catch (Exception e) {
					reifier = topicMap
							.createTopicBySubjectIdentifier((Locator) value);
					eventManager.event(new InsertEvent(reifier, this));
					count++;
				}
			} else {
				try {
					// reifier = (Topic)
					// ConstructResolver.getElementByIdentifier(
					// runtime, (value.toString()));
					reifier = (Topic) runtime
							.getDataBridge()
							.getConstructByIdentifier(runtime, value.toString());
				} catch (Exception e) {
					reifier = topicMap.createTopicBySubjectIdentifier(topicMap
							.createLocator(value.toString()));
					eventManager.event(new InsertEvent(reifier, this));
					count++;
				}
			}
		} else if (entry instanceof Topic) {
			reifier = (Topic) entry;
			if (value instanceof Reifiable) {
				reifiable = (Reifiable) value;
			} else {
				throw new UpdateException(
						"value has to be an instance of name, occurrence or association.");
			}
		} else {
			throw new UpdateException(
					"value has to be an instance of topic, name, occurrence or association.");
		}

		reifiable.setReifier(reifier);
		eventManager.event(new UpdateEvent(reifiable, this, AxisReifier.class,
				reifier));
		return count;
	}

	private long updateAssociation(Association association, Object roleType,
			Topic optionalPlayer) throws UpdateException {
		long count = 1;
		Topic role;
		if (roleType instanceof Topic) {
			role = (Topic) roleType;
		} else if (roleType instanceof Locator) {
			try {
				// role = (Topic) ConstructResolver.getElementByIdentifier(
				// runtime, ((Locator) roleType).getReference());
				role = (Topic) runtime.getDataBridge()
						.getConstructByIdentifier(runtime,
								(((Locator) roleType).getReference()));
			} catch (Exception e) {
				role = topicMap
						.createTopicBySubjectIdentifier((Locator) roleType);
				eventManager.event(new InsertEvent(role, this));
				count++;
			}
		} else {
			try {
				// role = (Topic) ConstructResolver.getElementByIdentifier(
				// runtime, (roleType.toString()));
				role = (Topic) runtime.getDataBridge()
						.getConstructByIdentifier(runtime, roleType.toString());
				;
			} catch (Exception e) {
				role = topicMap.createTopicBySubjectIdentifier(topicMap
						.createLocator(roleType.toString()));
				eventManager.event(new InsertEvent(role, this));
				count++;
			}
		}

		Construct player = null;
		if (optionalPlayer == null) {
			player = topicMap.getConstructByItemIdentifier(topicMap
					.createLocator("tm:subject"));
			if (player == null) {
				player = topicMap.createTopicByItemIdentifier(topicMap
						.createLocator("tm:subject"));
				count++;
			}
		} else {
			player = optionalPlayer;
		}

		association.createRole(role, (Topic) player);
		eventManager.event(new UpdateEvent(association, this, AxisRoles.class,
				role));
		return count;
	}

}
