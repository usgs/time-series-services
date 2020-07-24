package gov.usgs.wma.waterdata.timeseries;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import gov.usgs.wma.waterdata.springinit.BaseIT;

/**
 * The '1095' tests specifically tests that timeseries data older than 1095 days is not displayed if not approved.
 * Adding unapproved test data that is older than 1095 doesn't affect other tests (if we correctly filter the data out),
 * however, adding data that is exactly 1095 days old to test the cutoff does affect other tests, so it is done in
 * separate test classes with '1095' in the name.
 */
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/timeSeries/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue1095/")
public class TimeSeriesController1095IT extends BaseIT {

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
					sameJSONObjectAs(new JSONObject(getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac_1095.json"))));
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
					sameJSONObjectAs(new JSONObject(getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac_1095.json"))));
		} catch (JSONException e) {
			fail("Unexpected JSONException during test", e);
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

}
