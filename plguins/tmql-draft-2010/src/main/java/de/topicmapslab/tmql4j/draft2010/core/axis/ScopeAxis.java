package de.topicmapslab.tmql4j.draft2010.core.axis;

import java.util.Collection;
import java.util.Set;

import org.tmapi.core.Construct;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.utility.TypeHierarchyUtils;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.AxisScope;

public class ScopeAxis implements IAxis {

	
	public IToken getIdentifier() {
		return new AxisScope();
	}

	
	public Collection<?> navigate(Construct source, Topic type)
			throws TMQLRuntimeException {
		Set<Topic> themes = HashUtil.getHashSet();

		if (source instanceof Scoped) {
			if (type != null) {
				Set<Topic> types = TypeHierarchyUtils.getSupertypes(type, true);
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
