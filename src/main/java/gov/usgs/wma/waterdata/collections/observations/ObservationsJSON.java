package gov.usgs.wma.waterdata.collections.observations;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"observationTypes"})
@Schema(description="Observation datasets in JSON format.")
public class ObservationsJSON {
	@Schema(description="available dataSets")
	public ObservationTypeJSON[] getObservationTypes() {
		return null;
	}
}