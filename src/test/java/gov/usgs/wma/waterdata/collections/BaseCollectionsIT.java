package gov.usgs.wma.waterdata.collections;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import gov.usgs.wma.waterdata.springinit.BaseIT;

public abstract class BaseCollectionsIT extends BaseIT {
	@Autowired
	protected TestRestTemplate restTemplate;

	protected void doGetCollectionTest(String path, String resultFile) throws IOException {
		String actual = doCollectionRequest(path);
		String expected = getCompareFile(resultFile);
		assertJsonEquals(expected, actual);
	}

	protected String doCollectionRequest(String path) {
		return doCollectionRequest(path, HttpStatus.OK);
	}

	protected String doCollectionRequest(String path, HttpStatus expectedStatus) {
		ResponseEntity<String> rtn = restTemplate.getForEntity(path, String.class);
		assertNotNull(rtn);
		assertEquals(expectedStatus, rtn.getStatusCode());
		assertNotNull(rtn.getBody());

		return rtn.getBody();
	}

	protected void doJsonCompare(ResponseEntity<String> rtn, String compareFile) {
		doJsonCompare(rtn, compareFile, "");
	}

	protected void doJsonCompare(ResponseEntity<String> rtn, String compareFile, String filterOptions) {
		try {
			String compareJson = getCompareFile(compareFile);
			compareJson = compareJson.replace("{FilterOptions}", filterOptions);
			JSONObject featureCollection = new JSONObject(compareJson);
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

	protected void assertHasExpectedFields(JSONObject featureCollection) throws JSONException {
		assertTrue(featureCollection.names().length() == 4);
		assertNotNull(featureCollection.getString("type"));
		assertTrue(featureCollection.get("features") instanceof JSONArray);
		assertTrue(featureCollection.get("links") instanceof JSONArray);
		assertNotNull(featureCollection.getString("timeStamp"));
	}
}
