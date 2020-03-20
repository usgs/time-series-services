package gov.usgs.wma.waterdata.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/monitoringLocation/")
public class CollectionsObservationsIT extends BaseCollectionsIT {
	@Test
	public void getObservationsTest() {
		try {
			doGetCollectionTest("/collections/monitoring-locations/items/USGS-07227448/observations", "observations.json");
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	public void featureNotFoundTest() {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/AHS/items/xyz/observations", String.class);
		assertEquals(HttpStatus.NOT_FOUND.value(), rtn.getStatusCode().value());
		assertNull(rtn.getBody());
	}

	@Test
	public void featureNotFoundNoGeomTest() {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items/USGS-0402809/observations", String.class);
		assertEquals(HttpStatus.NOT_FOUND.value(), rtn.getStatusCode().value());
		assertNull(rtn.getBody());
	}

	@Test
	public void featureExistsInAnotherCollectionTest() {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/AHS/items/USGS-07227448/observations", String.class);
		assertEquals(HttpStatus.NOT_FOUND.value(), rtn.getStatusCode().value());
		assertNull(rtn.getBody());
	}
}