package gov.usgs.wma.waterdata.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import gov.usgs.wma.waterdata.parameter.BoundingBox;

public class BBoxValidator implements ConstraintValidator<BBox, String> {

	public static final BigDecimal MAX_LATITUDE = new BigDecimal(90);
	public static final BigDecimal MIN_LATITUDE = new BigDecimal(-90);
	public static final BigDecimal MAX_LONGITUDE = new BigDecimal(180);
	public static final BigDecimal MIN_LONGITUDE = new BigDecimal(-180);

	@Override
	public boolean isValid(String bboxSingleValue, ConstraintValidatorContext context) {
		boolean valid = false;
		if (null == bboxSingleValue) {
			valid = true;
		} else if (bboxSingleValue.split(",").length != 4) {
			valid = false;
		} else {
			try {
				BoundingBox value = new BoundingBox(bboxSingleValue);
				BigDecimal north = new BigDecimal(value.getNorth());
				BigDecimal south = new BigDecimal(value.getSouth());
				BigDecimal east = new BigDecimal(value.getEast());
				BigDecimal west = new BigDecimal(value.getWest());

				boolean latsValid = south.compareTo(MIN_LATITUDE) != -1
						&& south.compareTo(MAX_LATITUDE) != 1
						&& north.compareTo(MIN_LATITUDE) != -1
						&& north.compareTo(MAX_LATITUDE) != 1;
				boolean longsValid = west.compareTo(MIN_LONGITUDE) != -1
						&& west.compareTo(MAX_LONGITUDE) != 1
						&& east.compareTo(MIN_LONGITUDE) != -1
						&& east.compareTo(MAX_LONGITUDE) != 1;
				boolean crossCheckValid = west.compareTo(east) != 1
						&& south.compareTo(north) != 1;
				valid = latsValid && longsValid && crossCheckValid;
			} catch (NumberFormatException e) {
				valid = false;
			}
		}
		return valid;
	}

}
