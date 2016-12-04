package umbc.ebiquity.kang.instanceconstructor.impl;

import umbc.ebiquity.kang.instanceconstructor.IValue;

public class Value implements IValue {

	private String label;

	Value(String label){
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

}
