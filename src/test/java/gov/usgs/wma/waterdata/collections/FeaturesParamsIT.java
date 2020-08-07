package gov.usgs.wma.waterdata.collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import java.io.IOException;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/featuresParams/")
public class FeaturesParamsIT extends BaseCollectionsIT  {
	@Autowired
	private TestRestTemplate restTemplate;

	protected static final List<String> COLLECTION_IDS = List.of("ANC", "monitoring-locations");
	protected static final int MONLOC_COUNT = 20;

	@Test
	public void noPagingTest() throws IOException {
		for (String collectionId : COLLECTION_IDS) {
			ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/" + collectionId + "/items",
					String.class);
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
			String compareFile = "featureCollection/" + collectionId + "/featureCollection.json";
			doJsonCompare(rtn, compareFile);

			rtn = restTemplate.getForEntity("/collections/" + collectionId + "/items?startIndex=0",
					String.class);
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
			doJsonCompare(rtn, compareFile);
		}
	}




	@Test
	public void noPrevLinkTest() {
		for (String collectionId : COLLECTION_IDS) {
			ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/" + collectionId + "/items?limit=1",
					String.class);
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
			String compareFile = "featureCollection/"  + collectionId + "/featureCollection_s0l1.json";
			doJsonCompare(rtn, compareFile);

			rtn = restTemplate.getForEntity("/collections/" + collectionId + "/items?startIndex=0&limit=1",
					String.class);
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
			doJsonCompare(rtn, compareFile);
		}
	}

	@Test
	public void prevAndNextLinkTest() {
		for (String collectionId : COLLECTION_IDS) {
			ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/" + collectionId + "/items?startIndex=10&limit=2", String.class);
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
			String compareFile = "featureCollection/" + collectionId + "/featureCollection_s10l2.json";
			doJsonCompare(rtn, compareFile);
		}
	}

	@Test
	public void noNextLinkTest() {
		for (String collectionId : COLLECTION_IDS) {
			ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/" + collectionId + "/items?startIndex=19&limit=10", String.class);
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
			String compareFile = "featureCollection/" + collectionId + "/featureCollection_s19l10.json";
			doJsonCompare(rtn, compareFile);
		}
	}

	@Test
	public void paramBoundsTest() {
		for (String collectionId : COLLECTION_IDS) {
			String rtn = doCollectionRequest("/collections/" + collectionId + "/items?startIndex=" + MONLOC_COUNT,
					HttpStatus.NOT_FOUND);
			assertEquals(ogc404Payload, rtn);
		}
	}

	@Test
	public void featuresBboxTest() {
		String lowerLeftLong = "-101.0";
		String lowerLeftLat = "33.0";
		String upRightLong = "-90.0";
		String upRightLat = "36.0";

		for (String collectionId : COLLECTION_IDS) {
			String bboxUrlParams = String.format("bbox=%s,%s,%s,%s", lowerLeftLong, lowerLeftLat, upRightLong,
					upRightLat);
			ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/" + collectionId + "/items?" + bboxUrlParams, String.class);
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
			String compareFile = "featureCollection/" + collectionId + "/featureCollection_bbox.json";
			doJsonCompare(rtn, compareFile);
		}
	}

	@Test
	public void featuresSmallestBboxTest() {
		String lowerLeftLong = "-100.3706884";
		String lowerLeftLat ="35.935042";
		String upRightLong = "-100.3706884";
		String upRightLat = "35.935042";

		for (String collectionId : COLLECTION_IDS) {
			String bboxUrlParams = String.format("bbox=%s,%s,%s,%s", lowerLeftLong, lowerLeftLat, upRightLong,
					upRightLat);
			ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/" + collectionId + "/items?" + bboxUrlParams, String.class);
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
			String compareFile = "featureCollection/" + collectionId + "/featureCollection_bbox_one.json";
			doJsonCompare(rtn, compareFile);
		}
	}

	@Test
	public void featuresBiggestBboxTest() {
		String lowerLeftLong = "-180";
		String lowerLeftLat = "-90";
		String upRightLong = "180";
		String upRightLat = "90";

		for (String collectionId : COLLECTION_IDS) {
			String bboxUrlParams = String.format("bbox=%s,%s,%s,%s", lowerLeftLong, lowerLeftLat, upRightLong,
					upRightLat);
			ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/" + collectionId + "/items?" + bboxUrlParams, String.class);
			assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
			String compareFile = "featureCollection/" + collectionId + "/featureCollection.json";
			doJsonCompare(rtn, compareFile);
		}
	}



	@Test
	public void countryTest() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?country=MX&country=US",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		String compareFile = "featureCollection/monitoring-locations/countries_mx_us.json";
		doJsonCompare(rtn, compareFile);
	}

	@Test
	public void countryTestNotFound() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?country=MX",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}

	@Test
	public void countryTestInvalid() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?country=MXUS",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
	}



	@Test
	public void stateTest() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?state=26",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		String compareFile = "featureCollection/monitoring-locations/states_mi.json";
		doJsonCompare(rtn, compareFile);
	}


	@Test
	public void stateTestNotFound() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?state=57",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}


	@Test
	public void stateTestInvalid() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?state=57000",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
	}

	@Test
	public void countyTest() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?county=48205",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		String compareFile = "featureCollection/monitoring-locations/counties_tx_48205.json";
		doJsonCompare(rtn, compareFile);
	}

	@Test
	public void countyTestNotFound() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?county=06071",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}

	@Test
	public void countyTestInvalid() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?county=Hartley",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
	}


	@Test
	public void hydrologicalUnitsTest() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicalUnit=040103020107",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		String compareFile = "featureCollection/monitoring-locations/hydrological_unit_040103020107.json";
		doJsonCompare(rtn, compareFile);
	}

	@Test
	public void hydrologicalUnitsTestNotFound() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicalUnit=000000000000",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}


	@Test
	public void hydrologicalUnitsTestInvalid() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicalUnit=INVALID",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
	}


	@Test
	public void monitoringLocationTypeTest() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?monitoringLocationType=Well",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		String compareFile = "featureCollection/monitoring-locations/monitoring_location_type_well.json";
		doJsonCompare(rtn, compareFile);
	}

	@Test
	public void monitoringLocationTypeTestNotFound() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?monitoringLocationType=Unknown",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}

	@Test
	public void agencyCodeTest() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?agencyCode=USGS",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
	}

	@Test
	public void agencyCodeTestNotFound() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?agencyCode=Unknown",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}

	@Test
	public void nationalAquiferCodeTest() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?nationalAquiferCode=N9999OTHER",
			String.class);

		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		String compareFile = "featureCollection/monitoring-locations/nat_aqfr_cd_N9999OTHER.json";
		doJsonCompare(rtn, compareFile);

	}

	@Test
	public void nationalAquiferCodeTestNotFound() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?nationalAquiferCode=DOESNTEXIST",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}

	@Test
	public void monitoringLocationNumberTest() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity(
			"/collections/monitoring-locations/items?monitoringLocationNumber=USGS-343204093005501",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		String compareFile = "featureCollection/monitoring-locations/monitoring_location_number_USGS-343204093005501.json";
		doJsonCompare(rtn, compareFile);
	}

	@Test
	public void monitoringLocationNumberTestNotFound() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity(
			"/collections/monitoring-locations/items?monitoringLocationNumber=DOESNTEXIST",
			String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}


	private void doJsonCompare(ResponseEntity<String> rtn, String compareFile) {
		try {
			JSONObject featureCollection = new JSONObject(getCompareFile(compareFile));
			JSONObject returnedJson = new JSONObject(rtn.getBody());
			assertHasExpectedFields(returnedJson);
			assertThat(returnedJson,
					sameJSONObjectAs(featureCollection).allowingExtraUnexpectedFields());
		} catch (JSONException e) {
			fail("Unexpected JSONException during test", e);
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	private void assertHasExpectedFields(JSONObject featureCollection) throws JSONException {
		assertTrue(featureCollection.names().length() == 4);
		assertNotNull(featureCollection.getString("type"));
		assertTrue(featureCollection.get("features") instanceof JSONArray);
		assertTrue(featureCollection.get("links") instanceof JSONArray);
		assertNotNull(featureCollection.getString("timeStamp"));
	}

}
