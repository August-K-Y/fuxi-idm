package umbc.ebiquity.kang.instanceconstructor.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import umbc.ebiquity.kang.instanceconstructor.IRelation;
import umbc.ebiquity.kang.instanceconstructor.IValue;
import umbc.ebiquity.kang.instanceconstructor.IValuedRelation;

public class ValuedRelation implements IValuedRelation {

	private Relation relation;
	private Set<IValue> values;

	ValuedRelation(Relation relation) {
		this.relation = relation;
		this.values = new LinkedHashSet<IValue>();
	}

	@Override
	public IRelation getRelation() {
		return relation;
	}

	public String getRelationName() {
		return relation.getRelationName();
	}

	@Override
	public Set<IValue> getValues() {
		return values;
	}

	public void addValue(Value value) {
		this.values.add(value);
	}

	public boolean containsValue(Value value) {
		return values.contains(value);
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
