package gov.usgs.wma.waterdata.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
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

import gov.usgs.wma.waterdata.springinit.BaseIT;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/featuresParams/")
public class FeaturesParamsIT extends BaseIT {
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CollectionParams collectionsParams;

	protected static final List<String> COLLECTION_IDS = List.of("ANC", "monitoring-locations");

	public static final List<String> REL_TYPES = List.of("canonical", "self", "collection", "prev", "next");
	public static final int MONLOC_COUNT = 110;

	@Test
	public void featuresDefaultLimitTest() {
		int maxLimit = collectionsParams.getMaxLimit();
		for (String collectionId : COLLECTION_IDS) {
			try {
				String featuresJsonStr = doCollectionRequest("/collections/" + collectionId + "/items");
				JSONObject featuresJson = new JSONObject(featuresJsonStr);
				assertTrue(featuresJson.get("features") instanceof JSONArray);
				verifyFeatureFields(featuresJson, collectionId, 0, maxLimit, maxLimit);
			} catch (JSONException e) {
				fail("Unexpected JSONException during test", e);
			}
		}
	}

	@Test
	public void limitAndOffsetTest() {
		System.out.println("in two");
		int maxLimit = collectionsParams.getMaxLimit();
		for (String collectionId : COLLECTION_IDS) {
			int[] limits = { 1, 2, maxLimit / 2, maxLimit - 1, maxLimit };
			int[] offsets = { 1, 2, maxLimit / 2, maxLimit - 1, maxLimit };
			for (int l : limits) {
				for (int startIndex : offsets) {
					System.out.println("limit = " + l);
					System.out.println("offset = " + startIndex);
					try {
						String url = String.format("/collections/" + collectionId + "/items?limit=%d&startIndex=%d", l,
								startIndex);
						if (startIndex >= MONLOC_COUNT) {
							doCollectionRequest(url, HttpStatus.NOT_FOUND.value());
						} else {
							String featuresJsonStr = doCollectionRequest(url);
							JSONObject featuresJson = new JSONObject(featuresJsonStr);
							int endIndex = (startIndex + l) - 1;
							System.out.println("endIndex = " + endIndex);
							if (endIndex >= MONLOC_COUNT) {
								endIndex = MONLOC_COUNT - 1;
							}
							System.out.println("endIndex = " + endIndex);
							System.out.println("startIndex = " + startIndex);
							int expectedLength = (endIndex - startIndex) + 1;
							System.out.println("expectedLength = " + expectedLength);
							System.out.println("url = " + url);
							System.out.println("uturesrl = " + featuresJsonStr);
							assertTrue(featuresJson.get("features") instanceof JSONArray);
							verifyFeatureFields(featuresJson, collectionId, startIndex, expectedLength, l);
						}
					} catch (JSONException e) {
						fail("Unexpected JSONException during test", e);
					}
				}
			}
		}
	}

	@Test
	public void limitZeroTest() {
		String featuresJsonStr = doCollectionRequest("/collections/monitoring-locations/items?limit=0",
				HttpStatus.NOT_FOUND.value());
		assertNull(featuresJsonStr);
		featuresJsonStr = doCollectionRequest("/collections/ANC/items?limit=0",
				HttpStatus.NOT_FOUND.value());
		assertNull(featuresJsonStr);
	}

	@Test
	public void featuresBboxTest() {
		double lowerLeftLong = -95.0;
		double lowerLeftLat = 33.0;
		double upRightLong = -100.0;
		double upRightLat = 34.0;

		for (String collectionId : COLLECTION_IDS) {
		try {
			String featuresJsonStr = doCollectionRequest("/collections/" + collectionId + "/items");
			JSONObject featuresJson = new JSONObject(featuresJsonStr);
			List<String> expectedIds = getBboxFeaturedIds(featuresJson, lowerLeftLong, lowerLeftLat, upRightLong,
					upRightLat);
			featuresJsonStr = doCollectionRequest(
					"/collections/monitoring-locations/items?startIndex=" + collectionsParams.getMaxLimit());
			featuresJson = new JSONObject(featuresJsonStr);
			expectedIds.addAll(getBboxFeaturedIds(featuresJson, lowerLeftLong, lowerLeftLat, upRightLong, upRightLat));
			assertFalse(expectedIds.isEmpty());

			System.out.println("ids size = " + expectedIds.size());

			ArrayList<String> returnedIds = new ArrayList<>();
			String bboxUrlParams = String.format("bbox=%f,%f,%f,%f", lowerLeftLong, lowerLeftLat, upRightLong,
					upRightLat);
			featuresJsonStr = doCollectionRequest("/collections/monitoring-locations/items?" + bboxUrlParams);
			featuresJson = new JSONObject(featuresJsonStr);

			assertTrue(featuresJson.get("features") instanceof JSONArray);
			JSONArray features = (JSONArray) featuresJson.get("features");
			for (int i = 0; i < features.length(); i++) {
				assertTrue(features.get(i) instanceof JSONObject);
				JSONObject feature = features.getJSONObject(i);
				returnedIds.add(feature.getString("id"));
			}
			assertEquals(expectedIds, returnedIds);
		} catch (JSONException e) {
			fail("Unexpected JSONException during test", e);
		}
		}
	}

	private List<String> getBboxFeaturedIds(JSONObject featuresJson, double lowerLeftLong, double lowerLeftLat,
			double upRightLong, double upRightLat) throws JSONException {
		JSONArray features = (JSONArray) featuresJson.get("features");
		ArrayList<String> ids = new ArrayList<>();
		for (int i = 0; i < features.length(); i++) {
			assertTrue(features.get(i) instanceof JSONObject);
			JSONObject featureJson = (JSONObject) features.get(i);

			JSONObject geomJson = (JSONObject) featureJson.get("geometry");
			System.out.println("geom =" + geomJson);
			JSONArray coordinates = (JSONArray) geomJson.get("coordinates");
			assertTrue(coordinates.length() == 2);
			Double longitude = coordinates.getDouble(0);
			Double lat = coordinates.getDouble(1);

			if (isBetween(lowerLeftLong, upRightLong, longitude) && isBetween(lowerLeftLat, upRightLat, lat)) {
				ids.add(featureJson.getString("id"));
			}

		}

		return ids;
	}

	private boolean isBetween(double a, double b, double c) {
		return b > a ? c >= a && c <= b : c >= b && c <= a;
	}

	private void verifyFeatureFields(JSONObject featuresJson, String collectionId, int startIndex,
			int length, int limit) throws JSONException {
		assertNotNull(featuresJson.names());
		assertEquals(4, featuresJson.names().length());
		assertEquals("FeatureCollection", featuresJson.getString("type"));
		JSONArray features = (JSONArray) featuresJson.get("features");
		assertTrue(features.length() == length);
		int currIndex = startIndex;
		for (int i = 0; i < features.length(); i++) {
			String suffix = String.format("%06d", currIndex);
			assertTrue(features.get(i) instanceof JSONObject);
			JSONObject featureJson = (JSONObject) features.get(i);
			assertTrue(featureJson.get("properties") instanceof JSONObject);
			JSONObject properties = (JSONObject) featureJson.get("properties");
			assertEquals("USGS-" + suffix, featureJson.getString("id"));
			assertEquals("monitoringLocationName" + suffix, properties.getString("monitoringLocationName"));
			currIndex++;
		}
		verifyFeaturePagingLinks(featuresJson, collectionId, startIndex, limit);
	}

	private void verifyFeaturePagingLinks(JSONObject featuresJson, String collectionId, int startIndex,
			int limit) throws JSONException {
		assertTrue(featuresJson.get("links") instanceof JSONArray);
		JSONArray links = (JSONArray) featuresJson.get("links");
		List<String> returnedRels = new ArrayList<>();
		for (int i = 0; i < links.length(); i++) {
			assertTrue(links.get(i) instanceof JSONObject);
			assertTrue(links.get(i) instanceof JSONObject);
			JSONObject link = (JSONObject) links.get(i);
			String rel = link.getString("rel");
			assertTrue(REL_TYPES.contains(rel), "unknown link rel value returned: " + rel);
			assertFalse(returnedRels.contains(rel), "duplicate link rel value returned: " + rel);
			returnedRels.add(rel);
			verifyLink(link, collectionId, startIndex, limit);
		}
	}

	private void verifyLink(JSONObject link, String collectionId, int startIndex, int limit)
			throws JSONException {
		String rel = link.getString("rel");
		String type = link.getString("type");
		String title = link.getString("title");
		String href = link.getString("href");
		String urlParams = "";
		String serverUrl = collectionsParams.getServerUrl();
		switch (rel) {
		case "canonical":
			assertEquals("text/html", type);
			assertEquals("information", title);
			assertEquals("https://waterdata.usgs.gov", href);
			break;
		case "self":
			assertEquals("application/geo+json", type);
			assertEquals("This document as GeoJSON", title);
			assertEquals(serverUrl + "collections/" + collectionId + "/items?f=json", href);
			break;
		case "collection":
			assertEquals("application/json", type);
			assertEquals(getCollectionTitle(collectionId), title);
			assertEquals(serverUrl + "collections/" + collectionId + "?f=json", href);
			break;
		case "prev":
			int prevStartIndex = startIndex - limit;
			if (prevStartIndex < 0) {
				prevStartIndex = 0;
			}
			urlParams = String.format("&startIndex=%d&limit=%d", prevStartIndex, limit);
			assertEquals("application/geo+json", type);
			assertEquals("items (prev)", title);
			assertEquals(serverUrl + "collections/" + collectionId + "/items?f=json" + urlParams, href);
			break;
		case "next":
			int nextStartIndex = startIndex + limit;
			urlParams = String.format("&startIndex=%d&limit=%d", nextStartIndex, limit);
			assertEquals("application/geo+json", type);
			assertEquals("items (next)", title);
			assertEquals(serverUrl + "collections/" + collectionId + "/items?f=json" + urlParams, href);
			break;
		default:
			fail("Unexpected link rel returned: " + rel);
			break;
		}
	}

	private String doCollectionRequest(String path) {
		return doCollectionRequest(path, HttpStatus.OK.value());
	}

	private String doCollectionRequest(String path, int expectedStatus) {
		ResponseEntity<String> rtn = restTemplate.getForEntity(path, String.class);
		assertEquals(expectedStatus, rtn.getStatusCode().value());
		if (expectedStatus == HttpStatus.OK.value()) {
			assertNotNull(rtn);
			return rtn.getBody();
		} else {
			assertNull(rtn.getBody());
			return null;
		}
	}

	private String getCollectionTitle(String collectionId) {
		String title = "";
		if (collectionId.equals("monitoring-locations")) {
			title = "NWIS Monitoring Locations";
		} else if (collectionId.equals("ANC")) {
			title = "Arkansas Natural Resources Commission Groundwater Network";
		}

		return title;
	}
}