package gov.usgs.wma.waterdata.collections;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import static gov.usgs.wma.waterdata.collections.CollectionParams.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/monitoringLocation/")
@DatabaseSetup("classpath:/testData/groundwaterDailyValue1095/")
public class ObservationsStatTimeSeriesController1095IT extends BaseCollectionsIT {

	@Test
	public void foundTimeSeriesTest() throws Exception {
		String url = ObservationsStatTimeSeriesControllerIT.makeURL(DEFAULT_COLLECTION_ID, "USGS-07227448", "e6a4cc2de5bf437e83efe0107cf026ac");
		String rtn = doCollectionRequest(url, HttpStatus.OK);
		String expectResponseJSON = getCompareFile("e6a4cc2de5bf437e83efe0107cf026ac_1095.json");
		assertThat(new JSONObject(rtn), sameJSONObjectAs(new JSONObject(expectResponseJSON)));
	}

}