package gov.usgs.wma.waterdata.timeseries;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.xmlunit.matchers.CompareMatcher;

import java.io.IOException;

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

import gov.usgs.wma.waterdata.springinit.BaseIT;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/timeSeries/")
public class TimeSeriesControllerXmlIT extends BaseIT {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	@DatabaseSetup("classpath:/testData/groundwaterDailyValue/")
	public void foundTest() {
		runCase("monitoring-locations", "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac",
				"xml/e6a4cc2de5bf437e83efe0107cf026ac.xml");
		runCase("SAT", "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac", "xml/e6a4cc2de5bf437e83efe0107cf026ac.xml");
	}

	@Test
	@DatabaseSetup("classpath:/testData/groundwaterDailyValue1095/")
	public void found1095Test() {
		runCase("monitoring-locations", "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac",
				"xml/e6a4cc2de5bf437e83efe0107cf026ac_1095.xml");
		runCase("SAT", "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac",
				"xml/e6a4cc2de5bf437e83efe0107cf026ac_1095.xml");
	}

	@Test
	public void mediaTypeNotAcceptableTest() {
		String baseUrl = "/collections/monitoring-locations/items/USGS-07227448/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		for (String contentType : contentNotAccepted) {
			ResponseEntity<String> rtn = restTemplate.getForEntity(buildUrl(baseUrl, contentType), String.class);
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
			assertEquals(ogcNotAcceptedPayload, rtn.getBody());
		}
	}

	@Test
	public void mediaTypeNotAcceptableSatTest() {
		String baseUrl = "/collections/SAT/items/USGS-07227448/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		for (String contentType : contentNotAccepted) {
			ResponseEntity<String> rtn = restTemplate.getForEntity(buildUrl(baseUrl, contentType), String.class);
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
			assertThat(rtn.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON));
			assertEquals(ogcNotAcceptedPayload, rtn.getBody());
		}
	}

	@Test
	public void notFoundTest() {
		String url = "/collections/monitoring-locations/items/USGS-12345678/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> rtn = restTemplate.getForEntity(buildUrl(url, "xml"), String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertThat(rtn.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON));
		assertEquals(ogc404Payload, rtn.getBody());
	}

	@Test
	public void notFoundInSatCollectionTest() {
		String url = "/collections/monitoring-locations/items/USGS-12345678/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> rtn = restTemplate.getForEntity(buildUrl(url, "xml"), String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertThat(rtn.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON));
		assertEquals(ogc404Payload, rtn.getBody());
	}

	@Test
	public void noGeomTest() {
		String url = "/collections/monitoring-locations/items/USGS-04028090/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> rtn = restTemplate.getForEntity(buildUrl(url, "xml"), String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertThat(rtn.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON));
		assertEquals(ogc404Payload, rtn.getBody());
	}

	@Test
	public void noGeomSatCollectionTest() {
		String url = "/collections/SAT/items/USGS-04028090/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		ResponseEntity<String> rtn = restTemplate.getForEntity(buildUrl(url, "xml"), String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertThat(rtn.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_JSON));
		assertEquals(ogc404Payload, rtn.getBody());
	}

	private void runCase(String collectionId, String featureId, String tsid, String compareFile) {
		String urlFormat = "/collections/%s/items/%s/observations/statistical-time-series/%s?f=xml";
		String url = String.format(urlFormat, collectionId, featureId, tsid);
		ResponseEntity<String> rtn = restTemplate.getForEntity(url, String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(rtn.getHeaders().getContentType(), equalTo(MediaType.APPLICATION_XML));
		assertNotNull(rtn.getBody());

		try {
			String expectedXml = harmonizeXml(getCompareFile(compareFile));
			expectedXml = expectedXml.replaceAll("<wml2:generationDate>.*</wml2:generationDate>", "");
			expectedXml = expectedXml.replace("[collectionId]", collectionId);

			String actualXml = rtn.getBody();
			actualXml = actualXml.replaceAll("<wml2:generationDate>.*</wml2:generationDate>", "");
			assertThat(actualXml, CompareMatcher.isIdenticalTo(expectedXml));
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
