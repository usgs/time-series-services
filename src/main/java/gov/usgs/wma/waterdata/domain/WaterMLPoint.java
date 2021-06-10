package gov.usgs.wma.waterdata.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "point", namespace = WaterML2.NAMESPACE)
public class WaterMLPoint {

	@JacksonXmlElementWrapper(localName = "qualifier", namespace = WaterML2.NAMESPACE)
	@JacksonXmlProperty(localName = "Category", namespace = SensorWebEnablement2.NAMESPACE)
	List<String> qualifiers = new ArrayList();	//This may be a list.  Is this the same as status and approval?

	LocalDateTime resultDateTimeUTC;
	Double resultValue;
	String resultUnit;
	Double resultAccuracy;
	String resultAccuracyUnit;
	String verticalDatum;
	String pcode;


	@JacksonXmlProperty(namespace = ObsAndMeasure.NAMESPACE)
	public String myOM;

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

	public Double getResultAccuracy() {
		return resultAccuracy;
	}

	public void setResultAccuracy(Double resultAccuracy) {
		this.resultAccuracy = resultAccuracy;
	}

	public String getResultAccuracyUnit() {
		return resultAccuracyUnit;
	}

	public void setResultAccuracyUnit(String resultAccuracyUnit) {
		this.resultAccuracyUnit = resultAccuracyUnit;
	}

	public String getVerticalDatum() {
		return verticalDatum;
	}

	public void setVerticalDatum(String verticalDatum) {
		this.verticalDatum = verticalDatum;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public List<String> getQualifiers() {
		return qualifiers;
	}

	public void addQualifier(String qualifier) {
		qualifiers.add(qualifier);
	}

	public LocalDateTime getResultDateTimeUTC() {
		return resultDateTimeUTC;
	}

	public void setResultDateTimeUTC(LocalDateTime resultDateTimeUTC) {
		this.resultDateTimeUTC = resultDateTimeUTC;
	}
}
