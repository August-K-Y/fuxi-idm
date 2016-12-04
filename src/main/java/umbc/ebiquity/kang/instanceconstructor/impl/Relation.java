package umbc.ebiquity.kang.instanceconstructor.impl;

import umbc.ebiquity.kang.instanceconstructor.IRelation;

public class Relation implements IRelation {

	private String label;

	Relation(String label) {
		this.label = label;
	}

	@Override
	public String getRelationName() {
		return label;
	}

	@Override
	public String toString() {
		return this.getRelationName();
	}

	@Override
	public int hashCode() {
		return this.getRelationName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		Relation valuedRelation = (Relation) obj;
		return this.getRelationName().equals(valuedRelation.getRelationName());
	}

}
