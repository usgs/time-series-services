package gov.usgs.wma.waterdata.timeseries;

import static gov.usgs.wma.waterdata.collections.CollectionParams.DEFAULT_COLLECTION_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.waterdata.ConfigurationService;
import gov.usgs.wma.waterdata.collections.CollectionParams;
import gov.usgs.wma.waterdata.springinit.BaseIT;
import gov.usgs.wma.waterdata.springinit.DBTestConfig;

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = {DBTestConfig.class, CollectionParams.class, TimeSeriesDao.class,
					ConfigurationService.class})
@DatabaseSetup("classpath:/testData/timeSeries/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue/")
public class StatTimeSeriesDaoIT extends BaseIT {
	@Autowired
	private TimeSeriesDao timeSeriesDao;

	@Test
	public void featureTimeSeriesCollectionTest() throws Exception {
		String actualJSON  = timeSeriesDao.getStatisticalTimeSeries(DEFAULT_COLLECTION_ID, "USGS-07227448");
		String expectJSON = getCompareFile("timeSeries/monitoring-locations/usgs-07227448-time-series-list.json");
		assertThat(new JSONObject(actualJSON), sameJSONObjectAs(new JSONObject(expectJSON)));
	}

	@Test
	public void featureTimeSeriesCollectionEmptyTest() throws Exception {
		String actualJSON  = timeSeriesDao.getStatisticalTimeSeries(DEFAULT_COLLECTION_ID, "USGS-04027940");
		String expectJSON = getCompareFile("timeSeries/monitoring-locations/usgs-04027940-time-series-list.json");
		assertThat(new JSONObject(actualJSON), sameJSONObjectAs(new JSONObject(expectJSON)));
	}
	
	@Test
	public void featureTimeSeriesSatCollectionTest() throws Exception {
		String actualJSON  = timeSeriesDao.getStatisticalTimeSeries("SAT", "USGS-07227448");
		String expectJSON = getCompareFile("timeSeries/SAT/usgs-07227448-time-series-list.json");
		assertThat(new JSONObject(actualJSON), sameJSONObjectAs(new JSONObject(expectJSON)));
	}

	@Test
	public void featureTimeSeriesSatCollectionEmptyTest() throws Exception {
		String actualJSON  = timeSeriesDao.getStatisticalTimeSeries("SAT", "USGS-04027940");
		String expectJSON = getCompareFile("timeSeries/SAT/usgs-04027940-time-series-list.json");
		assertThat(new JSONObject(actualJSON), sameJSONObjectAs(new JSONObject(expectJSON)));
	}


	@Test
	public void collectionMissingTest() throws Exception {
		String actualJSON  = timeSeriesDao.getStatisticalTimeSeries("SOME-COLLECTION", "USGS-07227448");
		assertNull(actualJSON);
	}

	@Test
	public void featureNotFoundTest() {
		String actualJSON  = timeSeriesDao.getStatisticalTimeSeries(DEFAULT_COLLECTION_ID, "SOME-FEATURE");
		assertNull(actualJSON);
	}

	@Test
	public void notFoundNoGeomTest() throws Exception {
		String actualJSON  = timeSeriesDao.getStatisticalTimeSeries(DEFAULT_COLLECTION_ID, "USGS-04028090");
		assertNull(actualJSON);
	}

}
