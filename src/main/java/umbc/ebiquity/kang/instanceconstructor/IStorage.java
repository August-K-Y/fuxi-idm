package umbc.ebiquity.kang.instanceconstructor;

import java.util.List;

public interface IStorage {

	List<IDescribedInstance> getInstances();
	
	String getStorageName();
	
	int getNumberOfInstances();
	
}
