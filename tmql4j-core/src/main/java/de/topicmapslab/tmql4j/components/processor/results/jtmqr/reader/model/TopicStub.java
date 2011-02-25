package de.topicmapslab.tmql4j.components.processor.results.jtmqr.reader.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.tmapi.core.IdentityConstraintException;
import org.tmapi.core.Locator;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Reifiable;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicInUseException;
import org.tmapi.core.TopicMap;

import de.topicmapslab.majortom.core.LocatorImpl;

/**
 * topic implementation for jtmqr result set
 * 
 * @author Christian Haß
 * 
 */
public class TopicStub extends ConstructStub implements Topic {

	private final Set<Locator> subjectIdentifier;
	private final Set<Locator> subjectLocator;
	private Set<Topic> types;
	private Set<Name> names;
	private Set<Occurrence> occurrences;

	/**
	 * constructor
	 */
	protected TopicStub() {

		this.subjectIdentifier = new HashSet<Locator>();
		this.subjectLocator = new HashSet<Locator>();
		this.types = Collections.emptySet();
		this.names = Collections.emptySet();
		this.occurrences = Collections.emptySet();
	}

	/**
	 * adds a subject identifier
	 * 
	 * @param iri
	 *            - the iri as string
	 */
	protected void _addSubjectIdentifier(String iri) {
		Locator l = new LocatorImpl(iri);
		this.subjectIdentifier.add(l);
	}

	/**
	 * adds a set of subject identifier
	 * 
	 * @param sis
	 *            - set of subject identifier as strings
	 */
	protected void _setSubjectIdentifiers(Set<String> sis) {
		for (String si : sis) {
			_addSubjectIdentifier(si);
		}
	}

	/**
	 * adds a subject locator
	 * 
	 * @param iri
	 *            - the iri as string
	 */
	protected void _addSubjectLocator(String iri) {
		Locator l = new LocatorImpl(iri);
		this.subjectLocator.add(l);
	}

	/**
	 * adds a set of subject locator
	 * 
	 * @param sis
	 *            - set of subject locator as strings
	 */
	protected void _setSubjectLocators(Set<String> sis) {
		for (String si : sis) {
			_addSubjectLocator(si);
		}
	}

	/**
	 * sets the types
	 * 
	 * @param types
	 *            - the types
	 */
	protected void _setTypes(Set<Topic> types) {
		this.types = types;
	}

	/**
	 * sets the name
	 * 
	 * @param names
	 *            - the names
	 */
	protected void _setNames(Set<Name> names) {
		this.names = names;
	}

	/**
	 * sets the occurrences
	 * 
	 * @param occurrences
	 *            - the occurrences
	 */
	protected void _setOccurrences(Set<Occurrence> occurrences) {
		this.occurrences = occurrences;
	}

	// --[ TMAPI methods ]---------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Locator> getItemIdentifiers() {
		return this.itemIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TopicMap getTopicMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeItemIdentifier(Locator arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addItemIdentifier(Locator arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addSubjectIdentifier(Locator arg0) throws IdentityConstraintException, ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addSubjectLocator(Locator arg0) throws IdentityConstraintException, ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addType(Topic arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Name createName(String arg0, Topic... arg1) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Name createName(String arg0, Collection<Topic> arg1) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Name createName(Topic arg0, String arg1, Topic... arg2) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Name createName(Topic arg0, String arg1, Collection<Topic> arg2) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Occurrence createOccurrence(Topic arg0, String arg1, Topic... arg2) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Occurrence createOccurrence(Topic arg0, String arg1, Collection<Topic> arg2) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Occurrence createOccurrence(Topic arg0, Locator arg1, Topic... arg2) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Occurrence createOccurrence(Topic arg0, Locator arg1, Collection<Topic> arg2) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Occurrence createOccurrence(Topic arg0, String arg1, Locator arg2, Topic... arg3) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Occurrence createOccurrence(Topic arg0, String arg1, Locator arg2, Collection<Topic> arg3) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Name> getNames() {
		return this.names;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Name> getNames(Topic arg0) {

		Set<Name> names = new HashSet<Name>();

		for (Name n : this.names) {
			if (n.getType().equals(arg0)) {
				names.add(n);
			}
		}

		return names;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Occurrence> getOccurrences() {
		return this.occurrences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Occurrence> getOccurrences(Topic arg0) {

		Set<Occurrence> occurrences = new HashSet<Occurrence>();

		for (Occurrence o : this.occurrences) {
			if (o.getType().equals(arg0)) {
				occurrences.add(o);
			}
		}

		return occurrences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TopicMap getParent() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reifiable getReified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Role> getRolesPlayed() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Role> getRolesPlayed(Topic arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Role> getRolesPlayed(Topic arg0, Topic arg1) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Locator> getSubjectIdentifiers() {
		return this.subjectIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Locator> getSubjectLocators() {
		return this.subjectLocator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Topic> getTypes() {
		return this.types;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mergeIn(Topic arg0) throws ModelConstraintException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() throws TopicInUseException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSubjectIdentifier(Locator arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSubjectLocator(Locator arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeType(Topic arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String out = "Topic{";
		Set<Locator> set = getSubjectIdentifiers();
		if (!set.isEmpty()) {
			out += "si:" + set.iterator().next().toExternalForm();
		} else {
			set = getSubjectLocators();
			if (!set.isEmpty()) {
				out += "sl:" + set.iterator().next().toExternalForm();
			} else {
				set = getItemIdentifiers();
				if (!set.isEmpty()) {
					out += "ii:" + set.iterator().next().toExternalForm();
				} else {
					Set<Name> names = getNames();
					if (!names.isEmpty()) {
						out += "Name:" + names.iterator().next().getValue();
					} else {
						out += "id:" + getId();
					}
				}
			}
		}
		return out + "}";
	}

}
