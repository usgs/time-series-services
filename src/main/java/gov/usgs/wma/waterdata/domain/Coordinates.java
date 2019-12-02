package gov.usgs.wma.waterdata.domain;

import java.math.BigDecimal;

public class Coordinates {

	private BigDecimal[] values;
	public BigDecimal[] getValues() {
		return values;
	}
	public void setValues(BigDecimal[] values) {
		this.values = values;
	}
}
