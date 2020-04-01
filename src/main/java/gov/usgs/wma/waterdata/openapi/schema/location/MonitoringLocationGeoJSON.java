package gov.usgs.wma.waterdata.openapi.schema.location;

import gov.usgs.wma.waterdata.openapi.schema.geojson.GeoJSON;
import gov.usgs.wma.waterdata.openapi.schema.geojson.Geometry;
import gov.usgs.wma.waterdata.openapi.schema.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Monitoring Location MetaData in GeoJSON format.")
public class MonitoringLocationGeoJSON extends GeoJSON {

	@Schema(description="Monitoring Location Identification Number.", example="USGS-07227448")
	public String getId() {
		return null;
	}
	@Schema(description="The geometry associated with this Monitoring Location.")
	public Geometry getGeometry() {
		return null;
	}
	@Schema(description="The properties associated with this Monitoring Location.")
	public Properties getProperties() {
		return null;
	}
}
