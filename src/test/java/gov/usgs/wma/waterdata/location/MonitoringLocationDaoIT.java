package gov.usgs.wma.waterdata.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.waterdata.springinit.BaseIT;
import gov.usgs.wma.waterdata.springinit.DBTestConfig;

@SpringBootTest(webEnvironment=WebEnvironment.NONE,
	classes={DBTestConfig.class, MonitoringLocationDao.class})
@DatabaseSetup("classpath:/testData/monitoringLocation/")
public class MonitoringLocationDaoIT extends BaseIT {

	@Autowired
	private MonitoringLocationDao monitoringLocationDao;

	@Test
	public void foundTest() {
		try {
			assertEquals(getCompareFile("USGS-07227448.txt"), monitoringLocationDao.getLocation("USGS-07227448"));
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void notFoundTest() {
		String geoJSON = monitoringLocationDao.getLocation("USGS-12345678");
		assertNull(geoJSON);
	}

	@Test
	public void noGeomTest() {
		String geoJSON = monitoringLocationDao.getLocation("USGS-04028090");
		assertNull(geoJSON);
	}
}
