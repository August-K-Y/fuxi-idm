package umbc.ebiquity.kang.instanceconstructor.impl;

public class FileRepositoryConfiguration {
	
	/**
	 * 
	 */
	public static String REPOSITORIES_DIRECTORY_FULL_PATH = System.getProperty("user.dir");

	/**
	 *  
	 */
	private final static String MODEL_REPOSITORY_DIRECTORY = "/ModelRepository/";

	private final static String REPOSITORIES_DIRECTORY_NAME = "/Repositories";

	public static String getRepositoryDirectoryFullPath() {
		String projectDir = FileRepositoryConfiguration.REPOSITORIES_DIRECTORY_FULL_PATH + REPOSITORIES_DIRECTORY_NAME;
		String storageDir = FileRepositoryConfiguration.MODEL_REPOSITORY_DIRECTORY;
		return projectDir + storageDir;
	}

}
