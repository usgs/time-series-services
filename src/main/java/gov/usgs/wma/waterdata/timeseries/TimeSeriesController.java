package gov.usgs.wma.waterdata.timeseries;

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

import gov.usgs.wma.waterdata.OgcException;
import gov.usgs.wma.waterdata.collections.BaseController;
import gov.usgs.wma.waterdata.openapi.schema.observations.StatisticalFeatureGeoJSON;
import gov.usgs.wma.waterdata.openapi.schema.timeseries.TimeSeriesGeoJSON;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Statistical Observations Datasets", description = "Feature Statistical Time Series Observations, such as min, max, or median.")
@RestController
public class TimeSeriesController extends BaseController {
	protected static final String URL_STATISTICAL_TIME_SERIES_COLLECTION = "collections/{" + PARAM_COLLECTION_ID
			+ "}/items/{" + PARAM_FEATURE_ID + "}/observations/statistical-time-series";

	protected static final String URL_STATISTICAL_TIME_SERIES = "collections/{" + PARAM_COLLECTION_ID + "}/items/{"
			+ PARAM_FEATURE_ID + "}/observations/statistical-time-series/{" + PARAM_TIME_SERIES_ID + "}";

	protected TimeSeriesDao timeSeriesDao;

	@Autowired
	public TimeSeriesController(TimeSeriesDao timeSeriesDao) {
		this.timeSeriesDao = timeSeriesDao;
	}

	@Operation(description = "Return JSON Data list of statistical time series available for the requested Monitoring Location.", responses = {
			@ApiResponse(responseCode = "200", description = "GeoJSON representation of the Statistical Time Series list.", content = @Content(schema = @Schema(implementation = StatisticalFeatureGeoJSON.class))),
			@ApiResponse(responseCode = "404", description = HTTP_404_DESCRIPTION, content = @Content(schema = @Schema(implementation = OgcException.class))),
			@ApiResponse(responseCode = "500", description = HTTP_500_DESCRIPTION, content = @Content(schema = @Schema(implementation = OgcException.class))) }, externalDocs = @ExternalDocumentation(url = "https://github.com/opengeospatial/omsf-profile/tree/master/omsf-json"))
	@GetMapping(value = URL_STATISTICAL_TIME_SERIES_COLLECTION, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getStatisticalTimeSeries(@PathVariable(value = PARAM_COLLECTION_ID) String collectionId, // ex: networkId,
			@PathVariable(value = PARAM_FEATURE_ID) String featureId,
			HttpServletResponse response) {

		String rtn = timeSeriesDao.getStatisticalTimeSeries(collectionId, featureId);

		if (null == rtn) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			rtn = ogc404Payload;
		}
		return rtn;
	}

	@Operation(description = "Return GeoJSON Data specific to the requested Monitoring Location and Time Series.", responses = {
			@ApiResponse(responseCode = "200", description = "GeoJSON representation of the Time Series.", content = @Content(schema = @Schema(implementation = TimeSeriesGeoJSON.class))),
			@ApiResponse(responseCode = "404", description = HTTP_404_DESCRIPTION, content = @Content(schema = @Schema(implementation = OgcException.class))),
			@ApiResponse(responseCode = "500", description = HTTP_500_DESCRIPTION, content = @Content(schema = @Schema(implementation = OgcException.class))) }, externalDocs = @ExternalDocumentation(url = "https://github.com/opengeospatial/omsf-profile/tree/master/omsf-json"))
	@GetMapping(value = URL_STATISTICAL_TIME_SERIES, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getTimeSeries(@PathVariable(value = PARAM_COLLECTION_ID) String collectionId, // ex: networkId,
			@PathVariable(value = PARAM_FEATURE_ID) String featureId,
			@PathVariable(value = PARAM_TIME_SERIES_ID) String timeSeriesId, // ex: USGS-123456
			HttpServletResponse response) {

		String rtn = timeSeriesDao.getTimeSeries(collectionId, featureId, timeSeriesId);
		if (rtn == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			rtn = ogc404Payload;
		}

		return rtn;
	}
}
