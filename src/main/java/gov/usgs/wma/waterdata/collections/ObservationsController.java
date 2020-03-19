package gov.usgs.wma.waterdata.collections;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.wma.waterdata.collections.observations.ObservationsJSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Observations Datasets", description = "Feature Observations")
@RestController
public class ObservationsController {
	
	protected CollectionsDao collectionsDao;

	protected CollectionParams collectionsParams;
	
	@Autowired
	public ObservationsController(CollectionsDao collectionsDao, CollectionParams collectionsParams) {
		this.collectionsDao = collectionsDao;
		this.collectionsParams = collectionsParams;
	}

	
	@Operation(description = "Return data sets available at the monitoring location.", responses = {
			@ApiResponse(responseCode = "200", description = "available datasets.", content = @Content(schema = @Schema(implementation = ObservationsJSON.class))),
			@ApiResponse(responseCode = "404", description = "The specified collection monitoring location was not found.", content = @Content()) })
	@GetMapping(value = "collections/{collectionId}/items/{featureId}/observations", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getObservationTypes(
			@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			@PathVariable(value = "collectionId") String collectionId,
			@PathVariable(value = "featureId") String featureId,
			HttpServletResponse response) {

		String 	rtn = collectionsDao.getObsverationsJson(collectionsParams.buildParams(collectionId, featureId));
		if (rtn == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		} 

		return rtn;
	}
}