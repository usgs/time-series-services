package gov.usgs.wma.waterdata.collections;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/featuresFilters/")
public class FeaturesFilterLinksIT extends BaseCollectionsIT  {
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void prevAndNextLinkTestWithFilters() {
		ResponseEntity<String> rtn = restTemplate.getForEntity(
			"/collections/monitoring-locations/items?startIndex=10&limit=2&countryFIPS=US&countryFIPS=MX&agencyCode=USGS", String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		String compareFile = "featureCollection/monitoring-locations/featureCollection_s10l2_with_filters.json";
		doJsonCompare(rtn, compareFile);
	}
}
