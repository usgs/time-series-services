package gov.usgs.wma.waterdata.collections;

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
							content = @Content(schema = @Schema(implementation = CollectionsGeoJSON.class)))
			},
			externalDocs=@ExternalDocumentation(url="http://docs.opengeospatial.org/is/17-069r3/17-069r3.html#_collections_")
			)
	@GetMapping(value = "collections", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollections(@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			HttpServletResponse response) {

		return collectionsDao.getCollectionsJson(collectionsParams.getParameters(null));
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

		String rtn = collectionsDao.getCollectionJson(collectionsParams.getParameters(collectionId));
		if (null == rtn) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}

		return rtn;
	}

}
