package gov.usgs.wma.waterdata.collections;

import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_COLLECTION_ID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.wma.waterdata.BaseController;
import gov.usgs.wma.waterdata.OgcException;
import gov.usgs.wma.waterdata.openapi.schema.collections.CollectionGeoJSON;
import gov.usgs.wma.waterdata.openapi.schema.collections.CollectionsGeoJSON;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Observations - OGC api", description = "Feature Collections")
@RestController
@Validated
public class CollectionsController extends BaseController {

	protected CollectionsDao collectionsDao;
	protected CollectionParams collectionsParams;

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

		String rtn = collectionsDao.getCollectionJson(collectionsParams.buildParams(collectionId));
		if (rtn == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			rtn = ogc404Payload;
		}

		return rtn;
	}
}
