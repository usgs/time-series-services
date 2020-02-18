package gov.usgs.wma.waterdata.collections.geojson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"id","itemType","title","description", "keywords", "extent", "links"})
@Schema(description="Collection MetaData in GeoJSON format.")
public class CollectionGeoJSON {
	public static final String EXAMPLE_TITLE = "NWIS Monitoring Locations";
	public static final String EXAMPLE_DESC = "USGS water monitoring locations managed in the National Water Information System";

	@Schema(description="Collection Identifier.", example="monitoring-locations")
	public String getId() {
		return null;
	}
	@Schema(description="The type of items in this collection - Currently only 'Feature' objects are produced.", example="Feature")
	public String getItemType() {
		return null;
	}
	@Schema(description="human readable title of the collection.", example=EXAMPLE_TITLE)
	public String getTitle() {
		return null;
	}
	@Schema(description="a description of the features in the collection.", example=EXAMPLE_DESC)
	public String getDescription() {
		return null;
	}
	@Schema(description="informative words describing the collection content or subject area.", example="")
	public String[] getKeywords() {
		return null;
	}
	@Schema(description="The extent of the features in the collection. Only spatial extents are specified.")
	public ExtentGeoJSON getExtent() {
		return null;
	}
	@Schema(description="The links specified in the collection.")
	public LinkGeoJSON[] getLinks() {
		return null;
	}
}
