package umbc.ebiquity.kang.instanceconstructor.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import umbc.ebiquity.kang.entityframework.object.EntityNode;
import umbc.ebiquity.kang.instanceconstructor.IInstanceDescriptionModel;
import umbc.ebiquity.kang.instanceconstructor.impl.Triple.BuiltinPredicate;
import umbc.ebiquity.kang.instanceconstructor.impl.Triple.PredicateType;

/**
 * 
 * Stores RDF Triples extracted from Entity Graph or loaded from local storage
 * (e.g., file system or data base)
 * 
 * @author Yan Kang
 * 
 */
public final class InstanceDescriptionModel implements IInstanceDescriptionModel {
	
	private URL tripleStoreURI;
	private String tripleStoreName;
	private int numberOfRelations;
	private int numberOfTriples;
	
	/**
	 * This Set stores all the triples
	 */
	private Set<Triple> theWholeTripleSet;
	
	private Set<Triple> relationTypeTriple;
	
	/**
	 * This Map maps subjects to their group
	 */
	private Map<String, DescribedInstance> subject2TripleSetMap;
	
	/**
	 * This Set stores all triples with custom predicates
	 */
	private Set<Triple> triplesWithCustomRelation;
	
	/**
	 * This Set stores triples with predicates that are property subsumption relation
	 */
	private Set<Triple> triplesWithPropertySubsumptionPredicate;
	
	/**
	 * This Set stores triples with predicates that are class subsumption relation
	 */
	private Set<Triple> triplesWithClassSubsumptionPredicate;
	
	/**
	 * 
	 */
	private Set<Triple> triplesOfInstance2ConceptsRelation;
	
	/**
	 * This Map maps predicates to objects of these predicates
	 */
	private Map<String, Set<String>> customRelation2ObjectMap;
	
	/**
	 * This Map maps predicates to subjects of these predicates
	 */
	private Map<String, Set<String>> customRelation2SubjectMap;
	
	/**
	 * 
	 */
	private Map<EntityNode, Set<EntityNode>> classNodeDescendantMap;
	
	/**
	 * 
	 */
	private Map<EntityNode, Set<EntityNode>> classNodePrecedentMap;
	
	/**
	 * 
	 */
	private Map<EntityNode, Set<EntityNode>> propertyNodeDescendantMap;

	/**
	 * 
	 */
	private Map<EntityNode, Set<EntityNode>> propertyNodePrecedentMap;
	
	public InstanceDescriptionModel() {
		this.init();
	}

	/**
	 * 
	 * @param knowledgeBaseURI
	 * @param knowledgeBaseName
	 * @param tripleSet
	 */
	public InstanceDescriptionModel(Set<Triple> tripleSet, URL webSiteURL) {
		this.init();
		this.tripleStoreURI = webSiteURL;
		this.theWholeTripleSet.addAll(tripleSet);
		this.construct(tripleSet);
	}

	private void init() {
		this.theWholeTripleSet = new LinkedHashSet<Triple>();
		this.subject2TripleSetMap = new LinkedHashMap<String, DescribedInstance>();
		this.relationTypeTriple = new HashSet<Triple>();

		this.triplesWithCustomRelation = new LinkedHashSet<Triple>();
		this.triplesWithPropertySubsumptionPredicate = new LinkedHashSet<Triple>();
		this.triplesWithClassSubsumptionPredicate = new LinkedHashSet<Triple>();
		this.triplesOfInstance2ConceptsRelation = new LinkedHashSet<Triple>();
		this.customRelation2ObjectMap = new LinkedHashMap<String, Set<String>>();
		this.customRelation2SubjectMap = new LinkedHashMap<String, Set<String>>();
		this.classNodeDescendantMap = new LinkedHashMap<EntityNode, Set<EntityNode>>();
		this.classNodePrecedentMap = new LinkedHashMap<EntityNode, Set<EntityNode>>();
		this.propertyNodeDescendantMap = new LinkedHashMap<EntityNode, Set<EntityNode>>();
		this.propertyNodePrecedentMap = new LinkedHashMap<EntityNode, Set<EntityNode>>();
	}

	
	/**
	 * This method does the groundwork including group triples based on their
	 * subjects, types of predicate and predicates themselves
	 */
	private void construct(Collection<Triple> tripleSet) {

		for (Triple triple : tripleSet) {
			PredicateType predicateType = triple.getPredicateType();
			String subject = triple.getSubject().trim();
			
			/*
			 * Group triples based on subjects of triples
			 */
			DescribedInstance instanceTripleSet;
			if(subject2TripleSetMap.containsKey(subject)){
				instanceTripleSet = subject2TripleSetMap.get(subject);
			} else {
				 instanceTripleSet = new DescribedInstance(subject);
				 subject2TripleSetMap.put(subject, instanceTripleSet);
			}

			/*
			 * Separate triples based on the type of predicate. There are two
			 * types of predicate: Custom and Builtin 
			 * (1) If the type is Custom, it is highly likely that the predicate corresponds a property of the ontology 
			 * (2) If the type is Builtin, it is likely that the predicate corresponds a subsumption relation.
			 */
			if (PredicateType.Custom == predicateType) {
				
				triplesWithCustomRelation.add(triple);
				instanceTripleSet.addRelationalTriple(triple);
				String customRelation = triple.getPredicate();
				/*
				 * Create HashMap that maps custom predicates to their subjects.
				 */
				Set<String> relationSubjects = null;
				if(customRelation2SubjectMap.containsKey(customRelation)){
					relationSubjects = customRelation2SubjectMap.get(customRelation);
				}else{
					relationSubjects = new HashSet<String>();
					customRelation2SubjectMap.put(customRelation, relationSubjects);
				}
				relationSubjects.add(subject);
				
				/*
				 * Create HashMap that maps custom predicates to their objects.
				 */
				String object= triple.getObject();
				Set<String> relationObjects = null;
				if(customRelation2ObjectMap.containsKey(customRelation)){ 
					relationObjects = customRelation2ObjectMap.get(customRelation);
				}else{
					relationObjects = new HashSet<String>();
					customRelation2ObjectMap.put(customRelation, relationObjects);
				}
				relationObjects.add(object);
				numberOfTriples++;
				
			} else if (PredicateType.Builtin == predicateType) {
				if (BuiltinPredicate.SubRole.toString().equals(triple.getPredicate())) {
					triplesWithPropertySubsumptionPredicate.add(triple);
					instanceTripleSet.addTaxonomicTriple(triple);
					numberOfTriples++;
				} else if (BuiltinPredicate.SubConcept.toString().equals(triple.getPredicate())) {
					triplesWithClassSubsumptionPredicate.add(triple);
					instanceTripleSet.addTaxonomicTriple(triple);
					numberOfTriples++;
				} else if (BuiltinPredicate.hasConcept.toString().equals(triple.getPredicate())) {
					triplesOfInstance2ConceptsRelation.add(triple);
					instanceTripleSet.addConcept(triple.getConcept());
					numberOfTriples++;
				} else if (BuiltinPredicate.isTypeOf.toString().equals(triple.getPredicate())){
					relationTypeTriple.add(triple);
					numberOfRelations++;
					numberOfTriples++;
				}
			}
		}
		this.constructHierarchy(classNodeDescendantMap, classNodePrecedentMap, triplesWithClassSubsumptionPredicate);
		this.constructHierarchy(propertyNodeDescendantMap, propertyNodePrecedentMap, triplesWithPropertySubsumptionPredicate);
	}

	private void constructHierarchy(Map<EntityNode, Set<EntityNode>> nodeDescendantMap, Map<EntityNode, Set<EntityNode>> nodePrecedentMap,
			Set<Triple> tripleSet) {

		for (Triple triple : tripleSet) {
			// if (triple.getBuiltinPredicate() == BuiltinPredicate.SubClass ||
			// triple.getBuiltinPredicate() == BuiltinPredicate.Type) {
			EntityNode node = new EntityNode(triple.getObject());
			EntityNode descendant = new EntityNode(triple.getSubject());
			if (nodeDescendantMap.containsKey(node)) {
				nodeDescendantMap.get(node).add(descendant);
			} else {
				Set<EntityNode> descendantSet = new LinkedHashSet<EntityNode>();
				descendantSet.add(descendant);
				nodeDescendantMap.put(node, descendantSet);
			}

			if (nodePrecedentMap.containsKey(descendant)) {
				nodePrecedentMap.get(descendant).add(node);
			} else {
				Set<EntityNode> precedentSet = new LinkedHashSet<EntityNode>();
				precedentSet.add(node);
				nodePrecedentMap.put(descendant, precedentSet);
			}
		}
	}

	public Collection<DescribedInstance> getDescribedInstances(){
		return subject2TripleSetMap.values();
	}
	
	public DescribedInstance getDescribedInstanceByName(String instanceName){ 
		return this.subject2TripleSetMap.get(instanceName);
	}

	public Collection<Triple> getCustomRelationTriples(){
		List<Triple> tripleList = new ArrayList<Triple>(this.triplesWithCustomRelation);
		Collections.sort(tripleList, new TripleSorterBySubject());
		return tripleList;
	}
	
	@Override
	public Map<String, Collection<Triple>> getInstanceName2CustomRelationTripleMap() {
		List<Triple> tripleList = new ArrayList<Triple>(this.triplesWithCustomRelation);
		Collections.sort(tripleList, new TripleSorterBySubject());
		return this.groupTriplesByInstanceName(tripleList);
	}

	public Collection<Triple> getConceptRelationTriples(){
		List<Triple> tripleList = new ArrayList<Triple>(this.triplesOfInstance2ConceptsRelation);
		Collections.sort(tripleList, new TripleSorterBySubject());
		return tripleList;
	}
	
	@Override
	public Map<String, Collection<Triple>> getInstanceName2ConceptRelationTripleMap(){
		List<Triple> tripleList = new ArrayList<Triple>(this.triplesOfInstance2ConceptsRelation);
		Collections.sort(tripleList, new TripleSorterBySubject());
		return this.groupTriplesByInstanceName(tripleList);
	}
	
	@Override
	public Set<Triple> getRelationTypeTriple(){
		return relationTypeTriple;
	}
	
	private Map<String, Collection<Triple>> groupTriplesByInstanceName(List<Triple> tripleList){
		Map<String, Collection<Triple>> subject2TripleMap = new LinkedHashMap<String, Collection<Triple>>();
		for (Triple triple : tripleList) {
			String subjectLabel = triple.getSubject();
			Collection<Triple> triples;
			if (subject2TripleMap.containsKey(subjectLabel)) {
				triples = subject2TripleMap.get(subjectLabel);
			} else {
				triples = new ArrayList<Triple>();
				subject2TripleMap.put(subjectLabel, triples);
			}
			triples.add(triple);
		}
		return subject2TripleMap;
	}
	
	public Collection<String> getObjectTermsOfRelation(String relation){ 
		return this.customRelation2ObjectMap.get(relation);
	}
	
	public Collection<String> getSubjectTermsOfRelation(String relation){
		return this.customRelation2SubjectMap.get(relation);
	}
	
	public Collection<String> getCustomRelations(){
		Collection<String> relationCollection = new LinkedHashSet<String>();
		for(Triple triple : this.getCustomRelationTriples()){
			relationCollection.add(triple.getPredicate());
		}
		return relationCollection;
	}
	
	
	/**
	 * All the following printXXX methods are for test/debug purpose
	 * 
	 */
	
	public void printMetaData(){
		System.out.println("Triple Store URI: " + this.tripleStoreURI);
		System.out.println("Triple Store Name: " + this.tripleStoreName);
	}

	public void printTriplesWithCustomRelation() {
		this.printTriplesGroupBySuject(this.getInstanceName2CustomRelationTripleMap());
	}
	
	public void printTriplesOfInstance2ConceptRelation(){
		this.printTriplesGroupBySuject(this.getInstanceName2ConceptRelationTripleMap());
	}

	private void printTriplesGroupBySuject(Map<String, Collection<Triple>> triplesGroupBySubject) {
		for (String subject : triplesGroupBySubject.keySet()) {
			System.out.println("<" + subject + ">");
			for (Triple triple : triplesGroupBySubject.get(subject)) {
				System.out.println("              <" + triple.getPredicate() + "> <" + triple.getObject() + ">");
			}
		}
	}

	public void printClassHierarchy() {
		System.out.println("\nPrinting Class Hierarchy ...");
		Set<String> visitedNodeSet = new HashSet<String>();
		for (EntityNode node : classNodeDescendantMap.keySet()) {
			if (classNodePrecedentMap.get(node) == null) {
				String indent = "# ";
				System.out.println(indent + node.getLabel());
				visitedNodeSet.add(node.getLabel());
				Set<EntityNode> descedantSet = classNodeDescendantMap.get(node);
				indent = indent + "      ";
				for (EntityNode descedant : descedantSet) {
					String descedantStr = descedant.getLabel().toLowerCase();
					if (visitedNodeSet.contains(descedantStr)) {
						continue;
					}
					if (!node.getLabel().toLowerCase().equals(descedant.getLabel().toLowerCase())) {
						visitedNodeSet.add(descedantStr);
						System.out.println(indent + descedant.getLabel());
						this.printDescedants(descedant, indent + "      ", visitedNodeSet);
					}
				}
			}
		}
	}

	public void printPropertyHierarchy() {
		System.out.println("printing property hierarchy ...");
		Set<String> visitedNodeSet = new HashSet<String>();
		for (EntityNode node : propertyNodeDescendantMap.keySet()) {
			if (propertyNodePrecedentMap.get(node) == null) {
				String indent = "& ";
				System.out.println(indent + node.getLabel());
				visitedNodeSet.add(node.getLabel());
				Set<EntityNode> descedantSet = propertyNodeDescendantMap.get(node);
				indent = indent + "      ";
				for (EntityNode descedant : descedantSet) {
					String descedantStr = descedant.getLabel().toLowerCase();
					if (visitedNodeSet.contains(descedantStr)) {
						continue;
					}
					if (!node.getLabel().toLowerCase().equals(descedant.getLabel().toLowerCase())) {
						visitedNodeSet.add(descedantStr);
						System.out.println(indent + descedant.getLabel());
						this.printDescedants(descedant, indent + "      ", visitedNodeSet);
					}
				}
			}
		}
	}

	private void printDescedants(EntityNode node, String indent, Set<String> visitedNodeSet) {

		Set<EntityNode> descedantSet = classNodeDescendantMap.get(node);
		if (descedantSet == null)
			return;
		for (EntityNode descedant : descedantSet) {
			String descedantStr = descedant.getLabel().toLowerCase();
			if (visitedNodeSet.contains(descedantStr)) {
				continue;
			}
			if (!node.getLabel().toLowerCase().equals(descedant.getLabel().toLowerCase())) {
				visitedNodeSet.add(descedantStr);
				System.out.println(indent + descedant.getLabel());
				this.printDescedants(descedant, indent + "      ", visitedNodeSet);
			}
		}
	}

	@Override
	public void showTriples() {
		for (DescribedInstance tripleGroup: subject2TripleSetMap.values()) {
			tripleGroup.printTriples();
		}
	}

	@Override
	public String getRepositoryName() {
		return this.tripleStoreName;
	}
	
	@Override
	public URL getSourceURL(){
		return this.tripleStoreURI;
	}

}
