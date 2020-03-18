package gov.usgs.wma.waterdata.timeseries;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.wma.waterdata.timeseries.geojson.TimeSeriesGeoJSON;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Observations - Monitoring Location", description="Download osmf-json")
@RestController
public class TimeSeriesController {

	protected TimeSeriesDao timeSeriesDao;

	@Autowired
	public TimeSeriesController(TimeSeriesDao timeSeriesDao) {
		this.timeSeriesDao = timeSeriesDao;
	}

	@Operation(
			description="Return GeoJSON Data specific to the requested Monitoring Location and Time Series.",
			responses= {
					@ApiResponse(
							responseCode="200",
							description="GeoJSON representation of the Time Series.",
							content=@Content(schema=@Schema(implementation=TimeSeriesGeoJSON.class))),
					@ApiResponse(
							responseCode="404",
							description="The requested Time Series was not found.",
							content=@Content())
			},
			externalDocs=@ExternalDocumentation(url="https://github.com/opengeospatial/omsf-profile/tree/master/omsf-json")
			)
	@GetMapping(value="monitoring-location/{monitoringLocationId}/time-series/{timeSeriesId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public String getMonitoringLocation(
			@PathVariable(value="monitoringLocationId") String featureId,
			@PathVariable(value="timeSeriesId") String timeSeriesId,
			HttpServletResponse response) {
		String rtn = timeSeriesDao.getTimeSeries(featureId, timeSeriesId);
		if (null == rtn) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}
		return rtn;
	}
}
