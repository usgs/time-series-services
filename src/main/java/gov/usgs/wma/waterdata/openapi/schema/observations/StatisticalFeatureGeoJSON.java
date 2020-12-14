package gov.usgs.wma.waterdata.openapi.schema.observations;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gov.usgs.wma.waterdata.openapi.schema.geojson.Geometry;
import io.swagger.v3.oas.annotations.media.Schema;



@JsonPropertyOrder(value={"type","id","geometry","properties","timeSeries"})
@Schema(description="Statistical Time Series Observation datasets available for a feature.")
public class StatisticalFeatureGeoJSON  {
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
	public StatisticalFeatureProperties getProperties() {
		return null;
	}
	@Schema(description="The statistical time series available for this Feature.")
	public StatisticalTimeSeriesItem[] getTimeSeries() {
		return null;
	}

}
