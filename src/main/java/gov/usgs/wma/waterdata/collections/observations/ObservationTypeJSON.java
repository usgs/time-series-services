package gov.usgs.wma.waterdata.collections.observations;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"name","url"})
@Schema(description="Observation dataset in JSON format.")
public class ObservationTypeJSON {
	@Schema(description="observation dataset name", example="statistical-time-series")
	public String getName() {
		return null;
	}

	@Schema(description="Href link to the dataset.")
	public String getUrl() {
		return null;
	}

}