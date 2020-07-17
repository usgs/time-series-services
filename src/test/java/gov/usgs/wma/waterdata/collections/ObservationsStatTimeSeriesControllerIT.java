package gov.usgs.wma.waterdata.collections;

import static gov.usgs.wma.waterdata.collections.CollectionParams.DEFAULT_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_FEATURE_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_TIME_SERIES_ID;
import static gov.usgs.wma.waterdata.collections.ObservationsStatTimeSeriesController.URL_STATISTICAL_TIME_SERIES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/monitoringLocation/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue/")
public class ObservationsStatTimeSeriesControllerIT extends BaseCollectionsIT {

	protected static String makeURL(String collectionId, String featureId, String guid) {
		String url = URL_STATISTICAL_TIME_SERIES.replace(PARAM_COLLECTION_ID, collectionId);
		url = url.replace(PARAM_FEATURE_ID, featureId);
		url = url.replace(PARAM_TIME_SERIES_ID, guid);
		url = "/" + url.replaceAll("[{}]", "");
		return url;
	}

	@Test
	public void foundTimeSeriesTest() throws Exception {
		String url = makeURL(DEFAULT_COLLECTION_ID, "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac");
		String rtn = doCollectionRequest(url, HttpStatus.OK);
		String expectResponseJSON = getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac.json");
		assertThat(new JSONObject(rtn), sameJSONObjectAs(new JSONObject(expectResponseJSON)));
	}

	@Test
	public void timeSeriesNotInCollectionTest() {
		String url = makeURL("SOME-COLLECTION", "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac");
		String rtn = doCollectionRequest(url, HttpStatus.NOT_FOUND);
		assertEquals(ogc404Payload, rtn);
	}

	@Test
	public void timeSeriesNotInFeatureTest() {
		String url = makeURL(DEFAULT_COLLECTION_ID, "OTHER-07227448", "e6a4cc2de5bf437e83efe0107cf026ac");
		String rtn = doCollectionRequest(url, HttpStatus.NOT_FOUND);
		assertEquals(ogc404Payload, rtn);
	}

	@Test
	public void notFoundTimeSeriesTest() {
		String url = makeURL(DEFAULT_COLLECTION_ID, "USGS-12345678", "216d009de8914147a0f9e5237da77854");
		String rtn = doCollectionRequest(url, HttpStatus.NOT_FOUND);
		assertEquals(ogc404Payload, rtn);
	}

	@Test
	public void noGeomTimeSeriesTest() {
		String url = makeURL(DEFAULT_COLLECTION_ID, "USGS-04028090", "41a5ff887b744b84a271b65e48d78074");
		String rtn = doCollectionRequest(url, HttpStatus.NOT_FOUND);
		assertEquals(ogc404Payload, rtn);
	}
}