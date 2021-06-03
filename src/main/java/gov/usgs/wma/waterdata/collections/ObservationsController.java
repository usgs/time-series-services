package gov.usgs.wma.waterdata.collections;

import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_COLLECTION_ID;
import static gov.usgs.wma.waterdata.collections.CollectionParams.PARAM_FEATURE_ID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.wma.waterdata.BaseController;
import gov.usgs.wma.waterdata.OgcException;
import gov.usgs.wma.waterdata.openapi.schema.observations.ObservationTypesJSON;
import gov.usgs.wma.waterdata.openapi.schema.observations.ObservationsJSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Observations Datasets", description = "Feature Observations")
@RestController
public class ObservationsController extends BaseController {

	protected CollectionsDao collectionsDao;

	protected CollectionParams collectionsParams;

	@Autowired
	public ObservationsController(CollectionsDao collectionsDao, CollectionParams collectionsParams) {
		this.collectionsDao = collectionsDao;
		this.collectionsParams = collectionsParams;
	}


	@Operation(description = "Return data sets available at the monitoring location.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "available datasets.",
							content = @Content(schema = @Schema(implementation = ObservationTypesJSON.class))),
					@ApiResponse(
							responseCode = "404",
							description = HTTP_404_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class))),
					@ApiResponse(
							responseCode = "500",
							description = HTTP_500_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class)))
					}
		)
	@GetMapping(value = "collections/{collectionId}/items/{featureId}/observations", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getObservationTypes(
			@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			@PathVariable(value = PARAM_COLLECTION_ID) String collectionId,
			@PathVariable(value = PARAM_FEATURE_ID) String featureId,
			HttpServletResponse response) {

		String 	rtn = collectionsDao.getObsverationsJson(collectionsParams.builder().collectionId(collectionId)
			.featureId(featureId).build());
		if (rtn == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}

		return rtn;
	}

	@Operation(description = "Return descrete data observations available at the monitoring location.",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "available observations.",
							content = @Content(schema = @Schema(implementation = ObservationsJSON.class))),
					@ApiResponse(
							responseCode = "404",
							description = HTTP_404_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class))),
					@ApiResponse(
							responseCode = "500",
							description = HTTP_500_DESCRIPTION,
							content = @Content(schema = @Schema(implementation = OgcException.class)))
					}
		)
	@GetMapping(value = "collections/{collectionId}/items/{featureId}/observations/discrete-data", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getDiscreteDataObservations(
			@RequestParam(value = "f", required = false, defaultValue = "json") String mimeType,
			@PathVariable(value = PARAM_COLLECTION_ID) String collectionId,
			@PathVariable(value = PARAM_FEATURE_ID) String featureId,
			HttpServletResponse response) {

		String 	rtn = collectionsDao.getDiscreteDataObsverationsJson(collectionsParams.builder().collectionId(collectionId)
			.featureId(featureId).build());
		if (rtn == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}

		return rtn;
	}
}
