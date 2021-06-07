package gov.usgs.wma.waterdata.format;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.SerializationFeature;
import gov.usgs.wma.waterdata.domain.WaterMLPoint;
import org.apache.ibatis.session.ResultContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.stream.*;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@ExtendWith(MockitoExtension.class)
class WaterMLPointToXmlResultHandlerTest {

	@Mock
	ResultContext<WaterMLPoint> resultContext;

	WaterMLPoint point;

	@BeforeEach
	public void setup() {
		point = new WaterMLPoint();
		point.setPcode("1234");
		point.setQualifiers("Good");
		point.setResultValue(11.11);
		point.setResultUnit("ft");
		point.setResultDateTimeUTC(LocalDateTime.of(2021, 6, 4, 14, 30));
		point.myOM = "blah";	//Proof of concept:  Yes we can right a field w/ a different NS
	}

	@Test
	public void happyPath() throws XMLStreamException, IOException {

		when(resultContext.getResultObject()).thenReturn(point);

		//Configure an XMLInputFactory and XMLStreamWriter
		XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
		xmlOutputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", "true");
		StringWriter out = new StringWriter();
		XMLStreamWriter sw = xmlOutputFactory.createXMLStreamWriter(out);

		// then Jackson components
		XmlMapper mapper = new XmlMapper(xmlInputFactory);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.findAndRegisterModules();
		mapper.setAnnotationIntrospector(new InheritNamespaceAnnotationInspector());

		WaterMLPointToXmlResultHandler formatter = new WaterMLPointToXmlResultHandler(mapper, sw);

		//register namespaces
		sw.setPrefix("wml2", "http://www.opengis.net/waterml/2.0");
		sw.setPrefix("om", "http://www.opengis.net/om/2.0");

		//Write the start of the doc
		sw.writeStartDocument("utf-8", "1.0");
		sw.writeStartElement("wml2", "Collection", "http://www.opengis.net/waterml/2.0");
		sw.writeNamespace("wml2", "http://www.opengis.net/waterml/2.0");
		sw.writeNamespace("om", "http://www.opengis.net/om/2.0");
		sw.writeDefaultNamespace("http://www.opengis.net/waterml/2.0");
		sw.setDefaultNamespace("http://www.opengis.net/waterml/2.0");

		// Write whatever content POJOs...
		formatter.handleResult(resultContext);
		formatter.handleResult(resultContext);

		//Close the doc
		sw.writeEndElement();
		sw.writeEndDocument();

		System.out.println("XML:");
		System.out.println(out.toString());
	}

}
