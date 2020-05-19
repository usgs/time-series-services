package gov.usgs.wma.waterdata;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT,
	properties={"site.url.base=/"})
public class RedirectControllerTest {
	private static final Logger LOG = LoggerFactory.getLogger(RedirectControllerTest.class);

	@Test
	public void getVersionTest(@Autowired TestRestTemplate restTemplate) throws Exception {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/version", String.class);
		LOG.info(rtn.toString());
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		assertTrue(rtn.getBody().contains("\"artifact\":\"time-series-services\""));
	}

	@Test
	public void getSwaggerTest(@Autowired TestRestTemplate restTemplate) throws Exception {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/swagger", String.class);
		LOG.info(rtn.toString());
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		assertTrue(rtn.getBody().contains("Swagger UI"));
	}
}
