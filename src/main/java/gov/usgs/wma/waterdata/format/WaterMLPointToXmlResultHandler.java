package gov.usgs.wma.waterdata.format;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import gov.usgs.wma.waterdata.domain.ObsAndMeasure;
import gov.usgs.wma.waterdata.domain.SensorWebEnablement2;
import gov.usgs.wma.waterdata.domain.WaterML2;
import gov.usgs.wma.waterdata.domain.WaterMLPoint;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class WaterMLPointToXmlResultHandler implements ResultHandler<WaterMLPoint> {
    // xml namespace URIs
    public static final String XLINK_NS = "http://www.w3.org/1999/xlink";
    public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String GEN_SYS = "Water Data for the Nation - Observations Services";

    // Not using mapper yet
    // private final XmlMapper mapper = XmlFactory.getXmlMapper();
    private final XMLStreamWriter writer;

    // current monitoring location and parameter code being serialized
    private String monLocIdentifier = "";
    private String pcode = "";

    // elements in xml still pending
    private Stack<String> openElements = new Stack<>();

    public WaterMLPointToXmlResultHandler(Writer writer) throws XMLStreamException {
        this.writer = XmlFactory.newXMLStreamWriter(writer);
        initDoc();
    }

    public WaterMLPointToXmlResultHandler(OutputStream out) throws XMLStreamException {
        this.writer = XmlFactory.newXMLStreamWriter(out);
        initDoc();
    }

    @Override
    public void handleResult(final ResultContext<? extends WaterMLPoint> resultContext) {
        WaterMLPoint point = resultContext.getResultObject();
        boolean s = false;
        try {
            if (onNewObservation(point)) {
                startObservationMember(point);
            }
            addPoint(point);
        } catch (Exception e) {
            throw new RuntimeException("Exception during serialization", e);
        }
    }

    public void closeXmlDoc() throws XMLStreamException {
        //Close the doc
        writer.writeEndDocument();
    }

    private void initDoc() throws XMLStreamException {
        //Write the start of the doc
        writer.writeStartDocument("utf-8", "1.0");
        startElement(WaterML2.PREFIX, "Collection", WaterML2.NAMESPACE);

        //Namespace/prefix binding is complicated/weird and often just implemented wrong.
        //Discussion here:  https://veithen.io/2009/11/01/understanding-stax.html

        //set prefixes on root
        writer.setPrefix(WaterML2.PREFIX, WaterML2.NAMESPACE);
        writer.setPrefix(ObsAndMeasure.PREFIX, ObsAndMeasure.NAMESPACE);
        writer.setPrefix(SensorWebEnablement2.PREFIX, SensorWebEnablement2.NAMESPACE);
        // writer.setDefaultNamespace(WaterML2.NAMESPACE);   //This method doesn't seem to do anything

        writer.writeNamespace(WaterML2.PREFIX, WaterML2.NAMESPACE);
        writer.writeNamespace(ObsAndMeasure.PREFIX, ObsAndMeasure.NAMESPACE);
        writer.writeNamespace(SensorWebEnablement2.PREFIX, SensorWebEnablement2.NAMESPACE);
        writer.writeNamespace("xsi", XSI_NS);
        writer.writeNamespace("xlink", XLINK_NS);

        //This next line sets a namespace default for the document.  If set, any un-prefixed
        //element will be in this NS to for any NS aware parser, and, all WML2 elements will
        //be unprefixed.  If you take this line out, all wml2 elements will get a 'wml2' prefix.
        //Either way it means the same thing, but using a default will make the doc smaller.
        writer.writeDefaultNamespace(WaterML2.NAMESPACE);
        addHeaderMetData();
    }

    private void addHeaderMetData() throws XMLStreamException {
        startElement("DocumentMetadata");
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        addElement("generationDate", formatter.format(now));
        startElement("version");
        writer.writeAttribute(XLINK_NS, "href", "http://www.opengis.net/waterml/2.0");
        writer.writeAttribute(XLINK_NS, "title",
                "WaterML 2.0 RFC");
        endElement("version");
        addElement("generationSystem", GEN_SYS);
        endElement("DocumentMetadata");
    }

    private void startObservationMember(WaterMLPoint point) throws XMLStreamException {
        writer.flush();
        endElement("observationMember"); // close any previous started observationMember
        startElement("observationMember");
        startElement(ObsAndMeasure.NAMESPACE, "OM_Observation");
        startElement(ObsAndMeasure.NAMESPACE, "procedure");
        startElement(ObsAndMeasure.NAMESPACE, "ObservationProcess");
        startElement(ObsAndMeasure.NAMESPACE, "parameter");
        writer.writeAttribute(XLINK_NS, "title", "Statistic");
        writer.writeAttribute(XLINK_NS, "href",
                "http://waterdata.usgs.gov/nwisweb/rdf?statCd=00011\"");
        endElement("parameter");
        startElement(ObsAndMeasure.NAMESPACE, "NamedValue");
        startElement(ObsAndMeasure.NAMESPACE, "name");
        writer.writeAttribute(XLINK_NS, "title",
                "Daily random instantaneous values");
        endElement("name");
        startElement(ObsAndMeasure.NAMESPACE, "value");
        writer.writeAttribute(XLINK_NS, "href",
                "http://waterdata.usgs.gov/nwisweb/rdf?statCd=00011");
        endElement("value");
        endElement("procedure");
        addObservedProperty(point);
        startElement(ObsAndMeasure.NAMESPACE, "featureOfInterest");
        // TODO: link with the monitoring_location table, print the actual title
        writer.writeAttribute(XLINK_NS, "title", "Monitoring Location Identifier: "
                + point.getMonLocIdentifier());
        endElement("featureOfInterest");
        startResult(point);
    }

    private void addObservedProperty(WaterMLPoint point) throws XMLStreamException {
        if (point.getPcodeDesc() == null) {
            writer.writeEmptyElement(ObsAndMeasure.NAMESPACE, "observedProperty");
        } else {
            startElement(ObsAndMeasure.NAMESPACE, "observedProperty");
            String title = point.getPcodeDesc() == null ? "Parameter code " + point.getPcode() : point.getPcodeDesc();
            writer.writeAttribute(XLINK_NS, "title", title);
            endElement("observedProperty");
        }
    }

    private void startResult(WaterMLPoint point) throws XMLStreamException {
        startElement(ObsAndMeasure.NAMESPACE, "result");
        startElement(ObsAndMeasure.NAMESPACE, "defaultPointMetadata");
        startElement(ObsAndMeasure.NAMESPACE, "DefaultTVPMeasurementMetadata");
        if (point.getResultUnit() == null) {
            writer.writeEmptyElement("uom");
        } else {
            startElement(ObsAndMeasure.NAMESPACE, "uom");
            writer.writeAttribute(XLINK_NS, "title", point.getResultUnit());
            endElement("uom");
        }
        startElement("interpolationType");
        writer.writeAttribute("href",
                "www.opengis.net/def/waterml/2.0/interpolationType/Continuous");
        writer.writeAttribute("title", "Continuous");
        endElement("interpolationType");
        endElement("defaultPointMetadata");
    }

    private void addPoint(WaterMLPoint point) throws XMLStreamException {
        startElement("point");
        startElement("MeasurementTVP");
        addElement("time", formatResultTime(point));
        String resultStr = point.getResultValue() == null ? null : point.getResultValue().toString();
        addElement("value", resultStr);
        addPointMetadata(point);
        endElement("MeasurementTVP");
        endElement("point");
        writer.flush();
    }

    private void addPointMetadata(WaterMLPoint point) throws XMLStreamException {
        startElement("metadata");
        startElement("TVPMeasurementMetadata");
        addPointQualifiers(point);
        addElement("status", point.getStatus());
        endElement("TVPMeasurementMetadata");
        endElement("metadata");
    }

    private void addPointQualifiers(WaterMLPoint point) throws XMLStreamException {
        List<String> qualifiers = getQualifierList(point);
        if (qualifiers.isEmpty()) {
            writer.writeEmptyElement("qualifier");
        } else {
            for(String qualifier : qualifiers) {
               addElement("qualifier", qualifier);
            }
        }
    }

    private String formatResultTime(WaterMLPoint point) {
        LocalDateTime resultTime = point.getResultDateTimeUTC();
        String resultTimeStr = null;
        if(resultTime != null) {
             DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
             resultTimeStr = formatter.format(resultTime) + "-00:00"; // add timezone off set from UTC
        }
        return resultTimeStr;
    }

    private boolean onNewObservation(WaterMLPoint point) {
        boolean onNew = (!point.getMonLocIdentifier().equals(this.monLocIdentifier))
                || (!point.getPcode().equals(this.pcode));
        if (onNew) {
            monLocIdentifier = point.getMonLocIdentifier();
            pcode = point.getPcode();
        }
        return onNew;
    }

    private void startElement(String prefix, String localName, String namespaceURI)
            throws XMLStreamException {
        writer.writeStartElement(prefix, localName, namespaceURI);
        openElements.push(localName);
    }

    private void startElement(String namespaceURI, String localName)
            throws XMLStreamException {
        writer.writeStartElement(namespaceURI, localName);
        openElements.push(localName);
    }

    private void startElement(String localName) throws XMLStreamException {
        writer.writeStartElement(localName);
        openElements.push(localName);
    }

    private void addElement(String name, String value) throws XMLStreamException {
        if (value == null) {
            writer.writeEmptyElement(name);
        } else {
            writer.writeStartElement(name);
            writer.writeCharacters(value);
            writer.writeEndElement();
        }
    }

    // closes the specified element, first closing any outstanding inner elements.
    private void endElement(String name) throws XMLStreamException {
        while (openElements.contains(name)) {
            openElements.pop();
            writer.writeEndElement();
        }
    }

    private List<String> getQualifierList(WaterMLPoint point) {
          List<String> qualifierList = new ArrayList<>();
          if(point.getQualifiersAsJson() != null) {
              // convert to csv separated
              String csv =  point.getQualifiersAsJson().replace("[", "").
                      replace("]","").replace("\"", "");
              qualifierList =  Arrays.asList(csv.split(",", 10));
          }

          return qualifierList;
    }

}