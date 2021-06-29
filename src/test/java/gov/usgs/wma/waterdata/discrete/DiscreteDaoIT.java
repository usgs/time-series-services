package gov.usgs.wma.waterdata.discrete;

import static org.junit.jupiter.api.Assertions.fail;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import gov.usgs.wma.waterdata.ConfigurationService;
import gov.usgs.wma.waterdata.collections.CollectionParams;
import gov.usgs.wma.waterdata.discrete.DiscreteDao;
import gov.usgs.wma.waterdata.domain.WaterMLPoint;
import gov.usgs.wma.waterdata.format.WaterMLPointToXmlResultHandler;
import gov.usgs.wma.waterdata.springinit.BaseIT;
import gov.usgs.wma.waterdata.springinit.DBTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.xmlunit.matchers.CompareMatcher;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

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
		assertEquals(15, points.size());
		for(WaterMLPoint point : points) {
			assertNotNull(point.getResultDateTimeUTC());
			assertNotNull(point.getResultValue());
			assertTrue(point.getResultUnit() != null || point.getPcodeDesc() == null);
			assertNotNull(point.getPcode());
			assertTrue(point.getQualifiersAsJson() == null ||
					    point.getQualifiersAsJson().equals(("[\"Static\"]")));
		}
	}

	@Test
	public void getDiscretePointTest() throws XMLStreamException {
		StringWriter out = new StringWriter();
		WaterMLPointToXmlResultHandler resultHandler = new WaterMLPointToXmlResultHandler(out);
		discreteDao.getDiscreteGWMLPoint("USGS-175848066350900", resultHandler);
		assertEquals(15, resultHandler.getNumResults());
		resultHandler.closeXmlDoc();
		try {
			String expectedXml = harmonizeXml(getCompareFile("xml/discrete/USGS-175848066350900.xml"));
			expectedXml = expectedXml.replaceAll("<generationDate>.*</generationDate>", "");

			String actualXml = out.toString();
			actualXml = actualXml.replaceAll("<generationDate>.*</generationDate>", "");
			assertThat(actualXml, CompareMatcher.isIdenticalTo(expectedXml));
		} catch (IOException e) {
			fail("Unexpected IOException during test", e);
		}
	}

	@Test
	public void notFoundTest() {
		List<WaterMLPoint> points = discreteDao.getDiscreteGWMLPoint("USGS-12345678");
		assertNotNull(points);
		assertTrue(points.isEmpty());
	}
}
