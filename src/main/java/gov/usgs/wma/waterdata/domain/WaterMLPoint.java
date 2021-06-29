package gov.usgs.wma.waterdata.domain;

import java.time.LocalDateTime;

public class WaterMLPoint {

	// TODO: MyBatis does not easily handle reading the jsonb column without custom type handler. Until the json array is
	// directly mapped in sql, we'll read the field in raw and convert.
	String qualifiersAsJson;
	String approvalsAsJson;

	String featureId;
	String monLocReference;
	String siteName;

	String interpolationTypeRef;
	String interpolationTypeDesc;
	String phenomenonTimeStart;
	String phenomenonTimeEnd;
	String measurementTimeseriesId;
	String status;
	String resultDate;
	LocalDateTime resultDateTimeUTC;
	Double resultValue;
	String resultUnit;
	String resultUnitReference;
	String pcode;
	String pcodeDesc;
	String pcodeReference;
	String statisticDesc;
	String statisticReference;

	public String getApprovalsAsJson() {
		return approvalsAsJson;
	}

	public void setApprovalsAsJson(String approvalsAsJson) {
		this.approvalsAsJson = approvalsAsJson;
	}

	public String getPhenomenonTimeStart() {
		return phenomenonTimeStart;
	}

	public void setPhenomenonTimeStart(String phenomenonTimeStart) {
		this.phenomenonTimeStart = phenomenonTimeStart;
	}

	public String getPhenomenonTimeEnd() {
		return phenomenonTimeEnd;
	}

	public void setPhenomenonTimeEnd(String phenomenonTimeEnd) {
		this.phenomenonTimeEnd = phenomenonTimeEnd;
	}

	public String getMeasurementTimeseriesId() {
		return measurementTimeseriesId;
	}

	public void setMeasurementTimeseriesId(String measurementTimeseriesId) {
		this.measurementTimeseriesId = measurementTimeseriesId;
	}

	public String getResultDate() {
		return resultDate;
	}

	public void setResultDate(String resultDate) {
		this.resultDate = resultDate;
	}

	public String getResultUnitReference() {
		return resultUnitReference;
	}

	public void setResultUnitReference(String resultUnitReference) {
		this.resultUnitReference = resultUnitReference;
	}

	public String getPcodeReference() {
		return pcodeReference;
	}

	public void setPcodeReference(String pcodeReference) {
		this.pcodeReference = pcodeReference;
	}

	public String getStatisticDesc() {
		return statisticDesc;
	}

	public void setStatisticDesc(String statisticDesc) {
		this.statisticDesc = statisticDesc;
	}

	public String getStatisticReference() {
		return statisticReference;
	}

	public void setStatisticReference(String statisticReference) {
		this.statisticReference = statisticReference;
	}

	public Double getResultValue() {
		return resultValue;
	}

	public void setResultValue(Double resultValue) {
		this.resultValue = resultValue;
	}

	public String getResultUnit() {
		return resultUnit;
	}

	public void setResultUnit(String resultUnit) {
		this.resultUnit = resultUnit;
	}

	public String getPcode() { return pcode; }

	public void setPcode(String pcode) { this.pcode = pcode; }

	public String getPcodeDesc() { return pcodeDesc; }

	public void setPcodeDesc(String pcodeDesc) { this.pcodeDesc = pcodeDesc; }

	public String getQualifiersAsJson() {
		return qualifiersAsJson;
	}

	public void setQualifiersAsJson(String qualifiersAsJson) {
		this.qualifiersAsJson = qualifiersAsJson;
	}

	public String getFeatureId() {
		return featureId;
	}

	public String getMonLocReference() {
		return monLocReference;
	}

	public void setMonLocReference(String monLocReference) {
		this.monLocReference = monLocReference;
	}

	public String getSiteName() {
		return siteName;
	}

	public String getInterpolationTypeRef() {
		return interpolationTypeRef;
	}

	public void setInterpolationTypeRef(String interpolationTypeRef) {
		this.interpolationTypeRef = interpolationTypeRef;
	}

	public String getInterpolationTypeDesc() {
		return interpolationTypeDesc;
	}

	public void setInterpolationTypeDesc(String interpolationTypeDesc) {
		this.interpolationTypeDesc = interpolationTypeDesc;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getResultDateTimeUTC() {
		return resultDateTimeUTC;
	}

	public void setResultDateTimeUTC(LocalDateTime resultDateTimeUTC) {
		this.resultDateTimeUTC = resultDateTimeUTC;
	}
}
