package gov.usgs.wma.waterdata.collections;

import static gov.usgs.wma.waterdata.collections.ObservationsStatTimeSeriesController.DEFAULT_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.ObservationsStatTimeSeriesController.URL_PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.ObservationsStatTimeSeriesController.URL_PARAM_FEATURE_ID;
import static gov.usgs.wma.waterdata.collections.ObservationsStatTimeSeriesController.URL_PARAM_TIME_SERIES_ID;
import static gov.usgs.wma.waterdata.collections.ObservationsStatTimeSeriesController.URL_STATISTICAL_TIME_SERIES;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/monitoringLocation/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue/")
public class ObservationsStatTimeSeriesControllerIT extends BaseCollectionsIT {
	
	
	protected String makeURL(String collectionId, String featureId, String guid) {
		String url = URL_STATISTICAL_TIME_SERIES.replace(URL_PARAM_COLLECTION_ID, collectionId);
		url = url.replace(URL_PARAM_FEATURE_ID, featureId);
		url = url.replace(URL_PARAM_TIME_SERIES_ID, guid);
		url = "/" + url.replaceAll("[{}]", "");
		System.out.println(url);
		return url;
	}

	@Test
	public void foundTimeSeriesTest() throws Exception {
		String url = makeURL(DEFAULT_COLLECTION_ID, "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac");
//		String url = "/collections/monitoring-locations/items/USGS-07227448/time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		HttpStatus actualStatusCode = response.getStatusCode();
		assertThat(actualStatusCode, equalTo(HttpStatus.OK));

		String expectResponseJSON = getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac.json");
		String actualResponseJSON = response.getBody();
		assertThat(new JSONObject(actualResponseJSON), sameJSONObjectAs(new JSONObject(expectResponseJSON)));
	}
	@Test
	public void timeSeriesNotInCollectionTest() throws Exception {
		String url = makeURL("SOME-COLLECTION", "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac");
//		String url = "/collections/SOME-COLLECTION/items/USGS-07227448/time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		HttpStatus actualStatusCode = response.getStatusCode();
		assertThat(actualStatusCode, equalTo(HttpStatus.NOT_FOUND));
		assertNull(response.getBody());
	}
	@Test
	public void timeSeriesNotInFeatureTest() throws Exception {
		String url = makeURL(DEFAULT_COLLECTION_ID, "OTHER-07227448", "e6a4cc2de5bf437e83efe0107cf026ac");
//		String url = "/collections/monitoring-locations/items/OTHER-07227448/time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		HttpStatus actualStatusCode = response.getStatusCode();
		assertThat(actualStatusCode, equalTo(HttpStatus.NOT_FOUND));
		assertNull(response.getBody());
	}

	@Test
	public void notFoundTimeSeriesTest() {
		String url = makeURL(DEFAULT_COLLECTION_ID, "USGS-12345678", "216d009de8914147a0f9e5237da77854");
//		ResponseEntity<String> response = restTemplate.getForEntity("/collections/monitoring-locations/items/USGS-12345678/time-series/216d009de8914147a0f9e5237da77854", String.class);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertNull(response.getBody());
	}

	@Test
	public void noGeomTimeSeriesTest() {
		String url = makeURL(DEFAULT_COLLECTION_ID, "USGS-04028090", "41a5ff887b744b84a271b65e48d78074");
//		ResponseEntity<String> response = restTemplate.getForEntity("/collections/monitoring-locations/items/USGS-04028090/time-series/41a5ff887b744b84a271b65e48d78074", String.class);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertNull(response.getBody());
	}
}