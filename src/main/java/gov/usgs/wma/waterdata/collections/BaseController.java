package gov.usgs.wma.waterdata.collections;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

public abstract class BaseController {
	protected String ogc404Payload = "{\"code\":\"404\", \"description\":\"Requested data not found\"}";

	/**
	 * Helper method to set the response to 404 if there is no result from the
	 * request.
	 *
	 * @param response the HTTP response object that will be updated with 404 if no
	 *                 (null) result.
	 * @param result   the string response from any request
	 * @return ogc compliant payload containing code and description.
	 */
	protected String resultOr404(HttpServletResponse response, String result) {
		// set the response code to 404 if no results are found.
		if (null == result) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return ogc404Payload;
		}
		return result;
	}
}