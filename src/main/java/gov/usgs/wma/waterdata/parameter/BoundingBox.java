package gov.usgs.wma.waterdata.parameter;

public class BoundingBox {

	private String single;
	private String north;
	private String south;
	private String east;
	private String west;

	public BoundingBox(String single) {
		this.single = single;
		if (null != single) {
			String[] parts = single.split(",");
			if (4 == parts.length) {
				this.west = parts[0];
				this.south = parts[1];
				this.east = parts[2];
				this.north = parts[3];
			}
		}
	}

	public String getSingle() {
		return single;
	}

	public String getNorth() {
		return north;
	}

	public String getSouth() {
		return south;
	}

	public String getEast() {
		return east;
	}

	public String getWest() {
		return west;
	}

	@Override
	public String toString() {
		return this.single;
	}

}
