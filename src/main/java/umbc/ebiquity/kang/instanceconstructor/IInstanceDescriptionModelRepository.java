package umbc.ebiquity.kang.instanceconstructor;


public interface IInstanceDescriptionModelRepository {

	/**
	 * 
	 * @param model
	 * @param modelName
	 */
	public void save(IInstanceDescriptionModel model, String modelName);

	/**
	 * 
	 * @param modelName
	 * @return
	 */
	public IInstanceDescriptionModel load(String modelName);
}
