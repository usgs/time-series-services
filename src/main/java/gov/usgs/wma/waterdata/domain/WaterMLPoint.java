package gov.usgs.wma.waterdata.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// TODO: need root elements for child tags
@JacksonXmlRootElement(localName = "point", namespace = WaterML2.NAMESPACE)
public class WaterMLPoint {

	@JacksonXmlElementWrapper(localName = "qualifier", namespace = WaterML2.NAMESPACE)
	@JacksonXmlProperty(localName = "Category", namespace = SensorWebEnablement2.NAMESPACE)
	List<String> qualifiers = new ArrayList<>();	//This may be a list.  Is this the same as status and approval?

	// TODO: MyBatis does not easily handle reading the jsonb column without custom type handler. Until the json array is
	// directly mapped in sql, we'll read the field in raw and convert.
	String qualifiersAsJson;

	String monLocIdentifier;
	String status;
	LocalDateTime resultDateTimeUTC;
	Double resultValue;
	String resultUnit;
	String pcode;
	String pcodeDesc;


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

	public String getPcode() { return pcode; }

	public void setPcode(String pcode) { this.pcode = pcode; }

	public String getPcodeDesc() { return pcodeDesc; }

	public void setPcodeDesc(String pcodeDesc) { this.pcodeDesc = pcodeDesc; }

	public List<String> getQualifiers() {
		return qualifiers;
	}

	public void addQualifier(String qualifier) {
		qualifiers.add(qualifier);
	}

	public String getQualifiersAsJson() {
		return qualifiersAsJson;
	}

	public void setQualifiersAsJson(String qualifiersAsJson) {
		this.qualifiersAsJson = qualifiersAsJson;
	}

	public String getMonLocIdentifier() {
		return monLocIdentifier;
	}

	public void setMonLocIdentifier(String monLocIdentifier) {
		this.monLocIdentifier = monLocIdentifier;
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
