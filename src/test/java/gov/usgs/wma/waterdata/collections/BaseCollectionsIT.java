package gov.usgs.wma.waterdata.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
		System.out.println("expected = " + expected);
		System.out.println("actual = " + actual);
		assertJsonEquals(expected, actual);
	}
	
	protected String doCollectionRequest(String path) {
		ResponseEntity<String> rtn = restTemplate.getForEntity(path, String.class);
		assertEquals(HttpStatus.OK.value(), rtn.getStatusCode().value());
		assertNotNull(rtn);

		return rtn.getBody();
	}

	protected String doCollectionRequest(String path, int expectedStatus) {
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

}