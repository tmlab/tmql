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

import de.topicmapslab.tmql4j.components.processor.core.IContext;
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
import de.topicmapslab.tmql4j.update.exception.UpdateException;
import de.topicmapslab.tmql4j.update.grammar.tokens.Names;
import de.topicmapslab.tmql4j.update.grammar.tokens.Occurrences;

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
	private IContext context;

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
		} catch (TMQLRuntimeException e) {
			throw new UpdateException(e);
		}
	}

	public long update(Object entry, Object value, Class<? extends IToken> anchor, Topic optionalType, boolean isSetOperation, Locator optionalDatatype) throws UpdateException {
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

	public long updateLocators(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
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
			return 1;
		}

		throw new UpdateException("Entry has to be a topic.");
	}

	public long updateIndicators(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
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
			return 1;
		}

		throw new UpdateException("Entry has to be a topic.");
	}

	public long updateItem(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
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
					throw new UpdateException("Invalid value for item-identifier.", e);
				}
			}
			/*
			 * TODO: check merging and store for transaction management
			 */
			topic.addItemIdentifier(locator);
			return 1;
		}

		throw new UpdateException("Entry has to be a construct.");
	}

	public long updateNames(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			if (entry instanceof Name) {
				((Name) entry).setValue(value.toString());
				return 1;
			}
			throw new UpdateException("Entry has to be a name.");
		} else {
			if (entry instanceof Topic) {
				Name name = ((Topic) entry).createName(value.toString(), new Topic[0]);
				if (optionalType != null) {
					name.setType(optionalType);
				}
				return 1;
			}
			throw new UpdateException("Entry has to be a topic.");
		}
	}

	public long updateOccurrences(Object entry, Object value, Topic optionalType, boolean isSetOperation, Locator optionalDatatype) throws UpdateException {
		if (isSetOperation) {
			if (entry instanceof Occurrence) {
				Occurrence occurrence = (Occurrence) entry;
				if (optionalDatatype == null) {
					occurrence.setValue(value.toString());
				} else {
					occurrence.setValue(value.toString(), optionalDatatype);
				}
				return 1;
			}
			throw new UpdateException("Entry has to be an occurrence.");
		} else {
			if (entry instanceof Topic) {
				long count = 1;
				Construct type = null;
				if (optionalType == null) {
					throw new UpdateException("Entry type is missing.");
				} else {
					type = optionalType;
				}
				if (optionalDatatype != null) {
					((Topic) entry).createOccurrence((Topic) type, value.toString(), optionalDatatype, new Topic[0]);
				} else {
					((Topic) entry).createOccurrence((Topic) type, value.toString(), new Topic[0]);
				}
				return count;
			}
			throw new UpdateException("Entry has to be a topic.");
		}
	}

	public long updateScope(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Scoped) {
			long count = 1;
			Scoped scoped = (Scoped) entry;
			Topic theme;
			if (value instanceof Topic) {
				theme = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					theme = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					theme = topicMap.createTopicBySubjectIdentifier((Locator) value);
					count++;
				}
			} else {
				try {
					theme = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					theme = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					count++;
				}
			}
			scoped.addTheme(theme);
			return count;
		}

		throw new UpdateException("Entry has to be a association, name or occurrence.");
	}

	public long updateType(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			if (entry instanceof Typed) {
				long count = 1;
				Typed typed = (Typed) entry;
				Topic type;
				if (value instanceof Topic) {
					type = (Topic) value;
				} else if (value instanceof Locator) {
					try {
						type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
					} catch (Exception e) {
						type = topicMap.createTopicBySubjectIdentifier((Locator) value);
						count++;
					}
				} else {
					try {
						type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
					} catch (Exception e) {
						type = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
						count++;
					}
				}
				typed.setType(type);
				return count;
			}

			throw new UpdateException("Entry has to be an association, a role, a name or an occurrence.");
		} else if (entry instanceof Topic) {
			long count = 1;
			Topic topic = (Topic) entry;
			Topic type;
			if (value instanceof Topic) {
				type = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					type = topicMap.createTopicBySubjectIdentifier((Locator) value);
					count++;
				}
			} else {
				try {
					type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					type = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					count++;
				}
			}
			topic.addType(type);
			return count;
		}
		throw new UpdateException("Entry has to be a topic.");
	}

	public long updateSupertype(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Topic) {
			long count = 1;
			Topic topic = (Topic) entry;
			Topic type;
			if (value instanceof Topic) {
				type = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					type = topicMap.createTopicBySubjectIdentifier((Locator) value);
					count++;
				}
			} else {
				try {
					type = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					type = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					count++;
				}
			}

			try {
				TmdmUtility.ako(topic.getTopicMap(), type, topic);
			} catch (Exception e) {
				throw new UpdateException(e);
			}
			return count;
		}

		throw new UpdateException("Entry has to be a topic.");
	}

	public long updateInstances(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}
		if (entry instanceof Topic) {
			long count = 1;
			Topic topic = (Topic) entry;
			Topic instance;
			if (value instanceof Topic) {
				instance = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					instance = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					instance = topicMap.createTopicBySubjectIdentifier((Locator) value);
					count++;
				}
			} else {
				try {
					instance = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					instance = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					count++;
				}
			}
			instance.addType(topic);
			return count;
		}
		throw new UpdateException("Entry has to be a topic.");

	}

	public long updateSubtype(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword ADD was exprected but keyword SET was found.");
		}

		if (entry instanceof Topic) {
			long count = 1;
			Topic topic = (Topic) entry;
			Topic subtype;
			if (value instanceof Topic) {
				subtype = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					subtype = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					subtype = topicMap.createTopicBySubjectIdentifier((Locator) value);
					count++;
				}
			} else {
				try {
					subtype = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					subtype = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					count++;
				}
			}

			try {
				TmdmUtility.ako(topic.getTopicMap(), topic, subtype);
			} catch (Exception e) {
				throw new UpdateException(e);
			}
			return count;
		}

		throw new UpdateException("Entry has to be a topic.");
	}

	public long updatePlayers(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (!isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword SET was exprected but keyword ADD was found.");
		}

		if (entry instanceof Association) {
			long count = 0;
			Association association = (Association) entry;
			Topic player;
			if (value instanceof Topic) {
				player = (Topic) value;
			} else if (value instanceof Locator) {
				try {
					player = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					player = topicMap.createTopicBySubjectIdentifier((Locator) value);
					count++;
				}
			} else {
				try {
					player = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					player = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					count++;
				}
			}

			for (Role role : association.getRoles()) {
				if (optionalType != null && !role.getType().equals(optionalType)) {
					continue;
				}

				role.setPlayer(player);
				count++;
			}
			return count;
		}

		throw new UpdateException("Entry has to be an association.");
	}

	public long updateRoles(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
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
			long count = 0;
			for (Association association : index.getAssociations((Topic) entry)) {
				count += updateAssociation(association, value, optionalType);
			}
			return count;
		}

		throw new UpdateException("Entry has to be a topic or an association.");
	}

	public long updateReifier(Object entry, Object value, Topic optionalType, boolean isSetOperation) throws UpdateException {
		if (!isSetOperation) {
			throw new UpdateException("Error at position 2 because keyword SET was exprected but keyword ADD was found.");
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
					reifier = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) value).getReference()));
				} catch (Exception e) {
					reifier = topicMap.createTopicBySubjectIdentifier((Locator) value);
					count++;
				}
			} else {
				try {
					reifier = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, value.toString());
				} catch (Exception e) {
					reifier = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(value.toString()));
					count++;
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
		return count;
	}

	private long updateAssociation(Association association, Object roleType, Topic optionalPlayer) throws UpdateException {
		long count = 1;
		Topic role;
		if (roleType instanceof Topic) {
			role = (Topic) roleType;
		} else if (roleType instanceof Locator) {
			try {
				role = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, (((Locator) roleType).getReference()));
			} catch (Exception e) {
				role = topicMap.createTopicBySubjectIdentifier((Locator) roleType);
				count++;
			}
		} else {
			try {
				role = (Topic) runtime.getConstructResolver().getConstructByIdentifier(context, roleType.toString());
				;
			} catch (Exception e) {
				role = topicMap.createTopicBySubjectIdentifier(topicMap.createLocator(roleType.toString()));
				count++;
			}
		}

		Construct player = null;
		if (optionalPlayer == null) {
			player = topicMap.getConstructByItemIdentifier(topicMap.createLocator("tm:subject"));
			if (player == null) {
				player = topicMap.createTopicByItemIdentifier(topicMap.createLocator("tm:subject"));
				count++;
			}
		} else {
			player = optionalPlayer;
		}

		association.createRole(role, (Topic) player);
		return count;
	}

}
