package gov.usgs.wma.waterdata.format;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.io.Writer;

public class XmlFactory {
    private static XMLInputFactory xmlInputFactory;
    private static XMLOutputFactory xmlOutputFactory;
    private static XmlMapper mapper;

    static {
        xmlInputFactory = XMLInputFactory.newFactory();
        xmlOutputFactory = XMLOutputFactory.newFactory();
        xmlOutputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", "true");

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

}
