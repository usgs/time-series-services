package gov.usgs.wma.waterdata.features;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.waterdata.ConfigurationService;
import gov.usgs.wma.waterdata.collections.CollectionParams;
import gov.usgs.wma.waterdata.springinit.BaseIT;
import gov.usgs.wma.waterdata.springinit.DBTestConfig;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = {DBTestConfig.class, CollectionParams.class, FeaturesDao.class,
					ConfigurationService.class})
@DatabaseSetup("classpath:/testData/monitoringLocation/")
public class FeaturesDaoIT extends BaseIT {

	@Autowired
	private CollectionParams collectionsParams;

	@Autowired
	private FeaturesDao featuresDao;

	@Test
	public void foundCollectionFeatureTest() {
		try {
			String expected = getCompareFile("features/monitoring-locations/USGS-07227448.json");
			String actual = featuresDao.getCollectionFeatureJson(collectionsParams.buildParams("monitoring-locations","USGS-07227448"));
			assertJsonEquals(expected, actual);
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void monLocCollectionFeatureCountTest() {
		int count = featuresDao.getCollectionFeatureCount(collectionsParams.buildParams("monitoring-locations"));
		assertTrue(count == 3);
	}

	@Test
	public void networkCollectionFeatureCountTest() {
		int count = featuresDao.getCollectionFeatureCount(collectionsParams.buildParams("AHS"));
		assertTrue(count == 1);
	}

	@Test
	public void notFoundFeatureNoCollectionTest() {
		String featureJson = featuresDao.getCollectionFeatureJson(collectionsParams.buildParams("xyz","USGS-07227448"));
		assertNull(featureJson);
	}

	@Test
	public void notFoundFeaturesNoCollectionTest() {
		String featureJson = featuresDao.getCollectionFeaturesJson(collectionsParams.buildParams("xyz","USGS-07227448"));
		assertNull(featureJson);
	}

	@Test
	public void notFoundFeatureId() {
		String featureJson = featuresDao.getCollectionFeatureJson(collectionsParams.buildParams("monitoring-locations","xyz"));
		assertNull(featureJson);
	}

	@Test
	public void notFoundFeatureNoGeom() {
		String featureJson = featuresDao.getCollectionFeatureJson(collectionsParams.buildParams("monitoring-locations","USGS-04028090"));
		assertNull(featureJson);
	}

	@Test
	public void notFoundFeatureNotInCollection() {
		String featureJson = featuresDao.getCollectionFeatureJson(collectionsParams.buildParams("AHS","USGS-07227448"));
		assertNull(featureJson);
	}
}
