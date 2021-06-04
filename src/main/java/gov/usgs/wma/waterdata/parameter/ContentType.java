package gov.usgs.wma.waterdata.parameter;

public enum ContentType {
	json,
	waterml;

	public boolean isJson() {
		return this.equals(json);
	}

	public boolean isWaterML() {
		return this.equals(waterml);
	}
}
