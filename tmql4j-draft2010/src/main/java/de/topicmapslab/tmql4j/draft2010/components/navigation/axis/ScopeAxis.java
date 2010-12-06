package de.topicmapslab.tmql4j.draft2010.components.navigation.axis;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.AxisScope;
import de.topicmapslab.tmql4j.draft2010.util.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.HashUtil;

public class ScopeAxis implements IAxis {

	/**
	 * {@inheritDoc}
	 */
	public IToken getIdentifier() {
		return new AxisScope();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Topic> themes = HashUtil.getHashSet();

		if (source instanceof Scoped) {
			if (type != null) {
				List<Topic> types = TypeHierarchyUtils.getSupertypes(type, true);
				types.add(type);
				for (Topic theme : ((Scoped) source).getScope()) {
					for (Topic t : theme.getTypes()) {
						if (types.contains(t)) {
							themes.add(theme);
							break;
						}
					}
				}
			} else {
				themes.addAll(((Scoped) source).getScope());
			}
		}

		return themes;
	}

}
