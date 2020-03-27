package gov.usgs.wma.waterdata.collections;

import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_FEATURE_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_SERVER_URL;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_TIME_SERIES_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_LIMIT;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_POINT_LOW_lEFT;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_POINT_UP_RIGHT;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_PREV_START_INDEX;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_NEXT_START_INDEX;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gov.usgs.wma.waterdata.ConfigurationService;
import gov.usgs.wma.waterdata.parameter.BoundingBox;

class CollectionParamsTest {

	private static final String TEST_SERVER_URL="/sever/url";

	private String expectedCollectionId = "aCollectionId";
	private String expectedFeatureId = "aFeatureId";
	private String expectedTimeSeriesId = "aGUID";

	private CollectionParams builder;
	private Map<String,Object> params;

	@BeforeEach
	public void before() {
		// codacy wanted instance definition moved here. The construction was here.
		// if access is needed in future tests it will have to be moved back out to instance scope.
		ConfigurationService config = new ConfigurationService();
		config.setServerUrl(TEST_SERVER_URL);

		builder = new CollectionParams(config);
	}

	private boolean commonAsserts() {
		assertNotNull(params);
		assertEquals(TEST_SERVER_URL, params.get(PARAM_SERVER_URL));
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
		return true;
	}
	private boolean featureAsserts() {
		commonAsserts();
		assertEquals(expectedFeatureId, params.get(PARAM_FEATURE_ID));
		return true;
	}

	@Test
	public void testNotNull() {
		assertNotNull(builder);
	}

	@Test
	public void testCollectionAndCommon() {
		params = builder.buildParams(expectedCollectionId);
		assertTrue(commonAsserts());
	}

	@Test
	public void testCollectionFeature() {
		params = builder.buildParams(expectedCollectionId, expectedFeatureId);
		assertTrue(featureAsserts());
	}

	@Test
	public void testCollectionFeatureParms() {
		BoundingBox bbox = new BoundingBox("-180,-90,180,90");
		params = builder.buildParams(expectedCollectionId, 10001, 10, bbox, 15000);
		assertTrue(commonAsserts());
		assertEquals(10000, params.get(PARAM_LIMIT));
		assertEquals("Point(-180 -90)", params.get(PARAM_POINT_LOW_lEFT));
		assertEquals("Point(180 90)", params.get(PARAM_POINT_UP_RIGHT));
		assertEquals("&startIndex=0&limit=10000", params.get(PARAM_PREV_START_INDEX));
		assertEquals("&startIndex=10010&limit=10000", params.get(PARAM_NEXT_START_INDEX));
	}

	@Test
	public void testCollectionFeatureParmsNoLinks() {
		params = builder.buildParams(expectedCollectionId, 100, 0, null, 100);
		assertTrue(commonAsserts());
		assertEquals(100, params.get(PARAM_LIMIT));
		assertNull(params.get(PARAM_POINT_LOW_lEFT));
		assertNull(params.get(PARAM_POINT_UP_RIGHT));
		assertNull(params.get(PARAM_PREV_START_INDEX));
		assertNull(params.get(PARAM_NEXT_START_INDEX));
	}

	@Test
	public void testCollectionFeatureTimeSeries() {
		params = builder.buildParams(expectedCollectionId, expectedFeatureId, expectedTimeSeriesId);
		featureAsserts();
		assertEquals(expectedTimeSeriesId, params.get(PARAM_TIME_SERIES_ID));
	}
}
