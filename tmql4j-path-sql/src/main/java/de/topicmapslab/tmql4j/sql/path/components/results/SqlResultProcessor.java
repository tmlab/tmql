/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.results;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.postgresql.util.PGobject;

import de.topicmapslab.majortom.core.LocatorImpl;
import de.topicmapslab.majortom.database.jdbc.model.ISession;
import de.topicmapslab.majortom.database.store.JdbcIdentity;
import de.topicmapslab.majortom.model.core.IAssociation;
import de.topicmapslab.majortom.model.core.IConstructFactory;
import de.topicmapslab.majortom.model.core.IName;
import de.topicmapslab.majortom.model.core.ITopic;
import de.topicmapslab.majortom.model.core.ITopicMap;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.TmqlResultProcessor;
import de.topicmapslab.tmql4j.components.processor.results.model.IResult;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.query.IQuery;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;
import de.topicmapslab.tmql4j.sql.path.utils.ISqlConstants;
import de.topicmapslab.tmql4j.util.HashUtil;
import de.topicmapslab.tmql4j.util.LiteralUtils;

/**
 * @author Sven Krosse
 * 
 */
public class SqlResultProcessor extends TmqlResultProcessor {

	/**
	 * 
	 */
	private static final String VARIANT = "v";
	/**
	 * 
	 */
	private static final String NAME = "n";
	/**
	 * 
	 */
	private static final String OCCURRENCE = "o";
	/**
	 * 
	 */
	private static final String ROLE = "r";
	/**
	 * 
	 */
	private static final String ASSOCIATION = "a";
	/**
	 * 
	 */
	private static final String TOPIC = "t";
	/**
	 * 
	 */
	private static final String LOCATOR = "l";
	private static final String QUERY_RESOLVE_IDS = "WITH ids AS ( SELECT unnest(?) AS id) SELECT id, p, pp, type, reference FROM ("
			+ "SELECT id, -1 AS p, NULL AS pp, 'l' AS type, reference FROM locators WHERE id IN ( SELECT id FROM ids ) " + "UNION "
			+ "SELECT id, id_parent AS p, -1 AS pp, 't' AS type, NULL AS reference FROM topics WHERE id IN ( SELECT id FROM ids ) " + "UNION "
			+ "SELECT id, id_parent AS p, -1 AS pp, 'a' AS type, NULL AS reference  FROM associations WHERE id IN ( SELECT id FROM ids ) " + "UNION "
			+ "SELECT id, id_parent AS p, -1 AS pp, 'n' AS type, NULL AS reference FROM names WHERE id IN ( SELECT id FROM ids ) " + "UNION "
			+ "SELECT id, id_parent AS p, -1 AS pp, 'o' AS type, NULL AS reference FROM occurrences WHERE id IN ( SELECT id FROM ids ) " + "UNION "
			+ "SELECT id, id_parent AS p, -1 AS pp, 'r' AS type, NULL AS reference FROM roles WHERE id IN ( SELECT id FROM ids ) " + "UNION "
			+ "SELECT v.id, v.id_parent AS p, n.id_parent AS pp, 'v' AS type, NULL AS reference FROM names AS n, variants AS v WHERE n.id = v.id_parent AND v.id IN ( SELECT id FROM ids )) AS content";

	/**
	 * constructor
	 * 
	 * @param runtime
	 */
	public SqlResultProcessor(ITMQLRuntime runtime) {
		super(runtime);
	}

	/**
	 * {@inheritDoc}
	 */
	public void proceed(QueryMatches matches) throws TMQLRuntimeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * proceed the JDBC result set
	 * 
	 * @param definition
	 *            the SQL definition
	 * @param query
	 *            the query
	 * @param session
	 *            the session
	 * @param rs
	 *            the result set
	 * @throws TMQLRuntimeException
	 *             thrown if anything fails
	 */
	public void proceed(ISqlDefinition definition, IQuery query, ISession session, ResultSet rs) throws TMQLRuntimeException {
		try {
			/*
			 * create new instance of result set
			 */
			SqlResultSet resultSet = new SqlResultSet(getRuntime().getTopicMapSystem(), query.getTopicMap());
			resultSet.setAlias(getAliasIndex());
			Map<String, List<Index>> indexes = HashUtil.getHashMap();
			Set<String> ids = HashUtil.getHashSet();
			ResultSetMetaData metaData = rs.getMetaData();
			List<SqlTables> tables = new ArrayList<SqlTables>();
			for (ISelection sel : definition.getSelectionParts()) {
				SqlTables t = sel.getCurrentTable();
				tables.add(t == null ? SqlTables.ANY : t);
			}
			int row = 0;

			/*
			 * iterate over JDBC result set
			 */
			while (rs.next()) {
				IResult result = resultSet.createResult();
				boolean onlyNullValues = true;
				int resultIndex = 0;
				for (int col = 1; col < metaData.getColumnCount() + 1; col++) {
					SqlTables selectionType = tables.get(col - 1);
					int columnType = metaData.getColumnType(col);
					String value = rs.getString(col);
					if (ISqlConstants.IS_NULL_VALUE_IN_SQL.equalsIgnoreCase(value)) {
						value = null;
					}

					/*
					 * store value if value is an id
					 */
					if (value != null) {
						if (selectionType == SqlTables.INTEGER) {
							result.add(BigInteger.valueOf(rs.getLong(col)));
						} else if (selectionType == SqlTables.STRING) {
							result.add(value);
						} else if (selectionType == SqlTables.DECIMAL) {
							result.add(BigDecimal.valueOf(rs.getDouble(col)));
						} else if (selectionType == SqlTables.BOOLEAN) {
							result.add(rs.getBoolean(col));
						} else if (columnType == Types.BIGINT) {

							/*
							 * is topic map
							 */
							if (query.getTopicMap().getId().equalsIgnoreCase(value.toString())) {
								result.add(query.getTopicMap());
							}
							/*
							 * is any construct or locator except the topic map
							 */
							else if (!"-1".equalsIgnoreCase(value)) {
								/*
								 * store id value
								 */
								ids.add(value);
								/*
								 * store index of id in result set
								 */
								List<Index> list = indexes.get(value);
								if (list == null) {
									list = HashUtil.getList();
									indexes.put(value, list);
								}
								list.add(new Index(row, resultIndex));
								result.add(value);
							}
						} else if (selectionType.isRecord()) {
							Object o = rs.getObject(col);
							if (o instanceof PGobject) {
								for (String id : toArray((PGobject) o)) {
									if (selectionType.isConstructRecord()) {
										ids.add(id);
										List<Index> list = indexes.get(id);
										if (list == null) {
											list = HashUtil.getList();
											indexes.put(id, list);
										}
										list.add(new Index(row, resultIndex));
									}
									resultIndex++;
									result.add(id);
								}
								resultIndex--;
							} else {
								throw new TMQLRuntimeException("Unsupported result type!");
							}

						}
						onlyNullValues = false;
					} else {
						result.add(value);
					}
					resultIndex++;
				}
				if (!onlyNullValues && result.size() > 0 || (onlyNullValues && result.size() > 1)) {
					resultSet.addResult(result);
				}
				row++;
			}
			/*
			 * sent request to database for resolve id
			 */
			if (!ids.isEmpty()) {
				PreparedStatement stmt = session.getConnection().prepareStatement(QUERY_RESOLVE_IDS);
				Array array = session.getConnection().createArrayOf(ISqlConstants.ISqlTypes.BIGINT, ids.toArray());
				stmt.setArray(1, array);
				ResultSet r = stmt.executeQuery();
				while (r.next()) {
					long id = r.getLong(1);
					long idP = r.getLong(2);
					long idPP = r.getLong(3);
					String type = r.getString(4);
					String reference = r.getString(5);
					setResult((ITopicMap) query.getTopicMap(), resultSet, indexes.get(Long.toString(id)), id, idP, idPP, type, reference);
				}
			}
			/*
			 * set internal reference
			 */
			setResultSet(resultSet);
		} catch (SQLException e) {
			throw new TMQLRuntimeException("Unexpected end of connection to SQL database!", e);
		}
	}

	/**
	 * Transforms the given {@link PGobject} to a set of IDs
	 * 
	 * @param obj
	 *            the object
	 * @return the set of IDs
	 */
	private Set<String> toArray(PGobject obj) {
		String value = obj.getValue();
		if (value.length() > 2) {
			StringTokenizer tokenizer = new StringTokenizer(value.substring(1, value.length() - 1), ",");
			Set<String> set = HashUtil.getHashSet();
			while (tokenizer.hasMoreTokens()) {
				set.add(LiteralUtils.asNonQuotedString(tokenizer.nextToken()));
			}
			return set;
		}
		return Collections.emptySet();
	}

	/**
	 * Utility method to convert a result set row to a TMAPI construct and
	 * replace all IDs by the construct
	 * 
	 * @param topicMap
	 *            the topic map
	 * @param resultSet
	 *            the result set to replace
	 * @param indexes
	 *            the indexes to replace
	 * @param id
	 *            the construct id
	 * @param idP
	 *            the id of the parent
	 * @param idPP
	 *            the id of the parent of the parent
	 * @param type
	 *            the type of construct
	 * @param reference
	 *            the reference
	 */
	private void setResult(ITopicMap topicMap, SqlResultSet resultSet, List<Index> indexes, long id, long idP, long idPP, String type, String reference) {
		final IConstructFactory factory = topicMap.getStore().getConstructFactory();
		/*
		 * create real object instead of IDs
		 */
		final Object object;
		/*
		 * is locator
		 */
		if (type.equalsIgnoreCase(LOCATOR)) {
			object = new LocatorImpl(reference, Long.toString(id));
		}
		/*
		 * is topic
		 */
		else if (type.equalsIgnoreCase(TOPIC)) {
			object = factory.newTopic(new JdbcIdentity(id), topicMap);
		}
		/*
		 * is association
		 */
		else if (type.equalsIgnoreCase(ASSOCIATION)) {
			object = factory.newAssociation(new JdbcIdentity(id), topicMap);
		}
		/*
		 * is role
		 */
		else if (type.equalsIgnoreCase(ROLE)) {
			IAssociation a = factory.newAssociation(new JdbcIdentity(idP), topicMap);
			object = factory.newAssociationRole(new JdbcIdentity(id), a);
		}
		/*
		 * is occurrence
		 */
		else if (type.equalsIgnoreCase(OCCURRENCE)) {
			ITopic t = factory.newTopic(new JdbcIdentity(idP), topicMap);
			object = factory.newOccurrence(new JdbcIdentity(id), t);
		}
		/*
		 * is name
		 */
		else if (type.equalsIgnoreCase(NAME)) {
			ITopic t = factory.newTopic(new JdbcIdentity(idP), topicMap);
			object = factory.newName(new JdbcIdentity(id), t);
		}
		/*
		 * is variant
		 */
		else if (type.equalsIgnoreCase(VARIANT)) {
			ITopic t = factory.newTopic(new JdbcIdentity(idPP), topicMap);
			IName n = factory.newName(new JdbcIdentity(idP), t);
			object = factory.newVariant(new JdbcIdentity(id), n);
		} else {
			throw new TMQLRuntimeException("Invalid type '" + type + "' of results!");
		}
		/*
		 * set real value add stored indexes
		 */
		for (Index index : indexes) {
			resultSet.set(index.row, index.column, object);
		}
	}

	/**
	 * Utility bean to store the index of result set
	 * 
	 * @author Sven Krosse
	 * 
	 */
	private class Index {
		int row;
		int column;

		/**
		 * constructor
		 * 
		 * @param row
		 *            the row index
		 * @param col
		 *            the column index
		 */
		public Index(int row, int col) {
			this.row = row;
			this.column = col;
		}
	}

}
