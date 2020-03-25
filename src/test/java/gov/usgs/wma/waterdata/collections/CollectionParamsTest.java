package gov.usgs.wma.waterdata.collections;

import static org.junit.jupiter.api.Assertions.*;
import static gov.usgs.wma.waterdata.collections.CollectionParams.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import gov.usgs.wma.waterdata.ConfigurationService;

class CollectionParamsTest {

	private static final String TEST_SERVER_URL="/sever/url";

	private String expectedCollectionId = "aCollectionId";
	private String expectedFeatureId = "aFeatureId";
	private String expectedTimeSeriesId = "aGUID";

	private ConfigurationService config;
	private CollectionParams builder;
	private Map<String,Object> params;

	@BeforeEach
	public void before() {
		config = new ConfigurationService();
		config.setServerUrl(TEST_SERVER_URL);

		builder = new CollectionParams(config);
	}

	private void commonAsserts() {
		assertNotNull(params);
		assertEquals(TEST_SERVER_URL, params.get(PARAM_SERVER_URL));
		assertEquals(expectedCollectionId, params.get(PARAM_COLLECTION_ID));
	}
	private void featureAsserts() {
		commonAsserts();
		assertEquals(expectedFeatureId, params.get(PARAM_FEATURE_ID));
	}

	@Test
	void testNotNull() {
		assertNotNull(builder);
	}

	@Test
	void testCollectionAndCommon() {
		params = builder.buildParams(expectedCollectionId);
		commonAsserts();
	}

	@Test
	void testCollectionFeature() {
		params = builder.buildParams(expectedCollectionId, expectedFeatureId);
		featureAsserts();
	}

	@Test
	void testCollectionFeatureTimeSeries() {
		params = builder.buildParams(expectedCollectionId, expectedFeatureId, expectedTimeSeriesId);
		featureAsserts();
		assertEquals(expectedTimeSeriesId, params.get(PARAM_TIME_SERIES_ID));
	}
}
