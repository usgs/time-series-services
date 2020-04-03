package gov.usgs.wma.waterdata.openapi.schema.location;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import gov.usgs.wma.waterdata.openapi.schema.geojson.Properties;

@JsonPropertyOrder(value={"samplingFeatureName"})
public class MonitoringLocationProperties extends Properties {

}
