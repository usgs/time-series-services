package gov.usgs.wma.waterdata.location.geojson;

import gov.usgs.wma.waterdata.geojson.GeoJSON;
import gov.usgs.wma.waterdata.geojson.Geometry;
import gov.usgs.wma.waterdata.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Monitoring Location MetaData in GeoJSON format.")
public class MonitoringLocationGeoJSON extends GeoJSON {

	@Schema(description="Monitoring Location Identification Number.", example="USGS-07227448")
	public String getId() {
		return id;
	}
	@Schema(description="The geometry associated with this Monitoring Location.")
	public Geometry getGeometry() {
		return geometry;
	}
	@Schema(description="The properties associated with this Monitoring Location.")
	public Properties getProperties() {
		return properties;
	}
}
