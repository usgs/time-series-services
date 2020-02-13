package gov.usgs.wma.waterdata.collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

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
public class ColectionsControllerIT extends BaseIT {
	@Autowired
	private CollectionParams collectionsParams;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void getCollectionsTest() {
		doGetCollectionTest("/collections", "collections.json");
	}

	@Test
	public void getMonLocsCollectionTest() {
		doGetCollectionTest("/collections/monitoring-locations", "monLocsCollection.json");
	}

	@Test
	public void getNetworkCollectionTest() {
		doGetCollectionTest("/collections/AHS", "ahsCollection.json");
	}

	@Test
	public void notFoundTest() {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/xyz", String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
		assertNotNull(rtn);
		assertEquals("Collection with id 'xyz' not found.", rtn.getBody());
	}

	private void doGetCollectionTest(String path, String resultFile) {
		ResponseEntity<String> rtn = restTemplate.getForEntity(path, String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));

		try {
			String actual = rtn.getBody();
			String expected = getCompareFile(resultFile).replace("{serverUrl}", collectionsParams.getServerUrl());
			assertJsonEquals(expected, actual);
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}

	}

}
