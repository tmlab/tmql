package de.topicmapslab.tmql4j.draft2010.core;

import java.util.Map;

import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.draft2010.core.axis.AssociationAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.DatatypeAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.DirectInstanceAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.DirectSubtypeAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.DirectSupertypeAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.DirectTypeAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.InstanceAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.ItemIdentifierAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.NameAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.OccurrenceAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.ParentAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.PlayerAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.ReifiedAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.ReifierAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.RoleAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.ScopeAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.ScopedAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.SubjectIdentifierAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.SubjectLocatorAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.SubtypeAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.SupertypeAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.TopicAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.TypeAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.ValueAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.VariantAxis;
import de.topicmapslab.tmql4j.draft2010.core.axis.model.IAxis;
import de.topicmapslab.tmql4j.draft2010.tokens.Association;
import de.topicmapslab.tmql4j.draft2010.tokens.Datatype;
import de.topicmapslab.tmql4j.draft2010.tokens.DirectInstance;
import de.topicmapslab.tmql4j.draft2010.tokens.DirectSubtype;
import de.topicmapslab.tmql4j.draft2010.tokens.DirectSupertype;
import de.topicmapslab.tmql4j.draft2010.tokens.DirectType;
import de.topicmapslab.tmql4j.draft2010.tokens.Instance;
import de.topicmapslab.tmql4j.draft2010.tokens.ItemIdentifier;
import de.topicmapslab.tmql4j.draft2010.tokens.Name;
import de.topicmapslab.tmql4j.draft2010.tokens.Occurrence;
import de.topicmapslab.tmql4j.draft2010.tokens.Parent;
import de.topicmapslab.tmql4j.draft2010.tokens.Player;
import de.topicmapslab.tmql4j.draft2010.tokens.Reified;
import de.topicmapslab.tmql4j.draft2010.tokens.Role;
import de.topicmapslab.tmql4j.draft2010.tokens.Scoped;
import de.topicmapslab.tmql4j.draft2010.tokens.SubjectIdentifier;
import de.topicmapslab.tmql4j.draft2010.tokens.SubjectLocator;
import de.topicmapslab.tmql4j.draft2010.tokens.Subtype;
import de.topicmapslab.tmql4j.draft2010.tokens.Supertype;
import de.topicmapslab.tmql4j.draft2010.tokens.Topic;
import de.topicmapslab.tmql4j.draft2010.tokens.Type;
import de.topicmapslab.tmql4j.draft2010.tokens.Value;
import de.topicmapslab.tmql4j.draft2010.tokens.Variant;
import de.topicmapslab.tmql4j.lexer.model.IToken;
import de.topicmapslab.tmql4j.lexer.token.AxisReifier;
import de.topicmapslab.tmql4j.lexer.token.AxisScope;

public class Axis {

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
