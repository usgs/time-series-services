package gov.usgs.wma.waterdata.timeseries;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.waterdata.collections.BaseCollectionsIT;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/timeSeries/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue/")
@DatabaseSetup("classpath:/testData/bestTimeSeries/csv/")
public class BestTimeSeriesIT extends BaseCollectionsIT {
	@Test
	public void timeSeriesCollectionBestFoundTest() throws Exception {
		String url = "/collections/monitoring-locations/items/USGS-07227448/observations/statistical-time-series";

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		HttpStatus actualStatusCode = response.getStatusCode();
		assertThat(actualStatusCode, equalTo(HttpStatus.OK));

		String expectResponseJSON = getCompareFile("bestTimeSeries/monitoring-locations/usgs-07227448-time-series-list.json");
		String actualResponseJSON = response.getBody();
		assertThat(new JSONObject(actualResponseJSON),
				sameJSONObjectAs(new JSONObject(expectResponseJSON)).allowingAnyArrayOrdering());
	}

	@Test
	public void timeSeriesSatCollectionBestFoundTest() throws Exception {
		String url = "/collections/SAT/items/USGS-07227448/observations/statistical-time-series";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

		String expectResponseJSON = getCompareFile("bestTimeSeries/SAT/usgs-07227448-time-series-list.json");
		String actualResponseJSON = response.getBody();
		assertThat(new JSONObject(actualResponseJSON),
				sameJSONObjectAs(new JSONObject(expectResponseJSON)).allowingAnyArrayOrdering());
	}

	@Test
	public void collectionMissingTest() throws Exception {
		String url = "/collections/SOME_COLLECTION/items/USGS-07227448/observations/statistical-time-series";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		HttpStatus actualStatusCode = response.getStatusCode();
		assertThat(actualStatusCode, equalTo(HttpStatus.NOT_FOUND));
		assertEquals(ogc404Payload, response.getBody());
	}

	@Test
	public void featureNotFoundTest() {
		String url = "/collections/monitoring-locations/items/USGS-12345678/observations/statistical-time-series";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertEquals(ogc404Payload, response.getBody());
	}

	@Test
	public void featureNotFoundInStatCollectionTest() {
		String url = "/collections/SAT/items/USGS-12345678/observations/statistical-time-series";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertEquals(ogc404Payload, response.getBody());
	}

	@Test
	public void notFoundNoGeomTest() throws Exception {
		String url = "/collections/monitoring-locations/items/USGS-04028090/observations/statistical-time-series";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertEquals(ogc404Payload, response.getBody());
	}

	@Test
	public void notFoundInStatCollectionNoGeomTest() throws Exception {
		String url = "/collections/SAT/items/USGS-04028090/observations/statistical-time-series";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertEquals(ogc404Payload, response.getBody());
	}
}