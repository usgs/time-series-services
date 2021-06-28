package gov.usgs.wma.waterdata.format;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.OutputStream;
import java.io.Writer;

public class XmlFactory {
    private static XMLInputFactory xmlInputFactory;
    private static XMLOutputFactory xmlOutputFactory;
    private static XmlMapper mapper;
    private static SchemaFactory schemaFactory;

    static {
        xmlInputFactory = XMLInputFactory.newFactory();
        xmlOutputFactory = XMLOutputFactory.newFactory();
        xmlOutputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", "true");

        schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // then Jackson components
        mapper = new XmlMapper(xmlInputFactory);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.findAndRegisterModules();
        mapper.setAnnotationIntrospector(new InheritNamespaceAnnotationIntrospector());
    }

    public static XmlMapper getXmlMapper() {
        return mapper;
    }

    public static XMLStreamWriter newXMLStreamWriter(Writer writer) throws XMLStreamException {
        return xmlOutputFactory.createXMLStreamWriter(writer);
    }

    public static XMLStreamWriter newXMLStreamWriter(OutputStream out) throws XMLStreamException {
        return xmlOutputFactory.createXMLStreamWriter(out);
    }

    public static Schema newXMLSchema(String schemaUrl) throws SAXException {
        return schemaFactory.newSchema(new StreamSource(schemaUrl));
    }

}
