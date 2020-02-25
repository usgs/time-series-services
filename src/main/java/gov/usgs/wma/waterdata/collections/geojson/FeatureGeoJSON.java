package gov.usgs.wma.waterdata.collections.geojson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import gov.usgs.wma.waterdata.geojson.Geometry;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value = { "type", "id", "geometry", "properties"})
@Schema(description = "Feature MetaData in GeoJSON format.")
public class FeatureGeoJSON {
	@Schema(description="The type of this object - Currently only 'Feature' objects are produced.", example="Feature")
	public String getType() {
		return null;
	}
	@Schema(description="Feature Identifier.", example="USGS-07227448")
	public String getId() {
		return null;
	}
	@Schema(description="The geometry associated with this Feature.")
	public Geometry getGeometry() {
		return null;
	}
	@Schema(description="The properties associated with this Feature.")
	public FeatureProperties getProperties() {
		return null;
	}
	
}
