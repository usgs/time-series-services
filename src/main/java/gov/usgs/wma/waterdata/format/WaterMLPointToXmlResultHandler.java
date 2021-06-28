package gov.usgs.wma.waterdata.format;

import gov.usgs.wma.waterdata.domain.GeoMarkupLanguage;
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
import java.util.Objects;
import java.util.Stack;

public class WaterMLPointToXmlResultHandler implements ResultHandler<WaterMLPoint> {
    // xml namespace URIs
    public static final String XLINK_NS = "http://www.w3.org/1999/xlink";
    public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";
    public static final String SWE_NS = "http://www.opengis.net/swe/2.0";
    public static final String GEN_SYS = "Water Data for the Nation - Observations Services";

    // Not using mapper yet
    // private final XmlMapper mapper = XmlFactory.getXmlMapper();
    private final XMLStreamWriter writer;

    // current monitoring location and parameter code being serialized
    private String monLocIdentifier = "";
    private String pcode = "";
    private String statisticDesc = null;
    private String measurementTimeseriesId = null;

    // number of results processed
    private int numResults = 0;

    // elements in xml still pending
    private final Stack<String> openElements = new Stack<>();

    public WaterMLPointToXmlResultHandler(Writer writer) throws XMLStreamException {
        this.writer = XmlFactory.newXMLStreamWriter(writer);
    }

    public WaterMLPointToXmlResultHandler(OutputStream out) throws XMLStreamException {
        this.writer = XmlFactory.newXMLStreamWriter(out);
    }

    @Override
    public void handleResult(final ResultContext<? extends WaterMLPoint> resultContext) {
        WaterMLPoint point = resultContext.getResultObject();
        try {
            if(numResults == 0) {
                initDoc();
            }
            numResults++;
            if (onNewObservation(point)) {
                startObservationMember(point);
            }
            addPoint(point);
        } catch (XMLStreamException e) {
            throw new RuntimeException("Exception during serialization to xml", e);
        }
    }

    public int getNumResults() {
        return numResults;
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
        writer.setPrefix(GeoMarkupLanguage.PREFIX, GeoMarkupLanguage.NAMESPACE);
        // writer.setDefaultNamespace(WaterML2.NAMESPACE);   //This method doesn't seem to do anything

        writer.writeNamespace(WaterML2.PREFIX, WaterML2.NAMESPACE);
        writer.writeNamespace(ObsAndMeasure.PREFIX, ObsAndMeasure.NAMESPACE);
        writer.writeNamespace(SensorWebEnablement2.PREFIX, SensorWebEnablement2.NAMESPACE);
        writer.writeNamespace(GeoMarkupLanguage.PREFIX, GeoMarkupLanguage.NAMESPACE);
        writer.writeNamespace("xsi", XSI_NS);
        writer.writeNamespace("xsd", XSD_NS);
        writer.writeNamespace("swe", SWE_NS);
        writer.writeNamespace("xlink", XLINK_NS);

        //This next line sets a namespace default for the document.  If set, any un-prefixed
        //element will be in this NS to for any NS aware parser, and, all WML2 elements will
        //be unprefixed.  If you take this line out, all wml2 elements will get a 'wml2' prefix.
        //Either way it means the same thing, but using a default will make the doc smaller.
        writer.writeDefaultNamespace(WaterML2.NAMESPACE);
        writeAttribute(XSI_NS, "schemaLocation", WaterML2.NAMESPACE + " " + WaterML2.SCHEMA);
        addHeaderMetData();
    }

    private void addHeaderMetData() throws XMLStreamException {
        startElement("metadata");
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
        endElement("metadata");
    }

    private void startObservationMember(WaterMLPoint point) throws XMLStreamException {
        writer.flush();
        endElement("observationMember"); // close any previous started observationMember
        startElement("observationMember");
        startElement(ObsAndMeasure.NAMESPACE, "OM_Observation");
        addObservationTimes(point);
        startElement(ObsAndMeasure.NAMESPACE, "procedure");
        startElement(ObsAndMeasure.NAMESPACE, "ObservationProcess");
        startElement("parameter");
        startElement(ObsAndMeasure.NAMESPACE, "NamedValue");
        startElement(ObsAndMeasure.NAMESPACE, "name");
        writeAttribute(XLINK_NS, "title", "statistic");
        writeAttribute(XLINK_NS, "href", point.getStatisticReference());
        endElement("name");
        startElement(ObsAndMeasure.NAMESPACE, "value");
        writeAttribute(XSI_NS, "type", "xsd:string");
        writer.writeCharacters(point.getStatisticDesc());
        endElement("value");
        endElement("procedure");
        addObservedProperty(point);
        startElement(ObsAndMeasure.NAMESPACE, "featureOfInterest");
        writeAttribute(XLINK_NS, "href", point.getMonLocReference());
        writeAttribute(XLINK_NS, "title", point.getSiteName());
        endElement("featureOfInterest");
        startResult(point);
    }

    private void addObservationTimes(WaterMLPoint point) throws XMLStreamException {
        if(point.getPhenomenonTimeStart() != null || point.getPhenomenonTimeEnd() != null) {
            startElement(ObsAndMeasure.NAMESPACE, "phenomenonTime");
            startElement(GeoMarkupLanguage.NAMESPACE, "TimePeriod");
            addElement(GeoMarkupLanguage.NAMESPACE, "beginPosition", point.getPhenomenonTimeStart());
            addElement(GeoMarkupLanguage.NAMESPACE, "endPosition", point.getPhenomenonTimeEnd());
            endElement("TimePeriod");
            endElement("phenomenonTime");
        }
        if(point.getPhenomenonTimeEnd() != null) {
            startElement(ObsAndMeasure.NAMESPACE, "resultTime");
            startElement(GeoMarkupLanguage.NAMESPACE, "TimeInstant");
            addElement(GeoMarkupLanguage.NAMESPACE, "timePosition", point.getPhenomenonTimeEnd());
            endElement("TimeInstant");
            endElement("resultTime");
        }
    }

    private void addObservedProperty(WaterMLPoint point) throws XMLStreamException {
        if (point.getPcodeDesc() == null) {
            writer.writeEmptyElement(ObsAndMeasure.NAMESPACE, "observedProperty");
        } else {
            startElement(ObsAndMeasure.NAMESPACE, "observedProperty");
            writeAttribute(XLINK_NS, "href", point.getPcodeReference());
            String title = point.getPcodeDesc() == null ? "Parameter code " + point.getPcode() : point.getPcodeDesc();
            writeAttribute(XLINK_NS, "title", title);
            endElement("observedProperty");
        }
    }

    private void startResult(WaterMLPoint point) throws XMLStreamException {
        startElement(ObsAndMeasure.NAMESPACE, "result");
        startElement("MeasurementTimeseries");
        writeAttribute(GeoMarkupLanguage.NAMESPACE,"id", formatMeasureTimeSeriesId(point));
        startElement("defaultPointMetadata");
        startElement("DefaultTVPMeasurementMetadata");
        if (point.getResultUnit() == null) {
            writer.writeEmptyElement("uom");
        } else {
            startElement("uom");
            writeAttribute(XLINK_NS, "title", point.getResultUnit());
            writeAttribute(XLINK_NS, "href", point.getResultUnitReference());
            endElement("uom");
        }
        addInterpolationType(point);
        endElement("interpolationType");
        endElement("defaultPointMetadata");
    }

    // the gml:id can not start with a number or contain space or ":" etc, else schema validation
    // fails
    private String formatMeasureTimeSeriesId(WaterMLPoint point) {
        String id = null;
        if(point.getMeasurementTimeseriesId() != null) {
            // Only have ids for time series data now.
            // Todo: check for other data types (discrete,...) as they are implemented
            id = "aqts-tsid-" + point.getMeasurementTimeseriesId();
        }
        return id;
    }

    private void addInterpolationType(WaterMLPoint point) throws XMLStreamException {
        if(point.getInterpolationTypeRef() == null &&
           point.getInterpolationTypeDesc() == null) {
            writer.writeEmptyElement("interpolationType");
        } else {
            startElement("interpolationType");
            if(point.getInterpolationTypeRef() != null) {
                writer.writeAttribute("href", point.getInterpolationTypeRef());
            }
            if(point.getInterpolationTypeDesc() != null) {
                writer.writeAttribute("title", point.getInterpolationTypeDesc());
            }
            endElement("interpolationType");
        }
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
    }

    private void addPointMetadata(WaterMLPoint point) throws XMLStreamException {
        startElement("metadata");
        startElement("TVPMeasurementMetadata");
        addPointQualifiers(point);
        endElement("TVPMeasurementMetadata");
        endElement("metadata");
    }

    private void addPointQualifiers(WaterMLPoint point) throws XMLStreamException {
       addQualifiers(getQualifierList(point));
       // statuses are add to WaterMl as a qualifier element
        addQualifiers(getStatusList(point));
    }

    private void addQualifiers(List<String> qualifiers) throws XMLStreamException {
        if (qualifiers.isEmpty()) {
            writer.writeEmptyElement("qualifier");
        } else {
            for(String qualifier : qualifiers) {
                startElement("qualifier");
                startElement(SWE_NS, "Category");
                addElement(SWE_NS, "value", qualifier);
                endElement("Category");
                endElement("qualifier");
            }
        }
    }

    private String formatResultTime(WaterMLPoint point) {
        LocalDateTime resultTime = point.getResultDateTimeUTC();
        String resultTimeStr = null;
        if(resultTime != null) {
             DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
             resultTimeStr = formatter.format(resultTime) + "-00:00"; // add timezone off set from UTC
        } else {
           resultTimeStr = point.getResultDate();
        }
        return resultTimeStr;
    }

    private boolean onNewObservation(WaterMLPoint point) {
        boolean onNew = (!point.getMonLocIdentifier().equals(this.monLocIdentifier))
                || (!point.getPcode().equals(this.pcode))
                || (!Objects.equals(point.getStatisticDesc(), this.statisticDesc))
                || (!Objects.equals(point.getMeasurementTimeseriesId(), this.measurementTimeseriesId));
        if (onNew) {
            monLocIdentifier = point.getMonLocIdentifier();
            pcode = point.getPcode();
            statisticDesc = point.getStatisticDesc();
            measurementTimeseriesId = point.getMeasurementTimeseriesId();
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

    private void addElement(String namespaceURI, String name, String value) throws XMLStreamException {
        if (value == null) {
            writer.writeEmptyElement(namespaceURI, name);
        } else {
            writer.writeStartElement(namespaceURI, name);
            writer.writeCharacters(value);
            writer.writeEndElement();
        }
    }

    private void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
        if(value != null) {
            writer.writeAttribute(namespaceURI, localName, value);
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
              qualifierList =  Arrays.asList(csv.split(",", 0));
          }

          return qualifierList;
    }

    private List<String> getStatusList(WaterMLPoint point) {
        List<String> statusList = new ArrayList<>();
        if(point.getStatus() != null) {
           statusList.add(point.getStatus());
        } else if(point.getApprovalsAsJson() != null) {
            // convert to csv separated
            String csv =  point.getApprovalsAsJson().replace("[", "").
                    replace("]","").replace("\"", "");
            statusList =  Arrays.asList(csv.split(",", 0));
        }

        return statusList;
    }

}