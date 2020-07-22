package gov.usgs.wma.waterdata.timeseries;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import gov.usgs.wma.waterdata.collections.BaseCollectionsIT;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/monitoringLocation/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue1095/")
public class StatTimeSeriesController1095IT extends BaseCollectionsIT {

	@Test
	public void foundTimeSeriesTest() throws Exception {
		String url = "/collections/monitoring-locations/items/USGS-07227448/observations/statistical-time-series/e6a4cc2de5bf437e83efe0107cf026ac";
		String rtn = doCollectionRequest(url, HttpStatus.OK);
		String expectResponseJSON = getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac_1095.json");
		assertThat(new JSONObject(rtn), sameJSONObjectAs(new JSONObject(expectResponseJSON)));
	}
}
