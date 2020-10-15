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

import java.io.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
// this tests varying lengths of Hydrological Unit Tests
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/hucLength/")
//@DatabaseSetup("classpath:/testData/featuresFilter/")
public class FeaturesFilterHucLenIT extends BaseCollectionsIT {
    @Autowired
    private TestRestTemplate restTemplate;

    private String badRequestPayloadMonLocNum = 
        "{\"code\":\"400\",\"description\":\"" + CollectionsController.REGEX_MON_LOC_NUMBER_MESS + "\"}";
    
    private String badRequestPayloadNatAqfrCode = 
        "{\"code\":\"400\",\"description\":\"" + CollectionsController.REGEX_NATIONAL_AQUIFER_CODE_MESS + "\"}";

    private static final String badRequestHucLength =
        "{\"code\":\"400\",\"description\":\"" + CollectionsController.REGEX_FIPS_HYDRO_MESS + "\"}";
    
  
    @Test
    public void hydrologicUnits02Len12Test() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=020103020107",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_020103020107.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicUnits04Len12Test() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=040103020109",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_040103020109.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicUnitsMatch04Len1DigitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=0",
            String.class);
            assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
            assertEquals(badRequestHucLength, rtn.getBody());
    }
    
    @Test
    public void hydrologicUnitsMatch04Len2DigitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=04",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_040103020109.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicUnitsMatch044Len3igitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=040",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertEquals(badRequestHucLength, rtn.getBody());
    }

    @Test
    public void hydrologicUnitsMatch04Len4DigitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=0401",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_040103020109.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicUnitsMatch04Len5DigitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=04010",
            String.class);
            assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
            assertEquals(badRequestHucLength, rtn.getBody());
    }

    @Test
    public void hydrologicUnitsMatch04Len6DigitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=040103",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_040103020109.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicUnitsMatch04Len7DigitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=0401030",
            String.class);
            assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
            assertEquals(badRequestHucLength, rtn.getBody());
    }

    @Test
    public void hydrologicUnitsMatch04Len8DigitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=04010302",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_040103020109.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicUnitsMatch04Len9DigitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=040103020",
            String.class);
            assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
            assertEquals(badRequestHucLength, rtn.getBody());
    }

    @Test
    public void hydrologicUnitsMatch04Len10DigitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=0401030201",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_040103020109.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicUnitsMatch04Len11DigitsTest() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=04010302010",
            String.class);
            assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
            assertEquals(badRequestHucLength, rtn.getBody());
    }

    @Test
    public void hydrologicUnits06Len12Test() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=060103020107",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_060103020107.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicUnits08Len12Test() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=080103020107",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_080103020107.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicUnits10Len12Test() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=100103020107",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_100103020107.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicUnits12Len12Test() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=120103020107",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_120103020107.json";
        doJsonCompare(rtn, compareFile);
    }

    @Test
    public void hydrologicUnitsMultiValuesLen10Len12Test() throws IOException {
        ResponseEntity<String> rtn = restTemplate.getForEntity("/collections/monitoring-locations/items?hydrologicUnit=100103020107&hydrologicUnit=120103020107",
            String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        String compareFile = "featuresFilter/monitoring-locations/hydrological_unit_multi_values_len10_len12.json";
        doJsonCompare(rtn, compareFile);
    }
}
