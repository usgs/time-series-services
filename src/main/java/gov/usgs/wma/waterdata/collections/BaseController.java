package gov.usgs.wma.waterdata.collections;

import gov.usgs.wma.waterdata.parameter.ContentType;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.HttpMediaTypeNotAcceptableException;

public abstract class BaseController {
	protected final String CONTENT_TYPE_MESS = "Content type (f=) must be one of: ";

	protected ContentType contentType;
	protected final String contentTypeDesc = "Content format returned, default json";
	protected String ogc404Payload = "{\"code\":\"404\", \"description\":\"Requested data not found\"}";

	protected static final String HTTP_400_DESCRIPTION = "A query parameter has an invalid value.";
	protected static final String HTTP_404_DESCRIPTION = "The requested data was not found.";
	protected static final String HTTP_500_DESCRIPTION = "Unexpected error occurred.";

	protected void determineContentType(String mimeType) throws HttpMediaTypeNotAcceptableException {
		determineContentType(mimeType, Arrays.asList(ContentType.values()));
	}

	protected void determineContentType(String mimeType, List<ContentType> acceptedTypes)
			throws HttpMediaTypeNotAcceptableException {
		// TODO: for now just check the query parameter. later 'Accept' header.
		// Allow for application/json or json as values
		ContentType contentTypeLocal = null;
		if (ContentType.json.name().equalsIgnoreCase(mimeType)) {
			contentTypeLocal = ContentType.json;
		} else if (ContentType.waterml.name().equalsIgnoreCase(mimeType)) {
			contentTypeLocal = ContentType.waterml;
		}

		if (contentTypeLocal == null) {
			throw new HttpMediaTypeNotAcceptableException(CONTENT_TYPE_MESS + acceptedTypes);
		} else if(acceptedTypes != null && !acceptedTypes.contains(contentTypeLocal)) {
			throw new HttpMediaTypeNotAcceptableException(CONTENT_TYPE_MESS + acceptedTypes);
		}
		this.contentType = contentTypeLocal;
	}

	protected boolean contentIsJson() {
		return ContentType.json.equals(contentType);
	}

	protected boolean contentIsWaterML() {
		return ContentType.waterml.equals(contentType);
	}

}
