package gov.usgs.wma.waterdata.format;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.SerializationFeature;
import gov.usgs.wma.waterdata.domain.*;
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
		point.addQualifier("Good");
		point.addQualifier("OK");
		point.setPcode("1234");
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
		mapper.setAnnotationIntrospector(new InheritNamespaceAnnotationIntrospector());

		WaterMLPointToXmlResultHandler formatter = new WaterMLPointToXmlResultHandler(mapper, sw);

		//Write the start of the doc
		sw.writeStartDocument("utf-8", "1.0");
		sw.writeStartElement(WaterML2.PREFIX, "Collection", WaterML2.NAMESPACE);

		//Namespace/prefix binding is complicated/weird and often just implemented wrong.
		//Discussion here:  https://veithen.io/2009/11/01/understanding-stax.html

		//set prefixes on root
		sw.setPrefix(WaterML2.PREFIX, WaterML2.NAMESPACE);
		sw.setPrefix(ObsAndMeasure.PREFIX, ObsAndMeasure.NAMESPACE);
		sw.setPrefix(SensorWebEnablement2.PREFIX, SensorWebEnablement2.NAMESPACE);
		//sw.setDefaultNamespace(WaterML2.NAMESPACE);	//This method doesn't seem to do anything

		sw.writeNamespace(WaterML2.PREFIX, WaterML2.NAMESPACE);
		sw.writeNamespace(ObsAndMeasure.PREFIX, ObsAndMeasure.NAMESPACE);
		sw.writeNamespace(SensorWebEnablement2.PREFIX, SensorWebEnablement2.NAMESPACE);

		//
		//This next line sets a namespace default for the document.  If set, any un-prefixed
		//element will be in this NS to for any NS aware parser, and, all WML2 elements will
		//be unprefixed.  If you take this line out, all wml2 elements will get a 'wml2' prefix.
		//Either way it means the same thing, but using a default will make the doc smaller.
		sw.writeDefaultNamespace(WaterML2.NAMESPACE);


		sw.writeStartElement(WaterML2.NAMESPACE, "MeasurementTimeseries");


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
