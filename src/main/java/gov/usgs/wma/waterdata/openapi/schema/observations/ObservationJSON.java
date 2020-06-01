package gov.usgs.wma.waterdata.openapi.schema.observations;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"name","url"})
@Schema(description="Observation data in JSON format.")
public class ObservationJSON {
	@Schema(description="observation name", example="groundwater-levels")
	public String getName() {
		return null;
	}

	@Schema(description="Href link to the data.")
	public String getUrl() {
		return null;
	}

}