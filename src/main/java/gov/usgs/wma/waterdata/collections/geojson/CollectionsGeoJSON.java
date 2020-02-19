package gov.usgs.wma.waterdata.collections.geojson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"collections","links"})
@Schema(description="Collections MetaData in GeoJSON format.")
public class CollectionsGeoJSON {
	@Schema(description="List of collections available.")
	public CollectionGeoJSON[] getCollections() {
		return null;
	}
	@Schema(description="Reference links for this collection.")
	public LinkGeoJSON[] getLinks() {
		return null;
	}
}
