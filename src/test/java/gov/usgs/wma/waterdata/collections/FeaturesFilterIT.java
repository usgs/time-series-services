package gov.usgs.wma.waterdata.collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/featuresFilter/")
public class FeaturesFilterIT extends BaseCollectionsIT {
    @Autowired
    private TestRestTemplate restTemplate;

    private String badRequestPayloadMonLocNum = 
        "{\"code\":\"400\",\"description\":\"" + CollectionsController.REGEX_MON_LOC_NUMBER_MESS + "\"}";
    
    private String badRequestPayloadNatAqfrCode = 
        "{\"code\":\"400\",\"description\":\"" + CollectionsController.REGEX_NATIONAL_AQUIFER_CODE_MESS + "\"}";
    
    @Test
    public void countryTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?country=MX&country=US&monitoringLocationType=Well",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/countries_mx_us.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void countryTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?countryFIPS=MX",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void countryTestInvalid() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?countryFIPS=MXUS",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void stateTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?stateFIPS=US:26",
            String.class);

        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/states_mi.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void stateTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?stateFIPS=US:99",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void siteActiveTestTrueFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?siteActive=true",
            String.class);
            assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
            String compareFile = "featuresFilter/monitoring-locations/monitoring_location_type_multi_values.json";
            doJsonCompare(rtn, compareFile);
    }

    @Test
    public void siteActiveTestFalseFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?siteActive=false",
            String.class);
            assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
            String compareFile = "featuresFilter/monitoring-locations/monitoring_location_type_multi_values.json";
            doJsonCompare(rtn, compareFile);
    }

    

    @Test
    public void stateTestInvalid() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?stateFIPS=57000",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void countyTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?countyFIPS=US:05:051",
            String.class);

        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/counties_ak_05051.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void countyTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?countyFIPS=US:00:051",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void countyTestInvalid() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?countyFIPS=Hartley",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void hydrologicUnitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=040103020107",
            String.class);

        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_040103020107.json";
        doJsonCompare(rtn, compareFile);
    }

	@Test
	public void hydrologicUnitsMultiValuesTest() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity(
				"/collections/monitoring-locations/items?hydrologicUnit=040103020107&hydrologicUnit=080401010805",
				String.class);

		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_multi_values.json";
		doJsonCompare(rtn, compareFile);
	}

    @Test
    public void hydrologicUnitsTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=000000000000",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void hydrologicUnitsTestInvalid() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=INVALID",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void monitoringLocationTypeTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?monitoringLocationType=Well",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/monitoring_location_type_well.json";
        doJsonCompare(rtn, compareFile);
    }

	@Test
	public void monitoringLocationTypeMultiValuesTest() throws IOException {
		ResponseEntity<String> rtn = restTemplate.getForEntity(
				"/collections/monitoring-locations/items?monitoringLocationType=Well&monitoringLocationType=Stream",
				String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		String compareFile = "featuresFilter/monitoring-locations/monitoring_location_type_multi_values.json";
		doJsonCompare(rtn, compareFile);
	}

    @Test
    public void monitoringLocationTypeTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?monitoringLocationType=Unknown",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void agencyCodeTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?agencyCode=USGS",
            String.class);

        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
    }


    @Test
    public void agencyCodeMultiValuesTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?agencyCode=USGS&agencyCode=DOESNTEXIST",
            String.class);

        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void agencyCodeTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?agencyCode=DoesntExist",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void nationalAquiferCodeTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?nationalAquiferCode=N9999OTHER",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/nat_aqfr_cd_N9999OTHER.json";
        doJsonCompare(rtn, compareFile);

    }

    @Test
    public void nationalAquiferCodeMultiValuesTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?nationalAquiferCode=N9999OTHER&nationalAquiferCode=Z1234HMMMM",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/nat_aqfr_cd_N9999OTHER.json";
        doJsonCompare(rtn, compareFile);

    }

    @Test
    public void nationalAquiferCodeTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?nationalAquiferCode=Z0000000000DOESNTEXIST",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void nationalAquiferCodeTestInvalid() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?nationalAquiferCode=INVALID",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertEquals(badRequestPayloadNatAqfrCode, rtn.getBody());
    }

    @Test
    public void monitoringLocationNumberTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity(
            "/collections/monitoring-locations/items?monitoringLocationNumber=343204093005501",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/monitoring_location_number_USGS-343204093005501.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void monitoringLocationNumberMultiValuesTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity(
            "/collections/monitoring-locations/items?monitoringLocationNumber=343204093005501&monitoringLocationNumber=000000000000",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/monitoring_location_number_USGS-343204093005501.json";
        doJsonCompare(rtn, compareFile);
    }
    
    @Test
    public void monitoringLocationNumberTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity(
            "/collections/monitoring-locations/items?monitoringLocationNumber=8888888888888888888888888888",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void monitoringLocationNumberTestInvalid() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity(
            "/collections/monitoring-locations/items?monitoringLocationNumber=INVALID",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertEquals(badRequestPayloadMonLocNum, rtn.getBody());
    }
}
