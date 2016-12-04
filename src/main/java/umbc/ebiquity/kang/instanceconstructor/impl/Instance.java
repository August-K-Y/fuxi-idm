package umbc.ebiquity.kang.instanceconstructor.impl;

import umbc.ebiquity.kang.instanceconstructor.IInstance;

public class Instance implements IInstance {
	
	private String subjectLabel;
	private String storageName;
	
	public Instance(String subjectLabel) {
		this.subjectLabel = subjectLabel;
	}

	@Override
	public String getName() {
		return subjectLabel;
	}

	@Override
	public String getStorageName() {
		return storageName;
	}
	
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

}
