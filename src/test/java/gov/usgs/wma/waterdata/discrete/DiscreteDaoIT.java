package gov.usgs.wma.waterdata.discrete;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import gov.usgs.wma.waterdata.ConfigurationService;
import gov.usgs.wma.waterdata.collections.CollectionParams;
import gov.usgs.wma.waterdata.discrete.DiscreteDao;
import gov.usgs.wma.waterdata.domain.WaterMLPoint;
import gov.usgs.wma.waterdata.springinit.BaseIT;
import gov.usgs.wma.waterdata.springinit.DBTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment=WebEnvironment.NONE,
	classes={DBTestConfig.class, DiscreteDao.class, CollectionParams.class, ConfigurationService.class})
@DatabaseSetup("classpath:/testData/lookups/")
@DatabaseSetup("classpath:/testData/discreteGroundWaterAqts/")
public class DiscreteDaoIT extends BaseIT {

	@Autowired
	private DiscreteDao discreteDao;

	@Test
	public void foundTest() {
		List<WaterMLPoint> points = discreteDao.getDiscreteGWMLPoint("USGS-175848066350900");
		assertNotNull(points);
		assertEquals(10, points.size());
		for(WaterMLPoint point : points) {
			assertNotNull(point.getResultDateTimeUTC());
			assertNotNull(point.getResultValue());
			assertNotNull(point.getResultUnit());
			assertNotNull(point.getResultAccuracyUnit());
			assertNotNull(point.getVerticalDatum());
			assertNotNull(point.getPcode());
			assertTrue(point.getQualifiers() == null ||
					    point.getQualifiers().equals(("[\"Static\"]")));
		}
	}

	@Test
	public void notFoundTest() {
		List<WaterMLPoint> points = discreteDao.getDiscreteGWMLPoint("USGS-12345678");
		assertNotNull(points);
		assertTrue(points.isEmpty());
	}
}
