package gov.usgs.wma.waterdata.timeseries.geojson;

import gov.usgs.wma.waterdata.geojson.GeoJSON;
import gov.usgs.wma.waterdata.geojson.Geometry;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Timeseries Data in GeoJSON format.")
public class TimeSeriesGeoJSON extends GeoJSON {

	@Schema(description="Monitoring Location Identification Number.", example="USGS-425607088173001-{{AQTS Time series GUID}}")
	public String getId() {
		return null;
	}
	@Schema(description="The geometry associated with the Monitoring Locationfor this Time Series.")
	public Geometry getGeometry() {
		return null;
	}
	@Schema(description="The properties associated with this Monitoring Location.")
	public TimeSeriesProperties getProperties() {
		return null;
	}
}
