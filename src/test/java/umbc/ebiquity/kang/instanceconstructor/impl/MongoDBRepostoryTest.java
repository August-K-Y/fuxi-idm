package umbc.ebiquity.kang.instanceconstructor.impl;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.mongodb.MongoClient;

import umbc.ebiquity.kang.instanceconstructor.IInstanceDescriptionModel;
import umbc.ebiquity.kang.instanceconstructor.builder.InstanceDescriptionModelFactory;

public class MongoDBRepostoryTest {

	@Test
	public void test1() throws IOException {
		String webSiteURLString = "http://www.accutrex.com";
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient("localhost", 27017);
			URL webSiteURL = new URL(webSiteURLString);
			IInstanceDescriptionModel extractedTripleStore = InstanceDescriptionModelFactory.construct(webSiteURL);
			MongoDBRepository repo = new MongoDBRepository(mongoClient.getDatabase("repository"));
			repo.save(extractedTripleStore, "testRepo12");
		} catch (IOException e) {
			throw e;
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}
	}
}
