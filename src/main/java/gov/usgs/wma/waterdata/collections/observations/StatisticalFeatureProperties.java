package gov.usgs.wma.waterdata.collections.observations;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Statistical Feature properties.")
public class StatisticalFeatureProperties {
	@Schema(description = "The features descriptive name.", example = "Yahara River at Main St.")
	public String getSamplingFeatureName() {
		return null;
	}
}
