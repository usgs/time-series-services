package gov.usgs.wma.waterdata;

import io.swagger.v3.oas.annotations.media.Schema;

public class Version {

	public Build build;

	public class Build {
		@Schema(example="time-series-services")
		public String artifact;
		@Schema(example="time-series-services")
		public String name;
		@Schema(example="2019-12-03T17:43:14.683Z")
		public String time;
		@Schema(example="0.2.0-SNAPSHOT")
		public String version;
		@Schema(example="gov.usgs.wma.waterdata")
		public String group;
	}
}
