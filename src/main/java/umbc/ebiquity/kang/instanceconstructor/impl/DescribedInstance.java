package umbc.ebiquity.kang.instanceconstructor.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import umbc.ebiquity.kang.entityframework.object.Concept;
import umbc.ebiquity.kang.instanceconstructor.IDescribedInstance;
import umbc.ebiquity.kang.instanceconstructor.IRelation;
import umbc.ebiquity.kang.instanceconstructor.IValue;
import umbc.ebiquity.kang.instanceconstructor.IValuedRelation;

public class DescribedInstance extends Instance implements IDescribedInstance {

	private String processedSubjectLabel;
	
	// instance to its concept set map
	private Set<Concept> concepts;
	private Map<String, Set<String>> taxonomicRelationValueMap;
	private HashMap<Relation, ValuedRelation> relation2ValueMap;
	
	public DescribedInstance(String subjectLabel) {
		super(subjectLabel.trim());
		this.relation2ValueMap = new HashMap<Relation, ValuedRelation>();
		this.taxonomicRelationValueMap = new HashMap<String, Set<String>>();
		this.concepts = new LinkedHashSet<Concept>();
	}

	public void addConcept(Concept concept) {
		concepts.add(concept);
	}
	
	public boolean containsConcept() {
		return concepts.size() > 0 ? true : false;
	}
	
	public void addRelationalTriple(Triple triple) {
		Relation relation = new Relation(triple.getPredicate());
		ValuedRelation valuedRelation;
		if (relation2ValueMap.containsKey(relation)) {
			valuedRelation = relation2ValueMap.get(relation);
			Value value = new Value(triple.getObject());
			if (!valuedRelation.containsValue(value)) {
				valuedRelation.addValue(value);
			}
		} else {
			valuedRelation = new ValuedRelation(relation);
			relation2ValueMap.put(relation, valuedRelation);
			valuedRelation.addValue(new Value(triple.getObject()));
		}
	}
	
	public void addTaxonomicTriple(Triple triple) {
		String predicate = triple.getPredicate();
		String object = triple.getObject();
		
		Set<String> relationValueMap;
		if(taxonomicRelationValueMap.containsKey(predicate)){
			relationValueMap = taxonomicRelationValueMap.get(predicate);
		} else {
			relationValueMap = new HashSet<String>();
			taxonomicRelationValueMap.put(predicate, relationValueMap);
		}
		relationValueMap.add(object);
	}

	public void printTriples() {
		System.out.println("* <" + getName() + ">");
		for (Relation relation : relation2ValueMap.keySet()) {
			System.out.println("      <" + relation.getRelationName() + ">");
			ValuedRelation valuedRelation = relation2ValueMap.get(relation);
			for (IValue value : valuedRelation.getValues()) {
				System.out.println("                  <" + value.getLabel() + ">");
			}
		}
		for (Concept concept : concepts) {
			System.out.println(" <" + concept.getConceptName() + ">  <" + concept.getScore() + ">");
		}
	}
	
	public Set<Concept> getConcepts(){
		return this.concepts;
	}
	
	public Set<IRelation> getRelations(){
		return new HashSet<IRelation>(relation2ValueMap.keySet());
	}
	
	public IValuedRelation getRelationValueByName(String name){
		return relation2ValueMap.get(new Relation(name));
	}
	
	public Collection<String> getTaxonomicRelationValue() {
		Collection<String> emptyCollection = new ArrayList<String>();
		if (taxonomicRelationValueMap.get("SubConcept") == null) {
			return emptyCollection;
		}
		return taxonomicRelationValueMap.get("SubConcept");
	}

	public Set<IValuedRelation> getValuedRelations(){
		return new HashSet<IValuedRelation>(relation2ValueMap.values());
	}
	
	public Map<String, Set<String>> getTaxonomicRelation2ValueMap(){
		return this.taxonomicRelationValueMap;
	}
	
	@Override
	public String getProcessedSubjectLabel() {
		return processedSubjectLabel;
	}
	
	public void setProcessedSubjectLabel(String processedSubjectLabel) {
		this.processedSubjectLabel = processedSubjectLabel;
	}
	
	@Override
	public int hashCode() {
		return getName().trim().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		DescribedInstance cluster = (DescribedInstance) obj;
		return getName().equals(cluster.getName());
	}

}
