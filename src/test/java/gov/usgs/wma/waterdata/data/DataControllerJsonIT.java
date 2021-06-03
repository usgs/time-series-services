package gov.usgs.wma.waterdata.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.waterdata.collections.BaseCollectionsIT;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/timeSeries/")
@DatabaseSetup("classpath:/testData/bestTimeSeries/csv/")
public class DataControllerJsonIT extends BaseCollectionsIT {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	@DatabaseSetup("classpath:/testData/groundwaterDailyValue/")
	public void foundTest() {
		runCase("USGS-07227448", true, "e6a4cc2de5bf437e83efe0107cf026ac.json");
	}

	@Test
	@DatabaseSetup("classpath:/testData/groundwaterDailyValue1095/")
	public void found1095Test() {
		runCase("USGS-07227448", true, "e6a4cc2de5bf437e83efe0107cf026ac_1095.json");
	}

	@Test
	public void mediaTypeNotAcceptableTest() {
		String baseUrl = "/data?monitoringLocationID=USGS-07227448&domain=groundwater_levels&type=statistical_time_series";
		for (String contentType : contentNotAccepted) {
			ResponseEntity<String> rtn = restTemplate.getForEntity(buildUrl(baseUrl, contentType), String.class);
			assertTrue(rtn.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
			assertEquals(ogcNotAcceptedPayload, rtn.getBody());
		}
	}

	@Test
	public void notFoundTest() {
		String url = "/data?monitoringLocationID=USGS-12345678&domain=groundwater_levels&type=statistical_time_series&best=true";
		runErrorCase(url, HttpStatus.NOT_FOUND, ogc404Payload);
	}

	@Test
	public void monitoringLocationIDNotProvidedTest() {
		String url = "/data?domain=groundwater_levels&type=statistical_time_series";
		String desc = "Required request parameter 'monitoringLocationID' for method parameter type String is not present";
		runErrorCase(url, HttpStatus.BAD_REQUEST,
				"{\"code\":\"400\",\"description\":\"" + desc + "\"}");
	}

	@Test
	public void booleanValueNotAcceptedTest() {
		String url = "/data?monitoringLocationID=USGS-12345678&domain=groundwater_levels&type=statistical_time_series";
		String desc = "Json content json is only available with parameter best=true";
		String expectedBody = "{\"code\":\"400\",\"description\":\"" + desc + "\"}";
		runErrorCase(url, HttpStatus.BAD_REQUEST, expectedBody);
		url = url + "&best=false";
		runErrorCase(url, HttpStatus.BAD_REQUEST, expectedBody);
	}

	@Test
	public void badBestValueTest() {
		String url = "/data?monitoringLocationID=USGS-07227448&domain=groundwater_levels&type=statistical_time_series&best=notTrue";
		String desc = "Invalid boolean value [notTrue]";
		runErrorCase(url, HttpStatus.BAD_REQUEST,
				"{\"code\":\"400\",\"description\":\"Error in parameter best:  " + desc + "\"}");
	}

	@Test
	public void badDomainValueTest() {
		String url = "/data?monitoringLocationID=USGS-07227448&domain=xyz&type=statistical_time_series&best=false";
		String desc = "No enum constant gov.usgs.wma.waterdata.parameter.Domain.xyz";
		runErrorCase(url, HttpStatus.BAD_REQUEST,
				"{\"code\":\"400\",\"description\":\"Error in parameter domain:  " + desc + "\"}");
	}

	@Test
	public void badTypeValueTest() {
		String url = "/data?monitoringLocationID=USGS-07227448&domain=groundwater_levels&type=none&best=true";
		String desc = "No enum constant gov.usgs.wma.waterdata.parameter.DataType.none";
		runErrorCase(url, HttpStatus.BAD_REQUEST,
				"{\"code\":\"400\",\"description\":\"Error in parameter type:  " + desc + "\"}");
	}

	private void runErrorCase(String url, HttpStatus expectedStatus, String expectedBody) {
		ResponseEntity<String> rtn = restTemplate.getForEntity(buildUrl(url, "json"), String.class);
		assertThat(rtn.getStatusCode(), equalTo(expectedStatus));
		assertTrue(rtn.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
		assertEquals(expectedBody, rtn.getBody());
	}

	private void runCase(String featureId, Boolean best, String compareFile) {
		String urlFormat = "/data?monitoringLocationID=%s&domain=groundwater_levels&type=statistical_time_series%s&f=json";
		String bestTS = best == null ? "" : "&best=" + best.toString().toLowerCase();
		String url = String.format(urlFormat, featureId, bestTS);

		ResponseEntity<String> rtn = restTemplate.getForEntity(url, String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		assertTrue(rtn.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
		assertNotNull(rtn.getBody());

		try {
			assertThat(new JSONObject(rtn.getBody()),
					sameJSONObjectAs(new JSONObject(getCompareFile(compareFile))));
		} catch (JSONException e) {
			fail("Unexpected JSONException during test", e);
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	private String buildUrl(String baseUrl, String contentType) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
		if (contentType != null) {
			builder = builder.queryParam("f", contentType);
		}
		String url = builder.build().toUri().toString();
		return url;
	}
}
