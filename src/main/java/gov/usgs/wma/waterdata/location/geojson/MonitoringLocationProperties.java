package gov.usgs.wma.waterdata.location.geojson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import gov.usgs.wma.waterdata.geojson.Properties;

@JsonPropertyOrder(value={"samplingFeatureName"})
public class MonitoringLocationProperties extends Properties {

}
