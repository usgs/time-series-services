package gov.usgs.wma.waterdata.collections;

import static gov.usgs.wma.waterdata.collections.CollectionParams.DEFAULT_COLLECTION_ID;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.waterdata.ConfigurationService;
import gov.usgs.wma.waterdata.springinit.BaseIT;
import gov.usgs.wma.waterdata.springinit.DBTestConfig;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = {DBTestConfig.class, CollectionParams.class, CollectionsDao.class,
    ConfigurationService.class})
@DatabaseSetup("classpath:/testData/monitoringLocation/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue/")
public class CollectionsDaoIT extends BaseIT {
    @Autowired
    private CollectionParams collectionsParams;

    @Autowired
    private CollectionsDao collectionsDao;

    @BeforeEach
    public void setUp() {
        collectionsParams.builder.clear();
    }

    @Test
    public void foundCollectionsTest() {
        try {
            String expected = getCompareFile("collections.json");
            String actual = collectionsDao.getCollectionsJson(collectionsParams.builder.build());
            assertJsonEquals(expected, actual);
        } catch (IOException e) {
            fail("Unexpected IOException during test", e);
        }
    }

    @Test
    public void foundMonLocCollectionTest() {
        try {
            String expected = getCompareFile("monLocsCollection.json");
            String actual = collectionsDao.getCollectionJson(collectionsParams.builder.collectionId("monitoring-locations").build());
            assertJsonEquals(expected, actual);
        } catch (IOException e) {
            fail("Unexpected IOException during test", e);
        }
    }

    @Test
    public void foundNetworkCollectionTest() {
        try {
            String expected = getCompareFile("ahsCollection.json");
            String actual = collectionsDao.getCollectionJson(collectionsParams.builder.collectionId("AHS").build());
            assertJsonEquals(expected, actual);
        } catch (IOException e) {
            fail("Unexpected IOException during test", e);
        }
    }

    @Test
    public void foundCollectionFeatureTest() {
        try {
            String expected = getCompareFile("features/monitoring-locations/USGS-07227448.json");
            String actual = collectionsDao.getCollectionFeatureJson(
                collectionsParams.builder.collectionId(DEFAULT_COLLECTION_ID)
                .featureId("USGS-07227448").build());
            assertJsonEquals(expected, actual);
        } catch (IOException e) {
            fail("Unexpected IOException during test", e);
        }
    }

    @Test
    public void monLocCollectionFeatureCountTest() {
        Map<String, Object> params = collectionsParams.builder.collectionId(DEFAULT_COLLECTION_ID).build();
        int count = collectionsDao.getCollectionFeatureCount(params);
        assertTrue(count == 3);
    }

    @Test
    public void networkCollectionFeatureCountTest() {
        int count = collectionsDao.getCollectionFeatureCount(
            collectionsParams.builder.collectionId("AHS").build());
        assertTrue(count == 1);
    }

    @Test
    public void notFoundTest() {
        String collectionJson = collectionsDao.getCollectionJson(
            collectionsParams.builder.collectionId("xyz").build());
        assertNull(collectionJson);
    }

    @Test
    public void notFoundFeatureNoCollectionTest() {
        String featureJson = collectionsDao.getCollectionFeatureJson(
            collectionsParams.builder.collectionId("xyz")
            .featureId("USGS-07227448").build());
        assertNull(featureJson);
    }

    @Test
    public void notFoundFeaturesNoCollectionTest() {
        String featureJson = collectionsDao.getCollectionFeaturesJson(
            collectionsParams.builder.collectionId("xyz")
            .featureId("USGS-07227448").build());
        assertNull(featureJson);
    }

    @Test
    public void notFoundFeatureId() {
        String featureJson = collectionsDao.getCollectionFeatureJson(
            collectionsParams.builder.collectionId("monitoring-locations").featureId("xyz").build());
        assertNull(featureJson);
    }

    @Test
    public void notFoundFeatureNoGeom() {
        String featureJson = collectionsDao.getCollectionFeatureJson(
            collectionsParams.builder.collectionId("monitoring-locations").featureId("USGS-04028090").build());
        assertNull(featureJson);
    }

    @Test
    public void notFoundFeatureNotInCollection() {
        String featureJson = collectionsDao.getCollectionFeatureJson(
            collectionsParams.builder.collectionId("AHS").featureId("USGS-07227448").build());
        assertNull(featureJson);
    }

}
