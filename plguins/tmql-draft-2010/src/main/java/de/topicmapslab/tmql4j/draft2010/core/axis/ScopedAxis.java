package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;
import org.tmapi.index.ScopedIndex;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;

public class ScopedAxis implements IAxis {

	@Override
	public IToken getIdentifier() {
		return new de.topicmapslab.tmql4j.draft2010.tokens.Scoped();
	}

	@Override
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Scoped> scoped = HashUtil.getHashSet();

		if (source instanceof Topic) {
			ScopedIndex index = source.getTopicMap()
					.getIndex(ScopedIndex.class);
			if ( !index.isOpen()){
				index.open();
			}

			if (type != null) {
				Set<Topic> types = TypeHierarchyUtils.getSupertypes(type, true);
				types.add(type);
				for (Association a : index.getAssociations((Topic) source)) {
					if (types.contains(a.getType())) {
						scoped.add(a);
					}
				}
				for (Name n : index.getNames((Topic) source)) {
					if (types.contains(n.getType())) {
						scoped.add(n);
					}
				}
				for (Occurrence o : index.getOccurrences((Topic) source)) {
					if (types.contains(o.getType())) {
						scoped.add(o);
					}
				}
				for (Variant v : index.getVariants((Topic) source)) {
					if (types.contains(v.getParent().getType())) {
						scoped.add(v);
					}
				}
			} else {
				scoped.addAll(index.getAssociations((Topic) source));
				scoped.addAll(index.getNames((Topic) source));
				scoped.addAll(index.getOccurrences((Topic) source));
				scoped.addAll(index.getVariants((Topic) source));
			}
		}

		return scoped;
	}

}
