package gov.usgs.wma.waterdata.features;

import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_FEATURE_ID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.wma.waterdata.BaseController;
import gov.usgs.wma.waterdata.collections.CollectionParams;
import gov.usgs.wma.waterdata.openapi.responses.Http400Response;
import gov.usgs.wma.waterdata.openapi.responses.Http404Response;
import gov.usgs.wma.waterdata.openapi.responses.Http500Response;
import gov.usgs.wma.waterdata.openapi.schema.collections.FeatureCollectionGeoJSON;
import gov.usgs.wma.waterdata.openapi.schema.collections.FeatureGeoJSON;
import gov.usgs.wma.waterdata.parameter.BoundingBox;
import gov.usgs.wma.waterdata.validation.BBox;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Features - OGC api", description = "Feature Collections")
@RestController
@Validated
public class FeaturesController extends BaseController {

	protected FeaturesDao featuresDao;
	//TODO This should be a utility class with static methods so it does not need to be injected all over.
	//TODO OR eliminated and querys changed to use the actual object, rather than converting to a map all the time.
	protected CollectionParams collectionsParams;

	protected static final String LIMIT_VALIDATE_MESS = "limit must be greater than or equal to 1";
	protected static final String START_INDEX_VALIDATE_MESS = "startIndex must be greater than or equal to 0";
	protected static final String BBOX_DESCRIPTION = "Bounding box: minimum longitude, minimum latitude, maximum longitude, maximum latitude<br>"
			+ "bbox=-109.046667,37.0,-102.046667,39.0 limits results to monitoring sites in Colorado.";

	@Autowired
	public FeaturesController(FeaturesDao featuresDao, CollectionParams collectionsParams) {
		this.featuresDao = featuresDao;
		this.collectionsParams = collectionsParams;
	}

	@Operation(
			description="Return GeoJSON Data specific to the features in the requested Collection.",
			externalDocs=@ExternalDocumentation(url="http://docs.opengeospatial.org/is/17-069r3/17-069r3.html#_feature_")
		)
	@Parameter(name="bbox", description=BBOX_DESCRIPTION, schema=@Schema(type="string"))
	@ApiResponse(
			responseCode = "200",
			description = "GeoJSON representation of the Feature Collection.",
			content = @Content(schema = @Schema(implementation = FeatureCollectionGeoJSON.class)))
	@Http400Response
	@Http404Response
	@Http500Response
	@GetMapping(value="collections/{collectionId}/items", produces=MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollectionFeatures(HttpServletResponse response,
			@PathVariable(value = PARAM_COLLECTION_ID) String collectionId,
			@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			@Min(value=1, message = LIMIT_VALIDATE_MESS) @RequestParam(value = "limit", required = false, defaultValue = "10000") int limit,
			@Min(value=0, message = START_INDEX_VALIDATE_MESS) @RequestParam(value = "startIndex", required = false, defaultValue = "0") int startIndex,
			@BBox @RequestParam(value = "bbox", required = false) BoundingBox bbox
			) {

		//TODO - I think this gives the wrong results with a bbox query
		int count = featuresDao.getCollectionFeatureCount(collectionsParams.buildParams(collectionId));

		String rtn;
		if (startIndex >= count) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			rtn = ogc404Payload;
		} else {
			rtn = featuresDao.getCollectionFeaturesJson(
					collectionsParams.buildParams(collectionId, limit, startIndex, bbox, count));
			if (rtn == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				rtn = ogc404Payload;
			}
		}

		return rtn;
	}

	@Operation(
			description = "Return GeoJSON Data specific to the requested Collection Feature.",
			externalDocs=@ExternalDocumentation(url="http://docs.opengeospatial.org/is/17-069r3/17-069r3.html#_feature_")
		)
	@ApiResponse(
			responseCode = "200",
			description = "GeoJSON representation of the Feature.",
			content = @Content(schema = @Schema(implementation = FeatureGeoJSON.class)))
	@Http404Response
	@Http500Response
	@GetMapping(value = "collections/{collectionId}/items/{featureId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollectionFeature(HttpServletResponse response,
			@PathVariable(value = PARAM_COLLECTION_ID) String collectionId,
			@PathVariable(value = PARAM_FEATURE_ID) String featureId,
			@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType
			) {

		String rtn = featuresDao.getCollectionFeatureJson(collectionsParams.buildParams(collectionId, featureId));
			if (rtn == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				rtn = ogc404Payload;
			}

		return rtn;
	}
}
