package gov.usgs.wma.waterdata.collections.geojson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value = { "agency", "monitoringLocationNumber", "monitoringLocationName", "monitoringLocationType",
		"district", "state", "county", "country", "monitoringLocationAltitudeLandSurface", "altitudeMethod",
		"altitudeAccuracy", "altitudeDatum", "nationalAquifer", "localAquifer", "localAquiferType", "wellDepth",
		"holeDepth", "holeDepthSource", "agencyCode", "districtCode", "stateFIPS", "countyFIPS", "countryFIPS",
		"hydrologicUnit", "monitoringLocationUrl" })
@Schema(description="Feature properties.")
public class FeatureProperties {
	@Schema(description = "agency", example = "U.S. Geological Survey")
	public String getAgency() {
		return null;
	}
	@Schema(description = "monitoringLocationNumber", example = "07227448")
	public String getMonitoringLocationNumber() {
		return null;
	}
	@Schema(description = "monitoringLocationName", example = "Punta De Agua Ck nr Channing, TX")
	public String getMonitoringLocationName() {
		return null;
	}
	@Schema(description = "monitoringLocationType", example = "Steam")
	public String getMonitoringLocationType() {
		return null;
	}
	@Schema(description = "district", example = "Texas")
	public String getDistrict() {
		return null;
	}
	@Schema(description = "state", example = "Texas")
	public String getState() {
		return null;
	}
	@Schema(description = "county", example = "Texas")
	public String getCounty() {
		return null;
	}
	@Schema(description = "country", example = "US")
	public String getCountry() {
		return null;
	}
	@Schema(description = "monitoringLocationAltitudeLandSurface", example = "3407")
	public String getMonitoringLocationAltitudeLandSurface() {
		return null;
	}
	@Schema(description = "altitudeMethod", example = "Interpolated from topographic map.")
	public String getAltitudeMethod() {
		return null;
	}
	@Schema(description = "altitudeDatum", example = "National Geodetic Vertical Datum of 1929")
	public String getAltitudeDatum() {
		return null;
	}
	@Schema(description = "nationalAquifer", example = "Basin and Range carbonate-rock aquifers")
	public String getNationalAquifer() {
		return null;
	}
	@Schema(description = "localAquifer", example = "Eagle Ford Shale")
	public String getLocalAquifer() {
		return null;
	}
	@Schema(description = "localAquiferType", example = "Confined single aquifer")
	public String getLocalAquiferType() {
		return null;
	}
	@Schema(description = "wellDepth", example = "100.13")
	public String getWellDepth() {
		return null;
	}
	@Schema(description = "holeDepth", example = "10.0")
	public String getHoleDepth() {
		return null;
	}
	@Schema(description = "holeDepthSource", example = "D")
	public String getHoleDepthSource() {
		return null;
	}
	@Schema(description = "agencyCode", example = "USGS")
	public String getAgencyCode() {
		return null;
	}
	@Schema(description = "districtCode", example = "48")
	public String getDistrictCode() {
		return null;
	}
	@Schema(description = "stateFIPS", example = "US:48")
	public String getStateFIPS() {
		return null;
	}
	@Schema(description = "countyFIPS", example = "US:48:205")
	public String getCountyFIPS() {
		return null;
	}
	@Schema(description = "countryFIPS", example = "US")
	public String getCountryFIPS() {
		return null;
	}
	@Schema(description = "hydrologicUnit", example = "110901030506")
	public String getHydrologicUnit() {
		return null;
	}
	@Schema(description = "monitoringLocationUrl", example = "https://waterdata.usgs.gov/monitoring-location/07227448")
	public String getMonitoringLocationUrl() {
		return null;
	}
}