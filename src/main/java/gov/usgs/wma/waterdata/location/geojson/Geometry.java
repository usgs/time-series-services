package gov.usgs.wma.waterdata.location.geojson;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"type","coordinates"})
@Schema(description="A Geometry object represents points, curves, and surfaces in coordinate space.")
public class Geometry {
	@Schema(hidden=true)
	private BigDecimal longitude;
	@Schema(hidden=true)
	private BigDecimal latitude;
	@Schema(description="The type of this Geometry object - Currently only 'Point' (longitude, latitude) objects are produced.", example="Point")
	public String getType() {
		return "Point";
	}
	public BigDecimal[] getCoordinates() {
		return new BigDecimal[] {longitude,latitude};
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
}
