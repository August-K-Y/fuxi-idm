package umbc.ebiquity.kang.instanceconstructor.impl;

import java.util.ArrayList;
import java.util.List;

import umbc.ebiquity.kang.instanceconstructor.IRelation;
import umbc.ebiquity.kang.instanceconstructor.IValue;
import umbc.ebiquity.kang.instanceconstructor.IValuedRelation;

public class ValuedRelation implements IValuedRelation {

	private Relation relation;
	private List<IValue> values;

	ValuedRelation(Relation relation) {
		this.relation = relation;
		this.values = new ArrayList<IValue>();
	}
	
	@Override
	public IRelation getRelation() {
		return relation;
	}
	
	public String getRelationName(){
		return relation.getRelationName();
	}

	@Override
	public List<IValue> getValues() {
		return values;
	}
	
	public void addValue(Value value){
		this.values.add(value);
	}

	@Override
	public String toString() {
		return this.relation.getRelationName();
	}

	@Override
	public int hashCode() {
		return this.relation.getRelationName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		ValuedRelation valuedRelation = (ValuedRelation) obj;
		return this.getRelationName().equals(valuedRelation.getRelationName());
	}
}
