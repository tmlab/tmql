package de.topicmapslab.tmql4j.draft2010.components.navigation;

import java.util.Map;

import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.AssociationAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.DatatypeAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.DirectInstanceAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.DirectSubtypeAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.DirectSupertypeAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.DirectTypeAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.InstanceAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.ItemIdentifierAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.NameAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.OccurrenceAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.ParentAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.PlayerAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.ReifiedAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.ReifierAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.RoleAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.ScopeAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.ScopedAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.SubjectIdentifierAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.SubjectLocatorAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.SubtypeAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.SupertypeAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.TopicAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.TypeAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.ValueAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.axis.VariantAxis;
import de.topicmapslab.tmql4j.draft2010.components.navigation.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Association;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.AxisReifier;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.AxisScope;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Datatype;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DirectInstance;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DirectSubtype;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DirectSupertype;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.DirectType;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Instance;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.ItemIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Name;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Occurrence;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Parent;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Player;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Reified;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Role;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Scoped;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.SubjectIdentifier;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.SubjectLocator;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Subtype;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Supertype;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Topic;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Type;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Value;
import de.topicmapslab.tmql4j.draft2010.grammar.lexical.Variant;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Handler class for axis navigation. The class provides method to extract the instance of a specific axis.
 * @author Sven Krosse
 *
 */
public class Axis {

	/**
	 * A map of all axis by the lexical token which represents this axis
	 */
	private static Map<Class<? extends IToken>, Class<? extends IAxis>> axes = HashUtil
			.getHashMap();

	static {
		axes.put(Association.class, AssociationAxis.class);
		axes.put(Datatype.class, DatatypeAxis.class);
		axes.put(DirectInstance.class, DirectInstanceAxis.class);
		axes.put(DirectSubtype.class, DirectSubtypeAxis.class);
		axes.put(DirectSupertype.class, DirectSupertypeAxis.class);
		axes.put(DirectType.class, DirectTypeAxis.class);
		axes.put(Instance.class, InstanceAxis.class);
		axes.put(ItemIdentifier.class, ItemIdentifierAxis.class);
		axes.put(Name.class, NameAxis.class);
		axes.put(Occurrence.class, OccurrenceAxis.class);
		axes.put(Parent.class, ParentAxis.class);
		axes.put(Player.class, PlayerAxis.class);
		axes.put(Reified.class, ReifiedAxis.class);
		axes.put(AxisReifier.class, ReifierAxis.class);
		axes.put(Role.class, RoleAxis.class);
		axes.put(AxisScope.class, ScopeAxis.class);
		axes.put(Scoped.class, ScopedAxis.class);
		axes.put(SubjectIdentifier.class, SubjectIdentifierAxis.class);
		axes.put(SubjectLocator.class, SubjectLocatorAxis.class);
		axes.put(Subtype.class, SubtypeAxis.class);
		axes.put(Supertype.class, SupertypeAxis.class);
		axes.put(Topic.class, TopicAxis.class);
		axes.put(Type.class, TypeAxis.class);
		axes.put(Value.class, ValueAxis.class);
		axes.put(Variant.class, VariantAxis.class);
	}

	/**
	 * Returns the axis for the given lexical token
	 * @param token the token
	 * @return the axis or <code>null</code> if it does not exists any mapping for the given lexical token.
	 */
	public static IAxis getAxisForToken(Class<? extends IToken> token) {
		if (axes.containsKey(token)) {
			try {
				return axes.get(token).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
