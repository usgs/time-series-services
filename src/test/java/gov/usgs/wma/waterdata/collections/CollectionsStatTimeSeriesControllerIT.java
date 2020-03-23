package gov.usgs.wma.waterdata.collections;

import static gov.usgs.wma.waterdata.collections.CollectionsDao.DEFAULT_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionsController.URL_PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionsController.URL_PARAM_FEATURE_ID;
import static gov.usgs.wma.waterdata.collections.CollectionsStatTimeSeriesController.URL_STATISTICAL_TIME_SERIES_COLLECTION;
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
public class CollectionsStatTimeSeriesControllerIT extends BaseCollectionsIT {
	
	
	protected String makeURL(String collectionId, String featureId) {
		String url = URL_STATISTICAL_TIME_SERIES_COLLECTION.replace(URL_PARAM_COLLECTION_ID, collectionId);
		url = url.replace(URL_PARAM_FEATURE_ID, featureId);
		url = "/" + url.replaceAll("[{}]", "");
		System.out.println(url);
		return url;
	}
	
	@Test
	public void featureTimeSeriesCollectionTest() throws Exception {
		String url = makeURL(DEFAULT_COLLECTION_ID, "USGS-07227448"); //USGS-07227448 has two test time series
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		HttpStatus actualStatusCode = response.getStatusCode();
		assertThat(actualStatusCode, equalTo(HttpStatus.OK));

		String expectResponseJSON = getCompareFile("usgs-07227448-obs-list.json");
		String actualResponseJSON = response.getBody();
		assertThat(new JSONObject(actualResponseJSON), sameJSONObjectAs(new JSONObject(expectResponseJSON)));
	}

	@Test
	public void collectionMissingTest() throws Exception {
		String url = makeURL("SOME-COLLECTION", "USGS-07227448");
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		HttpStatus actualStatusCode = response.getStatusCode();
		assertThat(actualStatusCode, equalTo(HttpStatus.NOT_FOUND));
		assertNull(response.getBody());
	}

	@Test
	public void featureNotFoundTest() {
		String url = makeURL(DEFAULT_COLLECTION_ID, "USGS-12345678");
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertNull(response.getBody());
	}

	@Test
	public void notFoundNoGeomTest() throws Exception {
		String url = makeURL(DEFAULT_COLLECTION_ID, "USGS-04028090"); //USGS-04028090 has one test time series
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		HttpStatus actualStatusCode = response.getStatusCode();
		assertThat(actualStatusCode, equalTo(HttpStatus.NOT_FOUND));
	}
}