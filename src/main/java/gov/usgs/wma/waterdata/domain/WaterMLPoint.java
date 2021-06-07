package gov.usgs.wma.waterdata.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.*;

@JacksonXmlRootElement(localName = "point", namespace = "http://www.opengis.net/waterml/2.0")
public class WaterMLPoint {

	LocalDateTime resultDateTimeUTC;
	Double resultValue;
	String resultUnit;
	Double resultAccuracy;
	String resultAccuracyUnit;
	String verticalDatum;
	String pcode;
	/* String comment;  We don't have any comments - what about approval levels? */
	String qualifiers;	//This may be a list.  Is this the same as status and approval?

	@JacksonXmlProperty(namespace = "http://www.opengis.net/om/2.0")
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

	public String getQualifiers() {
		return qualifiers;
	}

	public void setQualifiers(String qualifiers) {
		this.qualifiers = qualifiers;
	}

	public LocalDateTime getResultDateTimeUTC() {
		return resultDateTimeUTC;
	}

	public void setResultDateTimeUTC(LocalDateTime resultDateTimeUTC) {
		this.resultDateTimeUTC = resultDateTimeUTC;
	}
}
