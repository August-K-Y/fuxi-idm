package umbc.ebiquity.kang.instanceconstructor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import umbc.ebiquity.kang.instanceconstructor.IInstanceDescriptionModel;
import umbc.ebiquity.kang.instanceconstructor.IInstanceDescriptionModelRepository;

public class MongoDBRepository implements IInstanceDescriptionModelRepository {

	private final MongoDatabase database;
	private ModelSaveSupport saveSupport;
	private ModelLoadSupport loadSupport;

	public MongoDBRepository(MongoDatabase database) {
		saveSupport = new ModelSaveSupport();
		loadSupport = new ModelLoadSupport();
		this.database = database;
	}

	@Override
	public void save(IInstanceDescriptionModel model, String modelName) {
		// how to get the documents more efficiently?
		MongoCollection<Document> collection = database.getCollection(modelName);
		if (collection == null || collection.count() == 0) {
			database.createCollection(modelName);
			collection = database.getCollection(modelName);
		}
		save(model, collection);

	}

	private void save(IInstanceDescriptionModel model, MongoCollection<Document> collection) {
		RecordsHolder recordsHolder = saveSupport.record(model);
		if (recordsHolder.hasRecords()) {
			System.out.println("Triples Extracted");
			collection.insertMany(createDocuments(recordsHolder));
		}
	}

	private List<? extends Document> createDocuments(RecordsHolder recordsHolder) {
		List<Document> documents = new ArrayList<Document>(recordsHolder.getSize());
		List<Map<String, String>> records = recordsHolder.getRecords();
		for (Map<String, String> record : records) {
			Document doc = new Document();
			for (String key : record.keySet()) {
				String value = record.get(key);
				doc.append(key, value);
			}
			documents.add(doc);
		}
		return documents;
	}

	@Override
	public IInstanceDescriptionModel load(String modelName) {
		MongoCollection<Document> collection = database.getCollection(modelName);
		if (collection == null || collection.count() == 0) {
			database.createCollection(modelName);
			collection = database.getCollection(modelName);
		}
		// TODO: retrieve all documents and create the IDM
		collection.find();
		return null;
	}

}
