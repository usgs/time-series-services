package gov.usgs.wma.waterdata.timeseries;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.waterdata.springinit.BaseIT;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/timeSeries/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue/")
public class TimeSeriesControllerIT extends BaseIT {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void foundTest() {
		String url = "/collections/monitoring-locations/items/USGS-07227448/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> rtn = restTemplate.getForEntity(url, String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		assertNotNull(rtn.getBody());

		try {
			assertThat(new JSONObject(rtn.getBody()),
					sameJSONObjectAs(new JSONObject(getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac.json"))));
		} catch (JSONException e) {
			fail("Unexpected JSONException during test", e);
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void foundInSatCollectionTest() {
		String url = "/collections/SAT/items/USGS-07227448/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> rtn = restTemplate.getForEntity(url, String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		assertNotNull(rtn.getBody());

		try {
			assertThat(new JSONObject(rtn.getBody()),
					sameJSONObjectAs(new JSONObject(getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac.json"))));
		} catch (JSONException e) {
			fail("Unexpected JSONException during test", e);
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void notFoundTest() {
		String url = "/collections/monitoring-locations/items/USGS-12345678/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> rtn = restTemplate.getForEntity(url, String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertEquals(ogc404Payload, rtn.getBody());
	}

	@Test
	public void notFoundInSatCollectionTest() {
		String url = "/collections/monitoring-locations/items/USGS-12345678/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> rtn = restTemplate.getForEntity(url, String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertEquals(ogc404Payload, rtn.getBody());
	}

	@Test
	public void noGeomTest() {
		String url = "/collections/monitoring-locations/items/USGS-04028090/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> rtn = restTemplate.getForEntity(url, String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertEquals(ogc404Payload, rtn.getBody());
	}

	@Test
	public void noGeomSatCollectionTest() {
		String url = "/collections/SAT/items/USGS-04028090/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> rtn = restTemplate.getForEntity(url, String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertEquals(ogc404Payload, rtn.getBody());
	}
}
