package gov.usgs.wma.waterdata.collections;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@Autowired
	protected CollectionParams collectionsHelper;

	@Autowired
	public CollectionsController(CollectionsDao collectionsDao) {
		this.collectionsDao = collectionsDao;
	}

	@Operation(description = "Return Collections.", responses = {
			@ApiResponse(responseCode = "200", description = "JSON representation the Collections.", content = @Content(schema = @Schema(implementation = OgcCollections.class))), }, externalDocs = @ExternalDocumentation(url = "https://github.com/opengeospatial/omsf-profile/tree/master/omsf-json"))
	@GetMapping(value = "collections", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollections(@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			HttpServletResponse response) {

		return collectionsDao.getCollectionsJson(collectionsHelper.getParameters(null));
	}

	@Operation(description = "Return collection by id.", responses = {
			@ApiResponse(responseCode = "200", description = "JSON representation the monitoring-locations Collection Metadata.", content = @Content(schema = @Schema(implementation = OgcCollections.class))), }, externalDocs = @ExternalDocumentation(url = "https://github.com/opengeospatial/omsf-profile/tree/master/omsf-json"))
	@GetMapping(value = "collections/{collectionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOgcCollection(@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			@PathVariable(value = "collectionId") String collectionId, HttpServletResponse response) {

		String rtn = collectionsDao.getCollectionJson(collectionsHelper.getParameters(collectionId));
		if (null == rtn) {
			rtn = String.format("Collection with id '%s' not found.", collectionId);
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}

		return rtn;
	}

}
