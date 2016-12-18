package umbc.ebiquity.kang.instanceconstructor.impl;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.mongodb.MongoClient;

import umbc.ebiquity.kang.entityframework.object.Concept;
import umbc.ebiquity.kang.instanceconstructor.IDescribedInstance;
import umbc.ebiquity.kang.instanceconstructor.IInstanceDescriptionModel;
import umbc.ebiquity.kang.instanceconstructor.IStorage;
import umbc.ebiquity.kang.instanceconstructor.IValue;
import umbc.ebiquity.kang.instanceconstructor.IValuedRelation;
import umbc.ebiquity.kang.instanceconstructor.builder.InstanceDescriptionModelFactory;

public class MongoDBRepostoryIntegrationTest {
	
	@Ignore
	@Test
	public void shouldSaveAStorageIntegrationTest() throws IOException {
		String webSiteURLString = "http://www.accutrex.com";
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient("localhost", 27017);
			URL webSiteURL = new URL(webSiteURLString);
			IInstanceDescriptionModel extractedTripleStore = InstanceDescriptionModelFactory.construct(webSiteURL);
			MongoDBPersistentRepository repo = new MongoDBPersistentRepository(mongoClient.getDatabase("repository"));
			repo.save(extractedTripleStore, "testRepo12");
		} catch (IOException e) {
			throw e;
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}
	}

//	@Ignore
	@Test
	public void shouldReturnAllStoragesIntegrationTest() {
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient("localhost", 27017);
			StorageNavigator query = new StorageNavigator(mongoClient.getDatabase("repository"));
			printStorages(query.retrieveStorages());
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}
	}

	@Ignore
	@Test
	public void shouldListStorageNamesIntegrationTest() {
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient("localhost", 27017);
			StorageNavigator query = new StorageNavigator(mongoClient.getDatabase("repository"));
			System.out.println(query.listStorageNames());
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}
	}

	private void printStorages(List<IStorage> storages) {
		for (IStorage storage : storages) {
			System.out.println(storage.getStorageName());
			for (IDescribedInstance instance : storage.getInstances()) {
				System.out.println("    " + instance.getName());
				for (Concept concept : instance.getConcepts()) {
					System.out.println("       c: " + concept.getConceptName());
				}
				for (IValuedRelation relation : instance.getValuedRelations()) {
					System.out.println("       r: " + relation.getRelation().getRelationName());
					for (IValue value : relation.getValues()) {
						System.out.println("            v: " + value.getLabel());
					}
				}
			}
		}
	}
}
