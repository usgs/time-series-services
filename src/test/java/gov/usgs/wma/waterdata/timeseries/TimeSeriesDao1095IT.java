package gov.usgs.wma.waterdata.timeseries;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import gov.usgs.wma.waterdata.ConfigurationService;
import gov.usgs.wma.waterdata.collections.CollectionParams;
import gov.usgs.wma.waterdata.springinit.BaseIT;
import gov.usgs.wma.waterdata.springinit.DBTestConfig;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

/**
 * The '1095' tests specifically tests that timeseries data older than 1095 days is not displayed if not approved.
 * Adding unapproved test data that is older than 1095 doesn't affect other tests (if we correctly filter the data out),
 * however, adding data that is exactly 1095 days old to test the cutoff does affect other tests, so it is done in
 * separate test classes with '1095' in the name.
 */
@SpringBootTest(webEnvironment=WebEnvironment.NONE,
	classes={DBTestConfig.class, TimeSeriesDao.class, CollectionParams.class, ConfigurationService.class})
@DatabaseSetup("classpath:/testData/monitoringLocation/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue1095/")
public class TimeSeriesDao1095IT extends BaseIT {

	@Autowired
	private TimeSeriesDao timeSeriesDao;

	@DatabaseSetup("classpath:/testData/monitoringLocation/")
	@DatabaseSetup("classpath:/testData/groundwaterDailyValue1095/")
	@Test
	public void foundTest_UnapprovedData1095DaysOldIsOK1096IsNot() {
		try {
			assertEquals(getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac_1095.txt"), timeSeriesDao.getTimeSeries("USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac"));
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@DatabaseSetup("classpath:/testData/monitoringLocation/")
	@DatabaseSetup("classpath:/testData/groundwaterDailyValue1095/")
	@Test
	public void foundTimeSeriesTest_UnapprovedData1095DaysOldIsOK1096IsNot() throws Exception {
		String geoJSON = timeSeriesDao.getTimeSeries("monitoring-locations", "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac");
		assertNotNull(geoJSON);

		String expectedJSON = getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac_1095.json");
		assertThat(new JSONObject(geoJSON), sameJSONObjectAs(new JSONObject(expectedJSON)));
	}

}
