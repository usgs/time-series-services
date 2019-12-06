package gov.usgs.wma.waterdata.location.geojson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"type","id","geometry","properties"})
@Schema(description="Monitoring Location MetaData in GeoJSON format.")
public class GeoJSON {

	@Schema(description="Monitoring Location Identification Number.", example="USGS-07227448")
	private String id;
	@Schema(description="The geometry associated with this Monitoring Location.")
	private Geometry geometry;
	@Schema(description="The properties associated with this Monitoring Location.")
	private Properties properties;
	@Schema(description="The type of this object - Currently on 'Feature' objects are produced.", example="Feature")
	public String getType() {
		return "Feature";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Geometry getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
