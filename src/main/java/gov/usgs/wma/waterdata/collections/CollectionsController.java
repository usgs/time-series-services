package gov.usgs.wma.waterdata.collections;

import gov.usgs.wma.waterdata.OgcException;
import gov.usgs.wma.waterdata.openapi.schema.collections.CollectionGeoJSON;
import gov.usgs.wma.waterdata.openapi.schema.collections.CollectionsGeoJSON;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_FEATURE_ID;

@Tag(name = "Observations - OGC api", description = "Feature Collections")
@RestController
@Validated
public class CollectionsController extends BaseController {
	protected CollectionsDao collectionsDao;

	protected CollectionParams collectionsParams;
	public static final String REGEX_FIPS_COUNTRY = "[A-Z]{2}";
	public static final String REGEX_FIPS_COUNTRY_MESS = "countryFIPS must match " + REGEX_FIPS_COUNTRY;
	public static final String FIPS_COUNTRY_DESC =  "two-character Federal Information Processing Standard (FIPS) Country Codes." +
            " (Example: US for United States, MX for Mexico)";
	public static final String REGEX_FIPS_HYDRO = "[0-9]{12}";
	public static final String REGEX_FIPS_HYDRO_MESS = "hydrologic unit must match " + REGEX_FIPS_HYDRO;

	public static final String REGEX_FIPS_COUNTY = "(?:([A-Z]{2}):)?([0-9]{1,2}):([0-9]{3}|N/A)";
	public static final String REGEX_FIPS_COUNTY_MESS = "countyFIPS must match " + REGEX_FIPS_COUNTY;
	public static final String FIPS_COUNTY_DESC =
			"two-character Federal Information Processing Standard (FIPS) Country Code, followed by a colon" +
            " followed by a two-digit FIPS State Code, followed by a colon, followed by a three-digit FIPS County Code." +
            " (Example: Buffalo County in Wisconsin is US:55:011)";

	public static final String FIPS_STATE_DESC =
			"two-character Federal Information Processing Standard (FIPS) country code, " +
	        "followed by a colon, followed by a two-digit FIPS state code." +
            " (Example: Wisconsin is US:55)";
	public static final String REGEX_FIPS_STATE = "(?:([A-Z]{2}):)?([0-9]{1,2})";
	public static final String REGEX_FIPS_STATE_MESS = "stateFIPS must match " + REGEX_FIPS_STATE;

	protected static final String LIMIT_VALIDATE_MESS = "limit must be greater than or equal to 1";
	protected static final String START_INDEX_VALIDATE_MESS = "startIndex must be greater than or equal to 0";
	protected static final String BBOX_DESCRIPTION = "Bounding box: minimum longitude, minimum latitude, maximum longitude, maximum latitude<br>"
			+ "bbox=-109.046667,37.0,-102.046667,39.0 limits results to monitoring sites in Colorado.";

	@Autowired
	public CollectionsController(CollectionsDao collectionsDao, CollectionParams collectionsParams) {
		this.collectionsDao = collectionsDao;
		this.collectionsParams = collectionsParams;
	}

	@Operation(
			description = "Return GeoJSON representation of the Collections.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "GeoJSON representation of the Collections.",
							content = @Content(schema = @Schema(implementation = CollectionsGeoJSON.class))),
					@ApiResponse(
							responseCode = "500",
							description = HTTP_500_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class)))
			},
			externalDocs=@ExternalDocumentation(url="http://docs.opengeospatial.org/is/17-069r3/17-069r3.html#_collections_")
		)
	@GetMapping(value = "collections", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollections(@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			HttpServletResponse response) {
		return collectionsDao.getCollectionsJson(collectionsParams.builder().build());
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
							description = HTTP_404_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class))),
					@ApiResponse(
							responseCode = "500",
							description = HTTP_500_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class)))
			},
			externalDocs=@ExternalDocumentation(url="http://docs.opengeospatial.org/is/17-069r3/17-069r3.html#_collection_")
		)
	@GetMapping(value = "collections/{collectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollection(@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			@PathVariable(value = PARAM_COLLECTION_ID) String collectionId, HttpServletResponse response) {
		String rtn = collectionsDao.getCollectionJson(collectionsParams.builder().collectionId(collectionId).build());
		if (rtn == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			rtn = ogc404Payload;
		}

		return rtn;
	}

	@Operation(
			description = "Return GeoJSON Data specific to the features in the requested Collection.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "GeoJSON representation of the Feature Collection.",
							content = @Content(schema = @Schema(implementation = FeatureCollectionGeoJSON.class))),
					@ApiResponse(
							responseCode = "400",
							description = HTTP_400_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class))),
					@ApiResponse(
							responseCode = "404",
							description = HTTP_404_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class))),
					@ApiResponse(
							responseCode = "500",
							description = HTTP_500_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class)))
			},
			externalDocs=@ExternalDocumentation(url="http://docs.opengeospatial.org/is/17-069r3/17-069r3.html#_feature_")
		)
	@Parameter(name = "bbox", description = BBOX_DESCRIPTION, schema = @Schema(implementation = String.class, type = "string"))
	@GetMapping(value = "collections/{collectionId}/items", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollectionFeatures(
		HttpServletRequest request,
		@Size(min=1, max=250, message="The number of countries queried on must be between {min} and {max}")
		@Parameter(description=FIPS_COUNTRY_DESC)
		@RequestParam(value="countryFIPS", required = false) List<@Pattern(regexp=REGEX_FIPS_COUNTRY, message=REGEX_FIPS_COUNTRY_MESS) String> countries,

		@Size(min=1, max=1000, message="The number of counties queried on must be between {min} and {max}")
		@Parameter(description=FIPS_COUNTY_DESC)
		@RequestParam(value="countyFIPS", required = false) List<@Pattern(regexp=REGEX_FIPS_COUNTY, message=REGEX_FIPS_COUNTY_MESS) String> counties,

		@Size(min=1, max=55, message="The number of states queried on must be between {min} and {max}")
		@Parameter(description=FIPS_STATE_DESC)
		@RequestParam(value="stateFIPS", required = false) List<@Pattern(regexp=REGEX_FIPS_STATE, message=REGEX_FIPS_STATE_MESS) String> states,

		@Size(min=1, max=1000, message="The number of hydrologic units queried on must be between {min} and {max}")
		@Parameter(description="Example: 040103020107")
		@RequestParam(value="hydrologicUnit", required = false)
			List<@Pattern(regexp=REGEX_FIPS_HYDRO, message=REGEX_FIPS_HYDRO_MESS) String> hydrologicUnits,

		@Parameter(description="Example: N100GLCIAL")
		@RequestParam(value="nationalAquiferCode", required = false) String nationalAquiferCode,

		@Parameter(description="Example: 343204093005501")
		@RequestParam(value="monitoringLocationNumber", required = false) String monitoringLocationNumber,

		@Size(min=1, max=100, message="The number of monitoring location types queried on must be between {min} and {max}")
		@Parameter(description="Well, Stream, or other type")
		@RequestParam(value="monitoringLocationType", required = false) List<String> monitoringLocationType,

		@Parameter(description="USGS or other agency")
		@RequestParam(value="agencyCode", required = false) String agencyCode,

		@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,

		@Min(value=1, message = LIMIT_VALIDATE_MESS) @RequestParam(value = "limit", required = false, defaultValue = "10000") int limit,
		@Min(value=0, message = START_INDEX_VALIDATE_MESS) @RequestParam(value = "startIndex", required = false, defaultValue = "0") int startIndex,

		@BBox @RequestParam(value = "bbox", required = false) BoundingBox bbox,
		@Parameter(description="monitoring-locations or ANC") @PathVariable(value = PARAM_COLLECTION_ID) String collectionId,
		HttpServletResponse response) {
		Map<String, Object> params = collectionsParams.builder().collectionId(collectionId)
			.countries(countries).states(states).counties(counties).hydrologicUnits(hydrologicUnits)
			.nationalAquiferCode(nationalAquiferCode).agencyCode(agencyCode)
			.monitoringLocationNumber(monitoringLocationNumber)
			.monitoringLocationType(monitoringLocationType)
			.bbox(bbox).build();

		int count = collectionsDao.getCollectionFeatureCount(params);
		
		String rtn;
		if (startIndex >= count) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			rtn = ogc404Payload;
		} else {
			params = collectionsParams.builder().collectionId(collectionId).bbox(bbox)
				.paging(limit, startIndex, count)
				.countries(countries).states(states).counties(counties)
				.hydrologicUnits(hydrologicUnits).nationalAquiferCode(nationalAquiferCode)
				.agencyCode(agencyCode).monitoringLocationNumber(monitoringLocationNumber)
				.monitoringLocationType(monitoringLocationType)
				.bbox(bbox). paging(limit, startIndex, count).build();

			rtn = collectionsDao.getCollectionFeaturesJson(params);
			if (rtn == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				rtn = ogc404Payload;
			}
		}

		return rtn;
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
							description = HTTP_404_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class))),
					@ApiResponse(
							responseCode = "500",
							description = HTTP_500_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class)))
			},
			externalDocs=@ExternalDocumentation(url="http://docs.opengeospatial.org/is/17-069r3/17-069r3.html#_feature_")
		)
	@GetMapping(value = "collections/{collectionId}/items/{featureId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollectionFeature(
			@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			@PathVariable(value = PARAM_COLLECTION_ID) String collectionId,
			@PathVariable(value = PARAM_FEATURE_ID) String featureId, HttpServletResponse response) {


		String rtn =collectionsDao.getCollectionFeatureJson(collectionsParams.builder().collectionId(collectionId).
			featureId(featureId).build());
			if (rtn == null) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
				rtn = ogc404Payload;
			}

		return rtn;
	}
}
