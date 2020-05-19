package gov.usgs.wma.waterdata.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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


@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/monitoringLocation/")
public class CollectionsControllerIT extends BaseCollectionsIT {
	@Autowired
	private TestRestTemplate restTemplate;

	public static final List<String> COLLECTION_IDS = List.of("AHS", "monitoring-locations");
	public static final List<String> AHS_FEATURE_IDS = List.of("USGS-343204093005501");
	public static final List<String> MONLOC_FEATURE_IDS = List.of("USGS-04027940", "USGS-07227448", "USGS-343204093005501");

	@Test
	public void collectionsToCollectionTest() {
		try {
			String collectionsJsonStr = doCollectionRequest("/collections");
			JSONObject collectionsJson = new JSONObject(collectionsJsonStr);
			assertTrue(collectionsJson.get("collections") instanceof JSONArray);
			JSONArray collections = (JSONArray) collectionsJson.get("collections");
			assertTrue(collections.length() == COLLECTION_IDS.size());
			ArrayList<String> returnedCollectionIds = new ArrayList<>();
			for(int i=0; i < collections.length(); i++) {
				assertTrue(collections.get(i) instanceof JSONObject);
				JSONObject collectionJson = (JSONObject) collections.get(i);
				String collectionId = collectionJson.getString("id");
				assertNotNull(collectionId);
				assertTrue(COLLECTION_IDS.contains(collectionId), "Unknown collection id returned: " + collectionId);
				assertFalse(returnedCollectionIds.contains(collectionId), "Duplicate collection id returned: " + collectionId);
				String returnedCollection = doCollectionRequest("/collections/" + collectionId);
				assertJsonEquals(collectionJson.toString(), returnedCollection);
				returnedCollectionIds.add(collectionId);
			}
		} catch (JSONException e) {
			fail("Unexpected JSONException during test", e);
		}
	}

	@Test
	public void getCollectionsTest() {
		try {
			doGetCollectionTest("/collections", "collections.json");
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void getMonLocsCollectionTest() {
		try {
			doGetCollectionTest("/collections/monitoring-locations", "monLocsCollection.json");
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void getNetworkCollectionTest() {
		try {
			doGetCollectionTest("/collections/AHS", "ahsCollection.json");
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void collectionNotFoundTest() {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/xyz", String.class);
		assertNotNull(rtn);
		assertEquals(HttpStatus.NOT_FOUND.value(), rtn.getStatusCode().value());
		assertEquals(ogc404Payload, rtn.getBody());
	}
}
