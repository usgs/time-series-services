package gov.usgs.wma.waterdata.collections.geojson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"type","rel","title","href"})
public class LinkGeoJSON {
	@Schema(description="The media type referenced url produces.", example="text/html")
	public String getType() {
		return null;
	}
	@Schema(description="The relationship of the url to referencing object.", example="canonical")
	public String getRel() {
		return null;
	}
	@Schema(description="human readable title describing this Link.", example="information")
	public String getTitle() {
		return null;
	}
	@Schema(description="The url specified in this Link.", example="https://waterdata.usgs.gov")
	public String getHref() {
		return null;
	}
}
