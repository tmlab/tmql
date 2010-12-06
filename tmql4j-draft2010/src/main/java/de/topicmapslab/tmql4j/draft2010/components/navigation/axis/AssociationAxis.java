package de.topicmapslab.tmql4j.draft2010.components.navigation.axis;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Association;
import de.topicmapslab.tmql4j.draft2010.util.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.HashUtil;

public class AssociationAxis implements IAxis {
	/**
	 * {@inheritDoc}
	 */
	public IToken getIdentifier() {
		return new Association();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigate(Construct source, final Topic type)
			throws TMQLRuntimeException {
		Set<org.tmapi.core.Association> associations = HashUtil.getHashSet();

		if (source instanceof TopicMap) {
			if (type != null) {
				final List<Topic> types = TypeHierarchyUtils.getSubtypes(type,
						true);
				types.add(type);
				for (Topic t : types) {
					TypeInstanceIndex index = ((TopicMap) source).getIndex(
							TypeInstanceIndex.class);
					if ( !index.isOpen()){
						index.open();
					}
					associations.addAll(index.getAssociations(t));
				}
			} else {
				associations.addAll(((TopicMap) source).getAssociations());
			}
		}

		return associations;
	}

}
