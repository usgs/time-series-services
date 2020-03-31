package gov.usgs.wma.waterdata.collections;

import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_FEATURE_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_TIME_SERIES_ID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.wma.waterdata.timeseries.TimeSeriesDao;
import gov.usgs.wma.waterdata.timeseries.geojson.TimeSeriesGeoJSON;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Statistical Observations Datasets", description = "Feature Statistical Time Series Observations, such as min, max, or median.")
@RestController
public class ObservationsStatTimeSeriesController extends BaseController {

	protected static final String URL_STATISTICAL_TIME_SERIES 
		= "collections/{"+PARAM_COLLECTION_ID+"}/items/{"+PARAM_FEATURE_ID+"}/observations/statistical-time-series/{"+PARAM_TIME_SERIES_ID+"}";
	
	protected TimeSeriesDao timeSeriesDao;

	@Autowired
	public ObservationsStatTimeSeriesController(TimeSeriesDao timeSeriesDao) {
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
	@GetMapping(value=URL_STATISTICAL_TIME_SERIES, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getTimeSeries(
			@PathVariable(value=PARAM_COLLECTION_ID) String collectionId, // ex: networkId,
			@PathVariable(value=PARAM_FEATURE_ID) String featureId, // ex: monitoringLocationId
			@PathVariable(value=PARAM_TIME_SERIES_ID) String timeSeriesId, //ex: USGS-123456
			HttpServletResponse response) {

		String rtn = timeSeriesDao.getTimeSeries(collectionId, featureId, timeSeriesId);
		if (rtn == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			rtn = ogc404Payload;
		}

		return rtn;
	}
}