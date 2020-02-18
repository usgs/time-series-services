package gov.usgs.wma.waterdata.geojson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"type","id","geometry","properties"})
public class GeoJSON {

	@Schema(description="The type of this object - Currently only 'Feature' objects are produced.", example="Feature")
	public String getType() {
		return null;
	}
	public String getId() {
		return null;
	}
	public Geometry getGeometry() {
		return null;
	}
	public Properties getProperties() {
		return null;
	}
}
