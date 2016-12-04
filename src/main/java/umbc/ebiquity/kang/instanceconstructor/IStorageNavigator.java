package umbc.ebiquity.kang.instanceconstructor;

import java.util.List;

public interface IStorageNavigator {

	/**
	 * Retrieve names of all storages.
	 * 
	 * @return a list of storage names
	 */
	List<String> listStorageNames();

	/**
	 * Retrieve instances in all storages.
	 * 
	 * @return a list of <code>IStorage</code>. It can not be null.
	 */
	List<IStorage> retrieveStorages();

	/**
	 * Retrieve instances in a storage with the specified name.
	 * 
	 * @param storageName
	 *            the name of the storage to be retrieved
	 * @return an <code>IStorage</code>
	 */
	IStorage retrieveStorage(String storageName);

	/**
	 * Retrieve instance with the specified name in the specified storage.
	 * 
	 * @param instanceName
	 *            the name of the instance to be retrieved
	 * @param storageName
	 *            the name of the storage from where the instance to be
	 *            retrieved
	 * @return an <code>IDescribedInstance</code>
	 */
	IDescribedInstance retrieveInstance(String instanceName, String storageName);

}
