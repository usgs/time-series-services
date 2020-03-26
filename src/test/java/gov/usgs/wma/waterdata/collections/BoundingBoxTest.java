package gov.usgs.wma.waterdata.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import gov.usgs.wma.waterdata.parameter.BoundingBox;

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
		assertEquals("", bbox.getSingle());
		assertNull(bbox.getWest());
		assertNull(bbox.getSouth());
		assertNull(bbox.getEast());
		assertNull(bbox.getNorth());
	}

	@Test
	public void oneFieldTest() {
		BoundingBox bbox = new BoundingBox("160.6");
		assertEquals("160.6", bbox.getSingle());
		assertNull(bbox.getWest());
		assertNull(bbox.getSouth());
		assertNull(bbox.getEast());
		assertNull(bbox.getNorth());
	}

	@Test
	public void twoFieldsTest() {
		BoundingBox bbox = new BoundingBox("160.6,-55.95");
		assertEquals("160.6,-55.95", bbox.getSingle());
		assertNull(bbox.getWest());
		assertNull(bbox.getSouth());
		assertNull(bbox.getEast());
		assertNull(bbox.getNorth());
	}

	@Test
	public void threeFieldsTest() {
		BoundingBox bbox = new BoundingBox("160.6,-55.95,-170");
		assertEquals("160.6,-55.95,-170", bbox.getSingle());
		assertNull(bbox.getWest());
		assertNull(bbox.getSouth());
		assertNull(bbox.getEast());
		assertNull(bbox.getNorth());
	}

	@Test
	public void fourFieldsTest() {
		BoundingBox bbox = new BoundingBox("160.6,-55.95,-170,-25.89");
		assertEquals("160.6,-55.95,-170,-25.89", bbox.getSingle());
		assertEquals("160.6", bbox.getWest());
		assertEquals("-55.95", bbox.getSouth());
		assertEquals("-170", bbox.getEast());
		assertEquals("-25.89", bbox.getNorth());
	}

	@Test
	public void extraFieldsTest() {
		BoundingBox bbox = new BoundingBox("160.6,-55.95,-170,-25.89,90");
		assertEquals("160.6,-55.95,-170,-25.89,90", bbox.getSingle());
		assertNull(bbox.getWest());
		assertNull(bbox.getSouth());
		assertNull(bbox.getEast());
		assertNull(bbox.getNorth());
	}

}