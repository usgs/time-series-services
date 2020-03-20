package gov.usgs.wma.waterdata.collections;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ObservationsStatTimeSeriesController {
	private static final Logger log = LoggerFactory.getLogger(ObservationsStatTimeSeriesController.class);
	
	protected static final String DEFAULT_COLLECTION_ID = "monitoring-locations";
	
	protected static final String URL_PARAM_COLLECTION_ID = "collectionId";
	protected static final String URL_PARAM_FEATURE_ID = "featureId";
	protected static final String URL_PARAM_TIME_SERIES_ID = "timeSeriesId";
	
	protected static final String URL_STATISTICAL_TIME_SERIES 
		= "observations/collections/{"+URL_PARAM_COLLECTION_ID+"}/items/{"+URL_PARAM_FEATURE_ID+"}/observations/statistical-time-series/{"+URL_PARAM_TIME_SERIES_ID+"}";
	
	protected CollectionsDao collectionsDao;

	protected CollectionParams collectionsParams;
	
	protected TimeSeriesDao timeSeriesDao;

	@Autowired
	public ObservationsStatTimeSeriesController(CollectionsDao collectionsDao, CollectionParams collectionsParams, TimeSeriesDao timeSeriesDao) {
		this.collectionsDao = collectionsDao;
		this.collectionsParams = collectionsParams;
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
			@PathVariable(value=URL_PARAM_COLLECTION_ID) String collectionId, // ex: networkId,
			@PathVariable(value=URL_PARAM_FEATURE_ID) String featureId, // ex: monitoringLocationId
			@PathVariable(value=URL_PARAM_TIME_SERIES_ID) String timeSeriesId, //ex: USGS-123456
			HttpServletResponse response) {
		
		log.trace("Growndwater time series request");
		
		// verify the collection and feature exist before fetching the time series
		String json = timeSeriesDao.getTimeSeries(collectionId, featureId, timeSeriesId);
		
		if (null == json) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}
		return json;
	}
}