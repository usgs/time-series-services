package gov.usgs.wma.waterdata.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import gov.usgs.wma.waterdata.parameter.BoundingBox;
import gov.usgs.wma.waterdata.validation.BBoxValidator;

class BoundingBoxTest {
	@Test
	public void nullSingleTest() {
		BoundingBox bbox = new BoundingBox(null);
		assertNull(bbox.getSingle());
		assertNull(bbox.getWest());
		assertNull(bbox.getSouth());
		assertNull(bbox.getEast());
		assertNull(bbox.getNorth());
	}

	@Test
	public void noDelimitorTest() {
		BoundingBox bbox = new BoundingBox("");
		assertEquals("", bbox.toString());
		assertEquals("", bbox.getSingle());
		assertNull(bbox.getWest());
		assertNull(bbox.getSouth());
		assertNull(bbox.getEast());
		assertNull(bbox.getNorth());
	}

	@Test
	public void oneFieldTest() {
		BoundingBox bbox = new BoundingBox("160.6");
		assertEquals("160.6", bbox.toString());
		assertEquals("160.6", bbox.getSingle());
		assertNull(bbox.getWest());
		assertNull(bbox.getSouth());
		assertNull(bbox.getEast());
		assertNull(bbox.getNorth());
	}

	@Test
	public void twoFieldsTest() {
		BoundingBox bbox = new BoundingBox("160.6,-55.95");
		assertEquals("160.6,-55.95", bbox.toString());
		assertEquals("160.6,-55.95", bbox.getSingle());
		assertNull(bbox.getWest());
		assertNull(bbox.getSouth());
		assertNull(bbox.getEast());
		assertNull(bbox.getNorth());
	}

	@Test
	public void threeFieldsTest() {
		BoundingBox bbox = new BoundingBox("160.6,-55.95,-170");
		assertEquals("160.6,-55.95,-170", bbox.toString());
		assertEquals("160.6,-55.95,-170", bbox.getSingle());
		assertNull(bbox.getWest());
		assertNull(bbox.getSouth());
		assertNull(bbox.getEast());
		assertNull(bbox.getNorth());
	}

	@Test
	public void fourFieldsTest() {
		BoundingBox bbox = new BoundingBox("160.6,-55.95,-170,-25.89");
		assertEquals("160.6,-55.95,-170,-25.89", bbox.toString());
		assertEquals("160.6,-55.95,-170,-25.89", bbox.getSingle());
		assertEquals("160.6", bbox.getWest());
		assertEquals("-55.95", bbox.getSouth());
		assertEquals("-170", bbox.getEast());
		assertEquals("-25.89", bbox.getNorth());
	}

	@Test
	public void extraFieldsTest() {
		BoundingBox bbox = new BoundingBox("160.6,-55.95,-170,-25.89,90");
		assertEquals("160.6,-55.95,-170,-25.89,90", bbox.toString());
		assertEquals("160.6,-55.95,-170,-25.89,90", bbox.getSingle());
		assertNull(bbox.getWest());
		assertNull(bbox.getSouth());
		assertNull(bbox.getEast());
		assertNull(bbox.getNorth());
	}

	@Test
	public void boundingBoxValidatorNullSingleTest() {
		BoundingBox bbox = new BoundingBox(null);
		BBoxValidator validator = new BBoxValidator();
		ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
		boolean valid = validator.isValid(bbox, context);
		assertFalse(valid);
	}

}