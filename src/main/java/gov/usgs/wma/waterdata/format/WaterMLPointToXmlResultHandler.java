package gov.usgs.wma.waterdata.format;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import gov.usgs.wma.waterdata.domain.WaterMLPoint;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;

public class WaterMLPointToXmlResultHandler implements ResultHandler<WaterMLPoint> {

	final XmlMapper mapper;
	final XMLStreamWriter writer;

	public WaterMLPointToXmlResultHandler(XmlMapper mapper, XMLStreamWriter writer) {
		this.writer = writer;
		this.mapper = mapper;
	}

	@Override
	public void handleResult(final ResultContext<? extends WaterMLPoint> resultContext) {
		WaterMLPoint point = resultContext.getResultObject();
		try {
			mapper.writeValue(writer, point);
		} catch (IOException e) {
			throw new RuntimeException("Exception during serialization", e);
		}
	}
}
