package gov.usgs.wma.waterdata.geojson;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Additional properties.")
public class Properties {

	@Schema(description="The name of the Monitoring Location.", example="Punta De Agua Ck nr Channing, TX")
	private String samplingFeatureName;
	public String getSamplingFeatureName() {
		return samplingFeatureName;
	}
	public void setSamplingFeatureName(String samplingFeatureName) {
		this.samplingFeatureName = samplingFeatureName;
	}
}
