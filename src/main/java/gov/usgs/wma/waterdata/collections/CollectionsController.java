package gov.usgs.wma.waterdata.collections;

import java.util.List;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.wma.waterdata.collections.geojson.CollectionGeoJSON;
import gov.usgs.wma.waterdata.collections.geojson.CollectionsGeoJSON;
import gov.usgs.wma.waterdata.collections.geojson.FeatureGeoJSON;
import gov.usgs.wma.waterdata.timeseries.TimeSeriesDao;
import gov.usgs.wma.waterdata.timeseries.geojson.TimeSeriesGeoJSON;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Observations - OGC api", description = "Feature Collections")
@RestController
public class CollectionsController {
	protected CollectionsDao collectionsDao;

	protected CollectionParams collectionsParams;
	
	protected TimeSeriesDao timeSeriesDao;


	@Autowired
	public CollectionsController(CollectionsDao collectionsDao, CollectionParams collectionsParams, TimeSeriesDao timeSeriesDao) {
		this.collectionsDao = collectionsDao;
		this.collectionsParams = collectionsParams;
		this.timeSeriesDao = timeSeriesDao;
	}

	@Operation(
			description = "Return GeoJSON representation of the Collections.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "GeoJSON representation of the Collections.",
							content = @Content(schema = @Schema(implementation = CollectionsGeoJSON.class)))
			},
			externalDocs=@ExternalDocumentation(url="http://docs.opengeospatial.org/is/17-069r3/17-069r3.html#_collections_")
			)
	@GetMapping(value = "collections", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollections(@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			HttpServletResponse response) {

		return collectionsDao.getCollectionsJson(collectionsParams.buildParams(null));
	}

	@Operation(
			description = "Return GeoJSON Data specific to the requested Collection.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "GeoJSON representation of the Collection.",
							content = @Content(schema = @Schema(implementation = CollectionGeoJSON.class))),
					@ApiResponse(
							responseCode = "404",
							description = "The requested collection was not found.",
							content=@Content())
			},
			externalDocs=@ExternalDocumentation(url="http://docs.opengeospatial.org/is/17-069r3/17-069r3.html#_collection_")
			)
	@GetMapping(value = "collections/{collectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollection(@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			@PathVariable(value = "collectionId") String collectionId, HttpServletResponse response) {

		
		return doIfResponseOk(response, 
				()->collectionsDao.getCollectionJson(collectionsParams.buildParams(collectionId)));
	}

	@Operation(
			description = "Return GeoJSON Data specific to the features in the requested Collection.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "GeoJSON representation of the Feature.",
							content = @Content(schema = @Schema(implementation = FeatureGeoJSON.class))),
					@ApiResponse(
							responseCode = "404",
							description = "The requested collection feature was not found.",
							content=@Content())
			},
			externalDocs=@ExternalDocumentation(url="http://docs.opengeospatial.org/is/17-069r3/17-069r3.html#_feature_")
			)
	@GetMapping(value = "collections/{collectionId}/items", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollectionFeatures(
			@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			@RequestParam(value = "limit", required = false, defaultValue = "10000") int limit,
			@RequestParam(value = "startIndex", required = false, defaultValue = "0") int startIndex,
			@RequestParam(value = "bbox", required = false) List<String> bbox,
			@PathVariable(value = "collectionId") String collectionId, HttpServletResponse response) {


		// requesting less than one is a non-starter
		resultOr404(response, limit>0 ?"Good" :null);
		if (response.getStatus() == HttpServletResponse.SC_OK) {
			// check the collection feature count
			int count = collectionsDao.getCollectionFeatureCount(collectionsParams.buildParams(collectionId));
			// verify the start index is within the feature count
			resultOr404(response, startIndex<count  ?"Good" :null);
		}
		return doIfResponseOk(response, ()->collectionsDao.getCollectionFeaturesJson(
						collectionsParams.buildParams(collectionId)));
	}

	@Operation(
			description = "Return GeoJSON Data specific to the requested Collection Feature.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "GeoJSON representation of the Feature.",
							content = @Content(schema = @Schema(implementation = FeatureGeoJSON.class))),
					@ApiResponse(
							responseCode = "404",
							description = "The requested collection feature was not found.",
							content=@Content())
			},
			externalDocs=@ExternalDocumentation(url="http://docs.opengeospatial.org/is/17-069r3/17-069r3.html#_feature_")
			)
	@GetMapping(value = "collections/{collectionId}/items/{featureId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollectionFeature(
			@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			@PathVariable(value = "collectionId") String collectionId,
			@PathVariable(value = "featureId") String featureId, HttpServletResponse response) {

		// verify the collection exist before fetching the feature
		getOgcCollection(mimeType, collectionId, response);
		return doIfResponseOk(response, 
				()->collectionsDao.getCollectionFeatureJson(collectionsParams.buildParams(collectionId, featureId)));
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
	@GetMapping(value="collections/{collectionId}/items/{featureId}/time-series/{timeSeriesId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public String getTimeSeries(
			@PathVariable(value="collectionId") String collectionId, // ex: networkId,
			@PathVariable(value="featureId") String featureId, // ex: monitoringLocationId
			@PathVariable(value="timeSeriesId") String timeSeriesId, //ex: USGS-123456
			HttpServletResponse response) {
		
		// verify the collection and feature exist before fetching the time series
		getOgcCollectionFeature("json", collectionId, featureId, response);
		return doIfResponseOk(response, ()->timeSeriesDao.getTimeSeries(featureId, timeSeriesId));
	}

	/**
	 * Helper method to perform a call to ensure higher level entities exist.
	 * @param response the instance of the response for the request provided by spring
	 * @param lambda an action to do to respond to the request that returns a string upon success
	 * @return on a successful response it will be the string provided by the lambda.
	 */
	protected String doIfResponseOk(HttpServletResponse response, Supplier<String> lambda) {
		if (response.getStatus() == HttpServletResponse.SC_OK) {
			return resultOr404(response, lambda.get());
		}
		return null; // TODO should we return the empty string?
	}
	
	/**
	 * Helper method to set the response to 404 if there is no result from the request.
	 * @param lambda an action to do to respond to the request that returns a string upon success
	 * @param result the string response from any request
	 * @return on a successful response it will be the string provided by the lambda.
	 */
	protected String resultOr404(HttpServletResponse response, String result) {
		if (null == result) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}
		return result;
	}
}
