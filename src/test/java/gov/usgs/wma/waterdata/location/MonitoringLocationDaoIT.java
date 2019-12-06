package gov.usgs.wma.waterdata.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.waterdata.location.geojson.GeoJSON;
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
		GeoJSON geoJSON = monitoringLocationDao.getLocation("07227448");
		assertNotNull(geoJSON);
		assertEquals("Feature", geoJSON.getType());
		assertEquals("USGS-07227448", geoJSON.getId());
		assertEquals("Point", geoJSON.getGeometry().getType());
		assertEquals(2, geoJSON.getGeometry().getCoordinates().length);
		assertEquals("-102.4804790", geoJSON.getGeometry().getCoordinates()[0].toString());
		assertEquals("35.6675430", geoJSON.getGeometry().getCoordinates()[1].toString());
		assertEquals("Punta De Agua Ck ñr Channing, TX", geoJSON.getProperties().getSamplingFeatureName());
	}

	@Test
	public void notFoundTest() {
		GeoJSON geoJSON = monitoringLocationDao.getLocation("12345678");
		assertNull(geoJSON);
	}

	@Test
	public void noGeomTest() {
		GeoJSON geoJSON = monitoringLocationDao.getLocation("04028090");
		assertNull(geoJSON);
	}
}
