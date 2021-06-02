package gov.usgs.wma.waterdata.domain;

import java.time.ZonedDateTime;

public class WaterMLPoint {

	ZonedDateTime resultDateTimeUTC;
	Double resultValue;
	String resultUnit;
	Double resultAccuracy;
	String resultAccuracyUnit;
	String verticalDatum;
	String pcode;
	/* String comment;  We don't have any comments - what about approval levels? */
	String qualifiers;	//This may be a list.  Is this the same as status and approval?
}
