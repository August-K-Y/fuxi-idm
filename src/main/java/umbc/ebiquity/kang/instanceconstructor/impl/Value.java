package umbc.ebiquity.kang.instanceconstructor.impl;

import umbc.ebiquity.kang.instanceconstructor.IValue;

public class Value implements IValue {

	private String label;

	Value(String label) {
		this.label = label.trim();
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public int hashCode() {
		return getLabel().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		Value value = (Value) obj;
		return getLabel().equals(value.getLabel());
	}

}
