package umbc.ebiquity.kang.instanceconstructor.impl;

import static umbc.ebiquity.kang.instanceconstructor.impl.RepositorySchemaConfiguration.IS_FROM_INSTANCE;
import static umbc.ebiquity.kang.instanceconstructor.impl.RepositorySchemaConfiguration.NORMALIZED_TRIPLE_OBJECT;
import static umbc.ebiquity.kang.instanceconstructor.impl.RepositorySchemaConfiguration.NORMALIZED_TRIPLE_SUBJECT;
import static umbc.ebiquity.kang.instanceconstructor.impl.RepositorySchemaConfiguration.TRIPLE_OBJECT;
import static umbc.ebiquity.kang.instanceconstructor.impl.RepositorySchemaConfiguration.TRIPLE_OBJECT_AS_CONCEPT_SCORE;
import static umbc.ebiquity.kang.instanceconstructor.impl.RepositorySchemaConfiguration.TRIPLE_PREDICATE;
import static umbc.ebiquity.kang.instanceconstructor.impl.RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE;
import static umbc.ebiquity.kang.instanceconstructor.impl.RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE_CONCEPT_OF_INSTANCE;
import static umbc.ebiquity.kang.instanceconstructor.impl.RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE_META_DATA;
import static umbc.ebiquity.kang.instanceconstructor.impl.RepositorySchemaConfiguration.TRIPLE_RECORD_TYPE_RELATION_VALUE;
import static umbc.ebiquity.kang.instanceconstructor.impl.RepositorySchemaConfiguration.TRIPLE_SUBJECT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import umbc.ebiquity.kang.entityframework.object.Concept;
import umbc.ebiquity.kang.instanceconstructor.IDescribedInstance;
import umbc.ebiquity.kang.instanceconstructor.IStorage;
import umbc.ebiquity.kang.instanceconstructor.IStorageNavigator;

public class StorageNavigator implements IStorageNavigator {

	protected static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

	private final MongoDatabase database;

	public StorageNavigator(MongoDatabase database) {
		this.database = database;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * umbc.ebiquity.kang.instanceconstructor.IStorageNavigator#listStorageNames
	 * ()
	 */
	@Override
	public List<String> listStorageNames() {
		return database.listCollectionNames().into(new ArrayList<String>());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see umbc.ebiquity.kang.instanceconstructor.IInstanceQuerier#findAll()
	 */
	@Override
	public List<IStorage> retrieveStorages() {
		Map<String, IStorage> storagelbl2obj = new HashMap<String, IStorage>();
		for (String collectionName : database.listCollectionNames()) {
			LOGGER.debug("Retrieving instances from " + collectionName + " collection");

			Storage storage = new Storage(collectionName);
			storagelbl2obj.put(collectionName, storage);
			doRetrieve(storage);
		}
		return new ArrayList<IStorage>(storagelbl2obj.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * umbc.ebiquity.kang.instanceconstructor.IInstanceNavigator#getStorage(java
	 * .lang.String)
	 */
	@Override
	public IStorage retrieveStorage(String collectionName) {
		Storage storage = new Storage(collectionName);
		doRetrieve(storage);
		return storage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see umbc.ebiquity.kang.instanceconstructor.IInstanceNavigator#
	 * retrieveInstance(java.lang.String, java.lang.String)
	 */
	@Override
	public IDescribedInstance retrieveInstance(String instanceName, String storageName) {
		FindIterable<Document> documents = database.getCollection(storageName).find(new Document(TRIPLE_SUBJECT, instanceName));
		List<DescribedInstance> instances = parseRecords(documents, storageName);
		// the list should only contain one instance
		return instances.get(0); 
	}

	private void doRetrieve(Storage storage) {
		String storageName = storage.getStorageName();
		FindIterable<Document> documents = database.getCollection(storageName).find();
		storage.setInstances(parseRecords(documents, storageName));
	}
	
	private List<DescribedInstance> parseRecords(FindIterable<Document> documents, String storageName) {
		Map<String, DescribedInstance> insName2obj = new HashMap<String, DescribedInstance>();
		for (Document doc : documents) {
			String recordType = (String) doc.get(TRIPLE_RECORD_TYPE);
			if (isMetaData(recordType)) {
				continue;
			}
			parseRecord(doc, storageName, insName2obj);
		}
		return new ArrayList<DescribedInstance>(insName2obj.values());
	}

	private DescribedInstance parseRecord(Document doc, String storageName, Map<String, DescribedInstance> insName2obj) {

		String insName = (String) doc.get(TRIPLE_SUBJECT);
		
		// check if the instance with the given label already created. If
		// not, create a new one and add it to the current storage.
		DescribedInstance instance = insName2obj == null ? null : insName2obj.get(insName);
		if (instance == null) {
			instance = new DescribedInstance(insName);
			instance.setStorageName(storageName);
			insName2obj.put(insName, instance);
		}

		String recordType = (String) doc.get(TRIPLE_RECORD_TYPE);
		if (isConcept(recordType)) {
			instance.addConcept(createConcept(doc));
		} else if (isRelation(recordType)) {
			instance.addRelationalTriple(createTriple(doc));
		}
		return instance;
	}

	private Triple createTriple(Document doc) {
		String subject = (String) doc.get(TRIPLE_SUBJECT);
		String object = (String) doc.get(TRIPLE_OBJECT);
		String predicate = (String) doc.get(TRIPLE_PREDICATE);
		String nSubject = (String) doc.get(NORMALIZED_TRIPLE_SUBJECT);
		String nObject = (String) doc.get(NORMALIZED_TRIPLE_OBJECT);
		return new Triple(subject, nSubject, predicate, object, nObject);
	}

	private Concept createConcept(Document doc) {
		String object = (String) doc.get(TRIPLE_OBJECT);
		String nObject = (String) doc.get(NORMALIZED_TRIPLE_OBJECT);
		String isFromInstance = (String) doc.get(IS_FROM_INSTANCE);
		String score = (String) doc.get(TRIPLE_OBJECT_AS_CONCEPT_SCORE);
		Concept concept = new Concept(object, Boolean.valueOf(isFromInstance));
		concept.setScore(Double.valueOf(score));
		concept.updateProcessedLabel(nObject);
		return concept;
	}

	private boolean isConcept(String recordType) {
		return TRIPLE_RECORD_TYPE_CONCEPT_OF_INSTANCE.equals(recordType) ? true : false;
	}

	private boolean isRelation(String recordType) {
		return TRIPLE_RECORD_TYPE_RELATION_VALUE.equals(recordType) ? true : false;
	}

	private boolean isMetaData(String recordType) {
		return TRIPLE_RECORD_TYPE_META_DATA.equals(recordType) ? true : false;
	}

}
