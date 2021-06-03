package gov.usgs.wma.waterdata.parameter;

import java.util.List;

public enum Domain {
	//any,
	groundwater_levels;

	public static boolean includesGroundWaterLevels(List<Domain> domains) {
		return domains != null && domains.contains(groundwater_levels);
	}
}
