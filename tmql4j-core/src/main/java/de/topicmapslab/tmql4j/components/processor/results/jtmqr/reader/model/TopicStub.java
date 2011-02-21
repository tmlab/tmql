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

public class TopicStub implements Topic {

	Set<Locator> subjectIdentifier;
	Set<Locator> subjectLocator;
	Set<Locator> itemIdentifier;
	
	Set<Name> names;
	Set<Occurrence> occurrences;
	
	protected TopicStub() {
		
		this.subjectIdentifier = new HashSet<Locator>();
		this.subjectLocator = new HashSet<Locator>();
		this.itemIdentifier = new HashSet<Locator>();
		
		this.names = Collections.emptySet();
		this.occurrences = Collections.emptySet();
	}
	
	protected void _addSubjectIdentifier(String iri){
		Locator l = new LocatorImpl(iri);
		this.subjectIdentifier.add(l);
	}
	
	protected void _addSubjectLocator(String iri){
		Locator l = new LocatorImpl(iri);
		this.subjectLocator.add(l);
	}
	
	protected void _addItemIdentifier(String iri){
		Locator l = new LocatorImpl(iri);
		this.itemIdentifier.add(l);
	}
	
	protected void _setNames(Set<Name> names){
		this.names = names;
	}
	
	protected void _setOccurrences(Set<Occurrence> occurrences){
		this.occurrences = occurrences;
	}
	
	
	
	// --[ TMAPI methods ]---------------------------------------
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Locator> getItemIdentifiers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopicMap getTopicMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeItemIdentifier(Locator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addItemIdentifier(Locator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSubjectIdentifier(Locator arg0)
			throws IdentityConstraintException, ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSubjectLocator(Locator arg0)
			throws IdentityConstraintException, ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addType(Topic arg0) throws ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Name createName(String arg0, Topic... arg1)
			throws ModelConstraintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name createName(String arg0, Collection<Topic> arg1)
			throws ModelConstraintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name createName(Topic arg0, String arg1, Topic... arg2)
			throws ModelConstraintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name createName(Topic arg0, String arg1, Collection<Topic> arg2)
			throws ModelConstraintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Occurrence createOccurrence(Topic arg0, String arg1, Topic... arg2)
			throws ModelConstraintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Occurrence createOccurrence(Topic arg0, String arg1,
			Collection<Topic> arg2) throws ModelConstraintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Occurrence createOccurrence(Topic arg0, Locator arg1, Topic... arg2)
			throws ModelConstraintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Occurrence createOccurrence(Topic arg0, Locator arg1,
			Collection<Topic> arg2) throws ModelConstraintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Occurrence createOccurrence(Topic arg0, String arg1, Locator arg2,
			Topic... arg3) throws ModelConstraintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Occurrence createOccurrence(Topic arg0, String arg1, Locator arg2,
			Collection<Topic> arg3) throws ModelConstraintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Name> getNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Name> getNames(Topic arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Occurrence> getOccurrences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Occurrence> getOccurrences(Topic arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopicMap getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reifiable getReified() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Role> getRolesPlayed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Role> getRolesPlayed(Topic arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Role> getRolesPlayed(Topic arg0, Topic arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Locator> getSubjectIdentifiers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Locator> getSubjectLocators() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Topic> getTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mergeIn(Topic arg0) throws ModelConstraintException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove() throws TopicInUseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSubjectIdentifier(Locator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSubjectLocator(Locator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeType(Topic arg0) {
		// TODO Auto-generated method stub
		
	}

}
