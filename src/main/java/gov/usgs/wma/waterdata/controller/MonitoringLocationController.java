package gov.usgs.wma.waterdata.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.wma.waterdata.domain.GeoJSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Observations - Monitoring Location", description="Download osmf-json")
@RestController
public class MonitoringLocationController {

	@Operation(description="Return Data specific to the requested Monitoring Location.")
	@GetMapping(value="observations/monitoring-location/{monitoringLocationId}")
	public GeoJSON getMonitoringLocation(final @RequestParam(value="monitoringLocationId", required=true) String monitoringLocationId) {
		return new GeoJSON();
	}
}
