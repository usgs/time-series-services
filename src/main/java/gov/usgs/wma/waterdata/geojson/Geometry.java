package gov.usgs.wma.waterdata.geojson;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"type","coordinates"})
@Schema(description="A Geometry object represents points, curves, and surfaces in coordinate space.")
public class Geometry {
	@Schema(description="The type of this Geometry object - Currently only 'Point' (longitude, latitude) objects are produced.", example="Point")
	public String getType() {
		return "Point";
	}
	public BigDecimal[] getCoordinates() {
		return null;
	}
}
