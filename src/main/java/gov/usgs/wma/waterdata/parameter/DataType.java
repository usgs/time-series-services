package gov.usgs.wma.waterdata.parameter;

public enum DataType {
	statistical_time_series,
	discrete;

	public boolean isStatisticalTimeSeries() {
		return this.equals(statistical_time_series);
	}
	public boolean isDiscrete() {
		return this.equals(discrete);
	}
}
