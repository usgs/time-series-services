package gov.usgs.wma.waterdata.timeseries;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.waterdata.springinit.BaseIT;
import gov.usgs.wma.waterdata.springinit.DBTestConfig;

@SpringBootTest(webEnvironment=WebEnvironment.NONE,
	classes={DBTestConfig.class, TimeSeriesDao.class})
@DatabaseSetup("classpath:/testData/monitoringLocation/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue/")
public class TimeSeriesDaoIT extends BaseIT {

	@Autowired
	private TimeSeriesDao timeSeriesDao;

	@Test
	public void foundTest() {
		try {
			assertEquals(getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac.txt"), timeSeriesDao.getTimeSeries("USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac"));
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void notFoundTest() {
		String geoJSON = timeSeriesDao.getTimeSeries("USGS-12345678", "216d009de8914147a0f9e5237da77854");
		assertNull(geoJSON);
	}

	@Test
	public void noGeomTest() {
		String geoJSON = timeSeriesDao.getTimeSeries("USGS-04028090", "41a5ff887b744b84a271b65e48d78074");
		assertNull(geoJSON);
	}
	
	
	@Test
	public void foundTimeSeriesTest() throws Exception {
		String geoJSON = timeSeriesDao.getTimeSeries("monitoring-locations", "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac");
		assertNotNull(geoJSON);
		
		String expectedJSON = getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac.json");
		assertThat(new JSONObject(geoJSON), sameJSONObjectAs(new JSONObject(expectedJSON)));
	}
	@Test
	public void timeSeriesNotInCollectionTest() throws Exception {
		String geoJSON = timeSeriesDao.getTimeSeries("SOME-COLLECTION", "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac");
		assertNull(geoJSON);
	}
	@Test
	public void timeSeriesNotInFeatureTest() throws Exception {
		String geoJSON = timeSeriesDao.getTimeSeries("monitoring-locations", "OTHER-07227448", "e6a4cc2de5bf437e83efe0107cf026ac");
		assertNull(geoJSON);
	}

	@Test
	public void notFoundTimeSeriesTest() {
		String geoJSON = timeSeriesDao.getTimeSeries("monitoring-locations", "USGS-12345678", "216d009de8914147a0f9e5237da77854");
		assertNull(geoJSON);
	}

	@Test
	public void noGeomTimeSeriesTest() {
		String geoJSON = timeSeriesDao.getTimeSeries("monitoring-locations", "USGS-04028090", "41a5ff887b744b84a271b65e48d78074");
		assertNull(geoJSON);
	}
	
	
}
