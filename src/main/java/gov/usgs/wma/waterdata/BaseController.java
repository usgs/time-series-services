package gov.usgs.wma.waterdata;

public abstract class BaseController {
	protected String ogc404Payload = "{\"code\":\"404\", \"description\":\"Requested data not found\"}";

	protected static final String HTTP_400_DESCRIPTION = "A query parameter has an invalid value.";
	protected static final String HTTP_404_DESCRIPTION = "The requested data was not found.";
	protected static final String HTTP_500_DESCRIPTION = "Unexpected error occurred.";
}