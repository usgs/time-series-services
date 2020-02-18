package gov.usgs.wma.waterdata.collections.geojson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"spatial"})
public class ExtentGeoJSON {
	@Schema(description="The spatial extent of the features in the collection.")
	public SpatialGeoJSON getSpatial() {
		return null;
	}
}
