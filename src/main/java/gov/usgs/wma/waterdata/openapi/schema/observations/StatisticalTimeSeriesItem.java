package gov.usgs.wma.waterdata.openapi.schema.observations;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Statistical time series description entry.")
public class StatisticalTimeSeriesItem {
	public static final String BEST_DESC = "The time series deemed the \"best\" of those currently served at this station and "
			+ "has the longest-running record out of many parameter and statistic combinations. "
			+ "The \"best\" time series is intended to help users identify the largest "
			+ "and most reliable data set at a site when only one value per day is needed.";

	@Schema(description = "The time series unique identifier.", example = "AGENCY-FEATURE-TIMESERIES")
    public String getId() {
		return null;
	}

	@Schema(description = "Information about the parameter and statistical codes", example = "Depth to water level, ft below land surface, daily maximum")
    public String getDescription() {
		return null;
	}

	@Schema(description = "The five digit parameter unique identifier.", example = "72019")
    public String getParameterCode() {
		return null;
	}

	@Schema(description = "The five digit statistic code unique identifier.", example = "00002")
    public String getStatisticCode() {
		return null;
	}

	@Schema(description = BEST_DESC, example = "true")
	public Boolean getBest() {
		return null;
	}

	@Schema(description = "URL to download the statistical time series data.")
    public String getUrl() {
		return null;
	}

}
