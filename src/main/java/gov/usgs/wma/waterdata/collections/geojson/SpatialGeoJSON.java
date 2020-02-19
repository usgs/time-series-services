package gov.usgs.wma.waterdata.collections.geojson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"bbox", "crs"})
public class SpatialGeoJSON {
	@Schema(description="A bounding box that describes the spatial extent of the dataset.", example="[-180.0, -90.0, 180.0, 90.0]")
	public Double[] getBbox() {
		return null;
	}
	@Schema(description="Coordinate reference system of the coordinates in the spatial extent (property `bbox`)",
			example="http://www.opengis.net/def/crs/OGC/1.3/CRS83")
	public String getCrs() {
		return null;
	}
}
