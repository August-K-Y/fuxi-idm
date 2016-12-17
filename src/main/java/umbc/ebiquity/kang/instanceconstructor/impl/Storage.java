package umbc.ebiquity.kang.instanceconstructor.impl;

import java.util.ArrayList;
import java.util.Collection;
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

	public void addInstance(DescribedInstance instance) {
		instances.add(instance);
	}

	public void setInstances(List<DescribedInstance> instances) {
		if (this.instances.size() > 0) {
			this.instances.clear();
		}
		assert instances.size() == 0;
		this.instances.addAll(instances);
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
