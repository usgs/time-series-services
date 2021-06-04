package gov.usgs.wma.waterdata.collections;

import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_FEATURE_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_SERVER_URL;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_TIME_SERIES_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_BEST_TIME_SERIES;
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
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_FILTER_OPTIONS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
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

	private String[] expectedBestTS = {"any", "true", "false"};

	private CollectionParams collectionParams;
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

		collectionParams = new CollectionParams(config);
	}

	@Test
	public void testNotNull() {
		assertNotNull(collectionParams);
	}

	@Test
	public void testCollectionAndCommon() {
		params = collectionParams.builder().collectionId(expectedCollectionId).build();
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
		assertEquals(null, params.get(PARAM_FEATURE_ID));
		assertCommonParams(7);

	}

	@Test
	public void testNullCollectionId() {
		params = collectionParams.builder().build();
		assertCommonParams(6);
		assertEquals(null, params.get(PARAM_COLLECTION_ID));
		assertEquals(null, params.get(PARAM_FEATURE_ID));
	}

	@Test
	public void testCollectionFeature() {
		params = collectionParams.builder().collectionId(expectedCollectionId)
			.featureId(expectedFeatureId).build();
		assertCommonParams(8);
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
		assertEquals(expectedFeatureId, params.get(PARAM_FEATURE_ID));
	}

	@Test
	public void testNullCollectionNotNullFeature() {
		params = collectionParams.builder().featureId(expectedFeatureId).build();
		assertCommonParams(7);
		assertEquals(null, params.get(PARAM_COLLECTION_ID));
		assertEquals(expectedFeatureId, params.get(PARAM_FEATURE_ID));
	}

	@Test
	public void testNullCollectionNullFeature() {
		params = collectionParams.builder().build();
		assertCommonParams(6);
		assertEquals(null, params.get(PARAM_COLLECTION_ID));
		assertEquals(null, params.get(PARAM_FEATURE_ID));
	}

	@Test
	public void testCollectionFeatureParms() {
		BoundingBox bbox = new BoundingBox("-180,-90,180,90");
		params = collectionParams.builder().collectionId(expectedCollectionId)
			.bbox(bbox).paging(10001, 10, 15000).build();
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
		assertEquals(10000, params.get(PARAM_LIMIT));
		assertEquals(10, params.get(PARAM_START_INDEX));
		assertEquals("Point(-180 -90)", params.get(PARAM_POINT_LOW_lEFT));
		assertEquals("Point(180 90)", params.get(PARAM_POINT_UP_RIGHT));
		assertEquals("&startIndex=0&limit=10000", params.get(PARAM_PREV_START_INDEX));
		assertEquals("&startIndex=10010&limit=10000", params.get(PARAM_NEXT_START_INDEX));
		assertCommonParams(13);
	}

	@Test
	public void testCollectionFeatureFilterParams() {
		List<String> countries = new ArrayList<>();
		countries.add("MX");
		countries.add("FR");
		countries.add("KR");
		List<String> states = new ArrayList<>();
		states.add("06");
		states.add("25");
		states.add("55");
		List<String> counties= new ArrayList<>();
		counties.add("06071");
		counties.add("55025");
		List<String> hydrologicUnits = new ArrayList<>();
		hydrologicUnits.add("000000000000");
		List<String> nationalAquiferCodes=new ArrayList<>();
		nationalAquiferCodes.add("N9999OTHER");
		List<String> agencyCodes=new ArrayList<>();
		agencyCodes.add("USGS");
		List<String> monitoringLocationType=List.of("Well");
		List<String> monitoringLocationNumbers=new ArrayList<>();
		monitoringLocationNumbers.add("USGS-72204322");

		params = collectionParams.builder().collectionId(expectedCollectionId)
			.countries(countries).states(states).counties(counties).hydrologicUnits(hydrologicUnits)
			.nationalAquiferCodes(nationalAquiferCodes).agencyCodes(agencyCodes)
			.monitoringLocationNumbers(monitoringLocationNumbers)
			.monitoringLocationType(monitoringLocationType)
			.paging(10001, 10, 15000).build();
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
		assertEquals(10000, params.get(PARAM_LIMIT));
		assertEquals(10, params.get(PARAM_START_INDEX));
		assertEquals("&startIndex=0&limit=10000", params.get(PARAM_PREV_START_INDEX));
		assertEquals("&startIndex=10010&limit=10000", params.get(PARAM_NEXT_START_INDEX));


		assertEquals("&countryFIPS=MX&countryFIPS=FR&countryFIPS=KR" +
			"&countyFIPS=06071&countyFIPS=55025" +
			"&stateFIPS=06&stateFIPS=25&stateFIPS=55" +
			"&hydrologicUnit=000000000000" +
			"&nationalAquiferCode=N9999OTHER" +
			"&agencyCode=USGS" +
			"&monitoringLocationNumber=USGS-72204322" +
			"&monitoringLocationType=Well",
			params.get(PARAM_FILTER_OPTIONS));
		assertCommonParams(19);
	}

	@Test
	public void testCollectionFeatureParmsNoCollectionId() {
		params = collectionParams.builder().paging(100, 0,  100).build();
		assertCommonParams(8);
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
		params = collectionParams.builder().collectionId(expectedCollectionId).paging( 100, 0, 100).build();
		assertCommonParams(9);
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
		params = collectionParams.builder().collectionId(expectedCollectionId)
			.featureId(expectedFeatureId).timeSeriesId(expectedTimeSeriesId).build();
		assertCommonParams(9);
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
		assertEquals(expectedFeatureId, params.get(PARAM_FEATURE_ID));
		assertEquals(expectedTimeSeriesId, params.get(PARAM_TIME_SERIES_ID));
	}

	@Test
	public void testFeatureIdTimeSeriesBest() {
		for (String bestTS : expectedBestTS) {
			params = collectionParams.builder().featureId(expectedFeatureId).
					bestTS(bestTS).build();
			assertCommonParams(8);
			assertEquals(expectedFeatureId, params.get(PARAM_FEATURE_ID));
			assertEquals(bestTS, params.get(PARAM_BEST_TIME_SERIES));
		}
	}

	private void assertCommonParams(int expectedMapSize) {
		assertNotNull(params);
		assertEquals(TEST_SERVER_URL, params.get(PARAM_SERVER_URL));
		assertEquals(TEST_MON_lOC_TITLE, params.get(PARAM_MON_lOC_TITLE));
		assertEquals(TEST_MON_LOC_DESCRIPTION, params.get(PARAM_MON_LOC_DESCRIPTION));
		assertEquals(TEST_MON_LOC_CONTACT_NAME, params.get(PARAM_MON_LOC_CONTACT_NAME));
		assertEquals(TEST_MON_LOC_CONTACT_EMAIL, params.get(PARAM_MON_LOC_CONTACT_EMAIL));
		assertEquals(expectedMapSize, params.size());
	}
}
