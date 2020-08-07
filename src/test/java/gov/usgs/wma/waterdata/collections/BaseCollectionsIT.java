package gov.usgs.wma.waterdata.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

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

}
