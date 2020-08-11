package gov.usgs.wma.waterdata.collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FeaturesFilterIT extends BaseCollectionsIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void countryTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?country=MX&country=US&monitoringLocationType=Well",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featureCollection/monitoring-locations/countries_mx_us.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void countryTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?country=MX",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void countryTestInvalid() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?country=MXUS",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void stateTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?state=26",
            String.class);

        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featureCollection/monitoring-locations/states_mi.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void stateTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?state=57",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void stateTestInvalid() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?state=57000",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void countyTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?county=05051",
            String.class);

        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featureCollection/monitoring-locations/counties_ak_05051.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void countyTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?county=06071",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void countyTestInvalid() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?county=Hartley",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void hydrologicalUnitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicalUnit=040103020107",
            String.class);

        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featureCollection/monitoring-locations/hydrological_unit_040103020107.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicalUnitsTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicalUnit=000000000000",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void hydrologicalUnitsTestInvalid() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicalUnit=INVALID",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void monitoringLocationTypeTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?monitoringLocationType=Well",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featureCollection/monitoring-locations/monitoring_location_type_well.json";
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
    public void agencyCodeTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?agencyCode=Unknown",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void nationalAquiferCodeTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?nationalAquiferCode=N9999OTHER",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featureCollection/monitoring-locations/nat_aqfr_cd_N9999OTHER.json";
        doJsonCompare(rtn, compareFile);

    }

    @Test
    public void nationalAquiferCodeTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?nationalAquiferCode=DOESNTEXIST",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }

    @Test
    public void monitoringLocationNumberTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity(
            "/collections/monitoring-locations/items?monitoringLocationNumber=USGS-343204093005501",
            String.class);

        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featureCollection/monitoring-locations/monitoring_location_number_USGS-343204093005501.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void monitoringLocationNumberTestNotFound() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity(
            "/collections/monitoring-locations/items?monitoringLocationNumber=DOESNTEXIST",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertEquals(ogc404Payload, rtn.getBody());
    }
}
