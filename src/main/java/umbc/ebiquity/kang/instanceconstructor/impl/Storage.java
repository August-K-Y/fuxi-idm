package umbc.ebiquity.kang.instanceconstructor.impl;

import java.util.ArrayList;
import java.util.List;

import umbc.ebiquity.kang.instanceconstructor.IDescribedInstance;
import umbc.ebiquity.kang.instanceconstructor.IInstance;
import umbc.ebiquity.kang.instanceconstructor.IStorage;

public class Storage implements IStorage {

	private String storageName;
	private List<IDescribedInstance> instances;

	Storage(String storageName) {
		this.storageName = storageName;
		this.instances = new ArrayList<IDescribedInstance>();
	}

	@Override
	public List<IDescribedInstance> getInstances() {
		return instances;
	}

	public void addInstance(IDescribedInstance instance) {
		instances.add(instance);
	}

	@Override
	public String getStorageName() {
		return storageName;
	}

	@Override
	public int getNumberOfInstances() {
		return instances.size();
	}

}
