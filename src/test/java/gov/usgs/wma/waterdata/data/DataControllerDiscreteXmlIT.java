package gov.usgs.wma.waterdata.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import gov.usgs.wma.waterdata.domain.WaterML2;
import org.xmlunit.matchers.CompareMatcher;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.waterdata.springinit.BaseIT;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/lookups/")
@DatabaseSetup("classpath:/testData/discreteGroundWaterAqts/")
public class DataControllerDiscreteXmlIT extends BaseIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void foundTest() {
        runCase("USGS-175848066350900","xml/discrete/USGS-175848066350900.xml");
    }

    @Test
    public void mediaTypeNotAcceptableTest() {
        String baseUrl = "/data?featureId=USGS-07227448&domain=groundwater_levels&type=discrete";
        for (String contentType : contentNotAccepted) {
            ResponseEntity<String> rtn = restTemplate.getForEntity(buildUrl(baseUrl, contentType), String.class);
            assertTrue(rtn.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
            assertThat(rtn.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
            assertEquals(ogcNotAcceptedPayload, rtn.getBody());
        }
    }

    @Test
    public void notFoundTest() {
        String url = "/data?featureId=USGS-12345678&domain=groundwater_levels&type=discrete";
        runErrorCase(url, HttpStatus.NOT_FOUND, ogc404Payload);
    }

    public void jsonNotAvailableTest() {
        String url = "/data?featureId=USGS-12345678&domain=groundwater_levels&type=discrete";
        String desc = "Discrete data is only available as WaterML";
        String expectedBody = "{\"code\":\"400\",\"description\":\"" + desc + "\"}";
        runErrorCase(url, HttpStatus.BAD_REQUEST, expectedBody, "json");
        url = url + "&best=false";
        runErrorCase(url, HttpStatus.BAD_REQUEST, expectedBody);
    }

    @Test
    public void featureIdNotProvidedTest() {
        String url = "/data?domain=groundwater_levels&type=discrete";
        String desc = "Required request parameter 'featureId' for method parameter type String is not present";
        runErrorCase(url, HttpStatus.BAD_REQUEST,
                "{\"code\":\"400\",\"description\":\"" + desc + "\"}");
    }

    @Test
    public void badDomainValueTest() {
        String url = "/data?featureId=USGS-07227448&domain=xyz&type=discrete&best=false";
        String desc = "No enum constant gov.usgs.wma.waterdata.parameter.Domain.xyz";
        runErrorCase(url, HttpStatus.BAD_REQUEST,
                "{\"code\":\"400\",\"description\":\"Error in parameter domain:  " + desc + "\"}");
    }

    @Test
    public void badTypeValueTest() {
        String url = "/data?featureId=USGS-07227448&domain=groundwater_levels&type=none&best=true";
        String desc = "No enum constant gov.usgs.wma.waterdata.parameter.DataType.none";
        runErrorCase(url, HttpStatus.BAD_REQUEST,
                "{\"code\":\"400\",\"description\":\"Error in parameter type:  " + desc + "\"}");
    }

    private void runErrorCase(String url, HttpStatus expectedStatus, String expectedBody) {
       runErrorCase(url, expectedStatus, expectedBody, "waterml");
    }

    private void runErrorCase(String url, HttpStatus expectedStatus, String expectedBody, String contentType) {
        ResponseEntity<String> rtn = restTemplate.getForEntity(buildUrl(url, contentType), String.class);
        assertThat(rtn.getStatusCode(), equalTo(expectedStatus));
        assertTrue(rtn.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
        assertEquals(expectedBody, rtn.getBody());
    }

    private void runCase(String featureId, String compareFile) {
        String urlFormat = "/data?featureId=%s&domain=groundwater_levels&type=discrete&f=waterML";
        String url = String.format(urlFormat, featureId);

        ResponseEntity<String> rtn = restTemplate.getForEntity(url, String.class);
        assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
        assertTrue(rtn.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_XML));
        assertNotNull(rtn.getBody());
        assertXmlSchemaCompliant(rtn.getBody(), WaterML2.SCHEMA);

        try {
            String expectedXml = harmonizeXml(getCompareFile(compareFile));
            expectedXml = expectedXml.replaceAll("<generationDate>.*</generationDate>", "");

            String actualXml = rtn.getBody();
            actualXml = actualXml.replaceAll("<generationDate>.*</generationDate>", "");
            assertThat(actualXml, CompareMatcher.isIdenticalTo(expectedXml));
        } catch (IOException e) {
            fail("Unexpected IOException during test", e);
        }
    }

    private String buildUrl(String baseUrl, String contentType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        if (contentType != null) {
            builder = builder.queryParam("f", contentType);
        }
        String url = builder.build().toUri().toString();
        return url;
    }
}
