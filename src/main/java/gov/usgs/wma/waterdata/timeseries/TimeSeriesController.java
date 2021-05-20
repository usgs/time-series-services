package gov.usgs.wma.waterdata.timeseries;

import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_FEATURE_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_TIME_SERIES_ID;

import java.io.IOException;

import gov.usgs.wma.waterdata.OgcException;
import gov.usgs.wma.waterdata.collections.BaseController;
import gov.usgs.wma.waterdata.openapi.schema.observations.StatisticalFeatureGeoJSON;
import gov.usgs.wma.waterdata.openapi.schema.timeseries.TimeSeriesGeoJSON;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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

	@Operation(description = "Return GeoJSON or Water ML 2 Data specific to the requested Monitoring Location and Time Series.", responses = {
			@ApiResponse(responseCode = "200", description = "GeoJSON representation of the Time Series.", content = @Content(schema = @Schema(implementation = TimeSeriesGeoJSON.class))),
			@ApiResponse(responseCode = "400", description = HTTP_400_DESCRIPTION, content = @Content(schema = @Schema(implementation = OgcException.class))),
			@ApiResponse(responseCode = "404", description = HTTP_404_DESCRIPTION, content = @Content(schema = @Schema(implementation = OgcException.class))),
			@ApiResponse(responseCode = "500", description = HTTP_500_DESCRIPTION, content = @Content(schema = @Schema(implementation = OgcException.class))) }, externalDocs = @ExternalDocumentation(url = "https://github.com/opengeospatial/omsf-profile/tree/master/omsf-json"))
	@GetMapping(value = URL_STATISTICAL_TIME_SERIES, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public void getTimeSeries(@PathVariable(value = PARAM_COLLECTION_ID) String collectionId,
			@PathVariable(value = PARAM_FEATURE_ID) String featureId,
			@PathVariable(value = PARAM_TIME_SERIES_ID) String timeSeriesId, // ex: 2ae58a8bdb1b4b778577a2ce3a5362d0
			@Parameter(in = ParameterIn.QUERY, description = contentTypeDesc, schema = @Schema(type = "string"), examples = {
					@ExampleObject(name = "json", value = "json", description = "GeoJSON"),
					@ExampleObject(name = "xml", value = "xml", description = "Water ML 2")})
			@RequestParam(value = "f", required = false, defaultValue = "json")
			String mimeType,
			HttpServletResponse response)
					throws HttpMediaTypeNotAcceptableException, IOException {

		determineContentType(mimeType);
		String rtn = null;
		if (contentIsJson()) {
			rtn = timeSeriesDao.getTimeSeries(collectionId, featureId, timeSeriesId);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		} else if (contentIsXml()) {
			rtn = timeSeriesDao.getTimeSeriesWaterML(collectionId, featureId, timeSeriesId);
			response.setContentType(MediaType.APPLICATION_XML_VALUE);
		}

		if (rtn == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			rtn = ogc404Payload;
		}

		response.getWriter().print(rtn);
	}
}
