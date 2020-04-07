package gov.usgs.wma.waterdata.openapi.schema.collections;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value = { "type", "features", "links", "timeStamp" })
@Schema(description = "Collection features MetaData in GeoJSON format.")
public class FeatureCollectionGeoJSON {
	@Schema(description = "The GeoJSON type (FeatureCollection) represented by this schema.", example = "FeatureCollection")
	public String getType() {
		return null;
	}

	@Schema(description = "List of features.")
	public FeatureGeoJSON[] getFeatures() {
		return null;
	}

	@Schema(description = "Reference links for this feature collection.")
	public LinkGeoJSON[] getLinks() {
		return null;
	}

	@Schema(description = "The time stamp when the response was generated (RFC 3339 format).", example = "2018-04-03T14:52:23Z")
	public String getTimeStamp() {
		return null;
	}
}
