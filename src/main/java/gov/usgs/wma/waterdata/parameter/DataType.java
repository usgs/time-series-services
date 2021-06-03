package gov.usgs.wma.waterdata.parameter;

public enum DataType {
	any,
	statistical_time_series;

	public boolean isStatisticalTimeSeries() {
		return this.equals(any) || this.equals(statistical_time_series);
	}
}
