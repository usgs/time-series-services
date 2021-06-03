package gov.usgs.wma.waterdata;

import gov.usgs.wma.waterdata.parameter.ContentType;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.HttpMediaTypeNotAcceptableException;

public abstract class BaseController {
	protected final String CONTENT_TYPE_MESS = "Content type (f=) must be one of: ";

	protected final String contentTypeDesc = "Content format returned, default json";
	protected String ogc404Payload = "{\"code\":\"404\", \"description\":\"Requested data not found\"}";

	protected static final String HTTP_400_DESCRIPTION = "A query parameter has an invalid value.";
	protected static final String HTTP_404_DESCRIPTION = "The requested data was not found.";
	protected static final String HTTP_500_DESCRIPTION = "Unexpected error occurred.";

	protected ContentType determineContentType(String mimeType) throws HttpMediaTypeNotAcceptableException {
		return determineContentType(mimeType, Arrays.asList(ContentType.values()));
	}

	protected ContentType determineContentType(String mimeType, List<ContentType> acceptedTypes)
			throws HttpMediaTypeNotAcceptableException {
		// TODO: for now just check the query parameter. later 'Accept' header.
		ContentType contentType = null;
		if (ContentType.json.name().equalsIgnoreCase(mimeType)) {
			contentType = ContentType.json;
		} else if (ContentType.waterml.name().equalsIgnoreCase(mimeType)) {
			contentType = ContentType.waterml;
		}

		if (contentType == null) {
			throw new HttpMediaTypeNotAcceptableException(CONTENT_TYPE_MESS + acceptedTypes);
		} else if(acceptedTypes != null && !acceptedTypes.contains(contentType)) {
			throw new HttpMediaTypeNotAcceptableException(CONTENT_TYPE_MESS + acceptedTypes);
		}
		return contentType;
	}

}
