package gov.usgs.wma.waterdata.domain;

public class Geometry {

	private String type;
	private Coordinates coordinates;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Coordinates getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}
}
