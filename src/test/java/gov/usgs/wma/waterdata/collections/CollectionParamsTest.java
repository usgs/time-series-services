package gov.usgs.wma.waterdata.collections;

import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_FEATURE_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_SERVER_URL;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_TIME_SERIES_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_LIMIT;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_POINT_LOW_lEFT;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_POINT_UP_RIGHT;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_START_INDEX;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_PREV_START_INDEX;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_NEXT_START_INDEX;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_MON_lOC_TITLE;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_MON_LOC_DESCRIPTION;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_MON_LOC_CONTACT_NAME;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_MON_LOC_CONTACT_EMAIL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gov.usgs.wma.waterdata.ConfigurationService;
import gov.usgs.wma.waterdata.parameter.BoundingBox;

class CollectionParamsTest {

	private static final String TEST_SERVER_URL = "/sever/url";
	private static final String TEST_MON_lOC_TITLE = "monitoring locations title";
	private static final String TEST_MON_LOC_DESCRIPTION = "monitoring locations description";
	private static final String TEST_MON_LOC_CONTACT_NAME = "monitoring locations name";
	private static final String TEST_MON_LOC_CONTACT_EMAIL = "monitoring locations email";

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
		config.setMonLocTitle(TEST_MON_lOC_TITLE);
		config.setMonLocDescription(TEST_MON_LOC_DESCRIPTION);
		config.setMonLocContactName(TEST_MON_LOC_CONTACT_NAME);
		config.setMonLocContactEmail(TEST_MON_LOC_CONTACT_EMAIL);

		builder = new CollectionParams(config);
	}

	@Test
	public void testNotNull() {
		assertNotNull(builder);
	}

	@Test
	public void testCollectionAndCommon() {
		params = builder.buildParams(expectedCollectionId);
		assertCommonParams(6);
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
		assertEquals(null, params.get(PARAM_FEATURE_ID));
	}

	@Test
	public void testNullCollectionId() {
		params = builder.buildParams(null);
		assertCommonParams(5);
		assertEquals(null, params.get(PARAM_COLLECTION_ID));
		assertEquals(null, params.get(PARAM_FEATURE_ID));
	}

	@Test
	public void testCollectionFeature() {
		params = builder.buildParams(expectedCollectionId, expectedFeatureId);
		assertCommonParams(7);
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
		assertEquals(expectedFeatureId, params.get(PARAM_FEATURE_ID));
	}

	@Test
	public void testNullCollectionNotNullFeature() {
		params = builder.buildParams(null, expectedFeatureId);
		assertCommonParams(6);
		assertEquals(null, params.get(PARAM_COLLECTION_ID));
		assertEquals(expectedFeatureId, params.get(PARAM_FEATURE_ID));
	}

	@Test
	public void testNullCollectionNullFeature() {
		params = builder.buildParams(null, null);
		assertCommonParams(5);
		assertEquals(null, params.get(PARAM_COLLECTION_ID));
		assertEquals(null, params.get(PARAM_FEATURE_ID));
	}

	@Test
	public void testCollectionFeatureParms() {
		BoundingBox bbox = new BoundingBox("-180,-90,180,90");
		params = builder.buildParams(expectedCollectionId, 10001, 10, bbox, 15000);
		assertCommonParams(12);
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
		assertEquals(10000, params.get(PARAM_LIMIT));
		assertEquals(10, params.get(PARAM_START_INDEX));
		assertEquals("Point(-180 -90)", params.get(PARAM_POINT_LOW_lEFT));
		assertEquals("Point(180 90)", params.get(PARAM_POINT_UP_RIGHT));
		assertEquals("&startIndex=0&limit=10000", params.get(PARAM_PREV_START_INDEX));
		assertEquals("&startIndex=10010&limit=10000", params.get(PARAM_NEXT_START_INDEX));
	}

	@Test
	public void testCollectionFeatureParmsNoCollectionId() {
		params = builder.buildParams(null, 100, 0, null, 100);
		assertCommonParams(7);
		assertEquals(null, params.get(PARAM_COLLECTION_ID));
		assertEquals(100, params.get(PARAM_LIMIT));
		assertEquals(0, params.get(PARAM_START_INDEX));
		assertNull(params.get(PARAM_POINT_LOW_lEFT));
		assertNull(params.get(PARAM_POINT_UP_RIGHT));
		assertNull(params.get(PARAM_PREV_START_INDEX));
		assertNull(params.get(PARAM_NEXT_START_INDEX));
	}

	@Test
	public void testCollectionFeatureParmsNoLinks() {
		params = builder.buildParams(expectedCollectionId, 100, 0, null, 100);
		assertCommonParams(8);
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
		assertEquals(100, params.get(PARAM_LIMIT));
		assertEquals(0, params.get(PARAM_START_INDEX));
		assertNull(params.get(PARAM_POINT_LOW_lEFT));
		assertNull(params.get(PARAM_POINT_UP_RIGHT));
		assertNull(params.get(PARAM_PREV_START_INDEX));
		assertNull(params.get(PARAM_NEXT_START_INDEX));
	}

	@Test
	public void testCollectionFeatureTimeSeries() {
		params = builder.buildParams(expectedCollectionId, expectedFeatureId, expectedTimeSeriesId);
		assertCommonParams(8);
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
		assertEquals(expectedFeatureId, params.get(PARAM_FEATURE_ID));
		assertEquals(expectedTimeSeriesId, params.get(PARAM_TIME_SERIES_ID));
	}

	private void assertCommonParams(int expectedMapSize) {
		assertNotNull(params);
		assertEquals(expectedMapSize, params.size());
		assertEquals(TEST_SERVER_URL, params.get(PARAM_SERVER_URL));
		assertEquals(TEST_MON_lOC_TITLE, params.get(PARAM_MON_lOC_TITLE));
		assertEquals(TEST_MON_LOC_DESCRIPTION, params.get(PARAM_MON_LOC_DESCRIPTION));
		assertEquals(TEST_MON_LOC_CONTACT_NAME, params.get(PARAM_MON_LOC_CONTACT_NAME));
		assertEquals(TEST_MON_LOC_CONTACT_EMAIL, params.get(PARAM_MON_LOC_CONTACT_EMAIL));
	}
}