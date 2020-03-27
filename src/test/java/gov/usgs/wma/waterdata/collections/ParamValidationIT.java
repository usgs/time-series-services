package gov.usgs.wma.waterdata.collections;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ParamValidationIT extends BaseCollectionsIT {
	protected String ogcBbox400Payload = "{\"code\":\"400\","
			+ "\"description\":\"The Bbox query parameter is not valid\"}";
	protected String ogcLimit400Payload = "{\"code\":\"400\","
			+ "\"description\":\"limit must be greater than or equal to 1\"}";

	@Test
	public void noBboxValues() {
		String rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=",
				HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

		rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=''", HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

		rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=\"\"", HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);
	}

	@Test
	public void missingBboxValues() {
		String rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=160.6",
				HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

		rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=106.6,-55.95",
				HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

		rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=160.6,-55.95,-170",
				HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);
	}

	@Test
	public void emptyBboxValues() {
		String rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=,",
				HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

		rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=,,", HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

		rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=,,,", HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

		rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=,,,,", HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

		rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=,,,,,", HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);
	}

	@Test
	public void bboxValuesNonNumericTest() {
		String[] nonNumbers = { " ", "a", "-", "+" };
		for(String nonNumeric : nonNumbers) {
	    String path = String.format("/collections/monitoring-locations/items?bbox=%s,-55.95,-170,-25.89", nonNumeric);
		String rtn = doCollectionRequest(path, HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

	    path = String.format("/collections/monitoring-locations/items?bbox=160.6,%s,-170,-25.89", nonNumeric);
		rtn = doCollectionRequest(path, HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

	    path = String.format("/collections/monitoring-locations/items?bbox=160.6,-55.95,%s,-25.89", nonNumeric);
		rtn = doCollectionRequest(path, HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

	    path = String.format("/collections/monitoring-locations/items?bbox=160.6,-55.95,-170,%s", nonNumeric);
		rtn = doCollectionRequest(path, HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);
		}
	}

	@Test
	public void bboxValuesMinTest() {
	    String path = "/collections/monitoring-locations/items?bbox=-180.1,-90,180,90";
		String rtn = doCollectionRequest(path, HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

	    path = "/collections/monitoring-locations/items?bbox=-180,-90.1,180,90";
		rtn = doCollectionRequest(path, HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);
	}

	@Test
	public void bboxValuesMaxTest() {
	    String path = "/collections/monitoring-locations/items?bbox=-180,-90,180.1,90";
		String rtn = doCollectionRequest(path, HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

	    path = "/collections/monitoring-locations/items?bbox=-180,-90.1,180,90.1";
		rtn = doCollectionRequest(path, HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);
	}

	@Test
	public void ogcBboxExampleTest() {
		String rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=160.6,160.0,-170,-25.89",
				HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);

		rtn = doCollectionRequest("/collections/monitoring-locations/items?bbox=160.6,-55.95,-170,160.0",
				HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcBbox400Payload, rtn);
	}

	@Test
	public void limitBoundsTest() {
	    String path = "/collections/monitoring-locations/items?limit=0";
		String rtn = doCollectionRequest(path, HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcLimit400Payload, rtn);

	    path = "/collections/monitoring-locations/items?limit=-1";
		rtn = doCollectionRequest(path, HttpStatus.BAD_REQUEST);
		assertJsonEquals(ogcLimit400Payload, rtn);
	}

}