package gov.usgs.wma.waterdata.collections;

import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_FEATURE_ID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.wma.waterdata.collections.observations.StatisticalFeatureGeoJSON;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Statistical Observations Datasets", description = "Feature Statistical Time Series Observations, such as min, max, or median.")
@RestController
public class CollectionsStatTimeSeriesController {

	protected static final String URL_STATISTICAL_TIME_SERIES_COLLECTION
		= "collections/{"+PARAM_COLLECTION_ID+"}/items/{"+PARAM_FEATURE_ID+"}/observations/statistical-time-series";

	protected CollectionsDao collectionsDao;


	@Autowired
	public CollectionsStatTimeSeriesController(CollectionsDao collectionsDao) {
		this.collectionsDao = collectionsDao;
	}

	@Operation(
			description="Return JSON Data list of statistical time series available for the requested Monitoring Location.",
			responses= {
					@ApiResponse(
							responseCode="200",
							description="GeoJSON representation of the Statistical Time Series list.",
							content=@Content(schema=@Schema(implementation=StatisticalFeatureGeoJSON.class))),
					@ApiResponse(
							responseCode="404",
							description="The requested collection and feature combination was not found.",
							content=@Content())
			},
			externalDocs=@ExternalDocumentation(url="https://github.com/opengeospatial/omsf-profile/tree/master/omsf-json")
		)
	@GetMapping(value=URL_STATISTICAL_TIME_SERIES_COLLECTION, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getTimeSeries(
			@PathVariable(value=PARAM_COLLECTION_ID) String collectionId, // ex: networkId,
			@PathVariable(value=PARAM_FEATURE_ID) String featureId, // ex: monitoringLocationId
			HttpServletResponse response) {

		// verify the collection and feature exist before fetching the time series
		String json = collectionsDao.getStatisticalTimeSeries(collectionId, featureId);

		if (null == json) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}
		return json;
	}
}