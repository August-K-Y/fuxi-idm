package umbc.ebiquity.kang.instanceconstructor.impl;

public class RepositorySchemaConfiguration {
	/**
	 * Operating System specific line separator that separates lines in a file
	 */
	public static String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Type of record
	 */
	public static String TRIPLE_RECORD_TYPE = "Record_Type";

	/**
	 * Values for the record type
	 */
	public static String TRIPLE_RECORD_TYPE_META_DATA = "Meta_Data";
	public static String TRIPLE_RECORD_TYPE_RELATION_VALUE = "Relation_Value";
	public static String TRIPLE_RECORD_TYPE_RELATION_DEFINITION = "Relation_Definition";
	public static String TRIPLE_RECORD_TYPE_CONCEPT_OF_INSTANCE = "Instance_Concept";

	/**
	 * 
	 */
	public static String TRIPLE_SUBJECT = "Subject";
	public static String TRIPLE_OBJECT = "Object";
	public static String TRIPLE_PREDICATE = "Predicate";
	public static String NORMALIZED_TRIPLE_SUBJECT = "N_Subject";
	public static String NORMALIZED_TRIPLE_OBJECT = "N_Object";
	public static String TRIPLE_PREDICATE_TYPE = "Predicate_Type";
	public static String IS_FROM_INSTANCE = "Is_From_Instance";
	public static String TRIPLE_OBJECT_AS_CONCEPT_SCORE = "Triple_Object_As_Concept_Score";

	public static String TRIPLE_STORE_URI = "Triple_Store_URI";
	public static String TRIPLE_STORE_NAME = "Triple_Store_Name";
	public static String TRIPLE_STORE_NUMBER_OF_TRIPLES = "Triple_Store_Number_Of_Triples";
	public static String TRIPLE_STORE_NUMBER_OF_RELATIONS = "Triple_Store_Number_of_Relations";

}
