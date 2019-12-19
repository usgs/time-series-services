package gov.usgs.wma.waterdata.timeseries;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
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

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/monitoringLocation/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue/")
public class TimeSeriesControllerIT extends BaseIT {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void foundTest() {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/monitoring-location/USGS-07227448/time-series/e6a4cc2de5bf437e83efe0107cf026ac", String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));

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
		ResponseEntity<String> rtn = restTemplate.getForEntity("/monitoring-location/USGS-12345678/time-series/216d009de8914147a0f9e5237da77854", String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertNull(rtn.getBody());
	}

	@Test
	public void noGeomTest() {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/monitoring-location/USGS-04028090/time-series/41a5ff887b744b84a271b65e48d78074", String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertNull(rtn.getBody());
	}
}
