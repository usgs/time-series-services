package gov.usgs.wma.waterdata.collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.waterdata.collections.CollectionsDao;
import gov.usgs.wma.waterdata.collections.CollectionParams;
import gov.usgs.wma.waterdata.springinit.BaseIT;
import gov.usgs.wma.waterdata.springinit.DBTestConfig;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = { DBTestConfig.class, CollectionParams.class, CollectionsDao.class })
@DatabaseSetup("classpath:/testData/monitoringLocation/")
public class CollectionsDaoIT extends BaseIT {
	@Autowired
	protected CollectionParams collectionsParams;

	@Autowired
	private CollectionsDao collectionsDao;

	@Test
	public void foundCollectionsTest() {
		try {
			String expected = getCompareFile("collections.json").replace("{serverUrl}", collectionsParams.getServerUrl());
			String actual = collectionsDao.getCollectionsJson(collectionsParams.getParameters(null));
			assertNotNull(actual);
			assertJsonEquals(expected, actual);
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void foundMonLocCollectionTest() {
		try {
			String expected = getCompareFile("monLocsCollection.json").replace("{serverUrl}", collectionsParams.getServerUrl());
			String actual = collectionsDao.getCollectionJson(collectionsParams.getParameters("monitoring-locations"));
			assertNotNull(actual);
			assertJsonEquals(expected, actual);
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void foundNetworkCollectionTest() {
		try {
			String expected = getCompareFile("ahsCollection.json").replace("{serverUrl}", collectionsParams.getServerUrl());
			String actual = collectionsDao.getCollectionJson(collectionsParams.getParameters("AHS"));
			assertNotNull(actual);
			assertJsonEquals(expected, actual);
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void notFoundTest() {
		String collectionJson = collectionsDao.getCollectionJson(collectionsParams.getParameters("xyz"));
		assertNull(collectionJson);
	}

}
