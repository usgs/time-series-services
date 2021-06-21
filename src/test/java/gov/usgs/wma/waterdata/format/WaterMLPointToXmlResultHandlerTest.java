package gov.usgs.wma.waterdata.format;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import gov.usgs.wma.waterdata.domain.*;
import org.apache.ibatis.session.ResultContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.stream.*;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;

import org.mockito.stubbing.Answer;
import org.springframework.core.io.ClassPathResource;
import org.xmlunit.matchers.CompareMatcher;

@ExtendWith(MockitoExtension.class)
class WaterMLPointToXmlResultHandlerTest {

	@Mock
	ResultContext<WaterMLPoint> resultContext;

	WaterMLPoint point;
	ArrayDeque<WaterMLPoint> pointQueue = new ArrayDeque<>();
	PointAnswer pointAnswer;

	@BeforeEach
	public void setup() {
	    populatePointUqeue();
	    pointAnswer = new PointAnswer();
	}

	@Test
	public void happyPath() throws XMLStreamException, IOException {

		when(resultContext.getResultObject()).then(pointAnswer);

		StringWriter out = new StringWriter();
		WaterMLPointToXmlResultHandler formatter = new WaterMLPointToXmlResultHandler(out);

		// Write whatever content POJOs...
		formatter.handleResult(resultContext);
		formatter.handleResult(resultContext);
		formatter.handleResult(resultContext);

		//Close the doc
		formatter.closeXmlDoc();

		String compareFile = "testResult/xml/discrete/resultHandlerTest.xml";
		String expectedXml = new String(new ClassPathResource(compareFile).getInputStream().readAllBytes());
		expectedXml = expectedXml.replaceAll("<generationDate>.*</generationDate>", "");
		// take out pretty printing. TODO: use XmlUnit
		expectedXml = expectedXml.replace("\r", "").replace("\n", "").replace("\t", "").replaceAll("> *<", "><");

		String actual = out.toString().replaceAll("<generationDate>.*</generationDate>", "");

		assertThat(actual, CompareMatcher.isIdenticalTo(expectedXml));
	}

	protected void populatePointUqeue() {
		pointQueue.clear();

	    WaterMLPoint point = createPoint();
	    pointQueue.add(point);
		point = createPoint();
		point.setResultValue(10.0);
		pointQueue.add(point);

		// add new pcode
		point = createPoint();
		point.setPcode("62611");
		point.setPcodeDesc("Elevation, GW, NAVD88");
		point.setResultValue(50.0);
		pointQueue.add(point);
	}

	protected WaterMLPoint createPoint() {
		point = new WaterMLPoint();
		point.setMonLocIdentifier("USGS-12345678");
		point.setQualifiersAsJson("[\"good\",\"Ok\"]");
		point.setStatus("Approved");
		point.setPcode("72019");
		point.setPcodeDesc("Depth to water level, ft below land surface");
		point.setResultValue(11.11);
		point.setResultUnit("ft");
		point.setResultDateTimeUTC(LocalDateTime.of(2021, 6, 4, 14, 30));
		point.myOM = "blah";	//Proof of concept:  Yes we can right a field w/ a different NS

	    return point;
	}

	private class PointAnswer implements Answer<WaterMLPoint> {
			@Override
			public WaterMLPoint answer(InvocationOnMock invocation) {
				WaterMLPoint point = pointQueue.removeFirst();
				return point;
			}
	}

}
