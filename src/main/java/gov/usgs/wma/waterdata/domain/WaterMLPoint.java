package gov.usgs.wma.waterdata.domain;

import java.time.ZonedDateTime;

public class WaterMLPoint {

	ZonedDateTime measurmentTime;
	Double value;
	String unit;
	Double accuracy;
	String accuracyUnit;
	String datum;
	String pcode;
	String comment;
	String qualifier;	//This may be a list.  Is this the same as status and approval?
}
