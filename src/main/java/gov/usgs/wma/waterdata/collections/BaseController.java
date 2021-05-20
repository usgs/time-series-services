package gov.usgs.wma.waterdata.collections;

import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

public abstract class BaseController {
	protected final String JSON = MediaType.APPLICATION_JSON_VALUE.replaceAll(".*/", "");
	protected final String XML = MediaType.APPLICATION_XML_VALUE.replaceAll(".*/", "");
	protected final String CONTENT_TYPE_MESS = "Content type must be either json or xml";

	protected String contentType = JSON;
	protected final String contentTypeDesc = "Content format returned, default json";
	protected String ogc404Payload = "{\"code\":\"404\", \"description\":\"Requested data not found\"}";

	protected static final String HTTP_400_DESCRIPTION = "A query parameter has an invalid value.";
	protected static final String HTTP_404_DESCRIPTION = "The requested data was not found.";
	protected static final String HTTP_500_DESCRIPTION = "Unexpected error occurred.";

	protected void determineContentType(String mimeType) throws HttpMediaTypeNotAcceptableException {
		// TODO: for now just check the query parameter. later 'Accept' header.
		// Allow for application/json or json as values
		String baseMimeType = mimeType == null ? null : mimeType.replaceAll(".*/", "");
		if (baseMimeType == null || baseMimeType.trim().isEmpty() || baseMimeType.equalsIgnoreCase(JSON)) {
			contentType = JSON;
		} else if (baseMimeType.equalsIgnoreCase(XML)) {
			contentType = XML;
		} else {
			throw new HttpMediaTypeNotAcceptableException(CONTENT_TYPE_MESS);
		}
	}

	protected boolean contentIsJson() {
		return JSON.equals(contentType);
	}

	protected boolean contentIsXml() {
		return XML.equals(contentType);
	}

}
