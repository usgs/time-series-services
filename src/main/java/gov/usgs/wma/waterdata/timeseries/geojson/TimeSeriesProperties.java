package gov.usgs.wma.waterdata.timeseries.geojson;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import gov.usgs.wma.waterdata.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder(value={"observationType","phenomenonTimeStart","phenomenonTimeEnd",
		"observedPropertyName","observedPropertyReference","samplingFeatureName",
		"statistic","statisticReference","timeStep","unitOfMeasureName",
		"unitOfMeasureReference","result","nilReason",
		"approvals","qualifiers","grades"})
public class TimeSeriesProperties extends Properties {

	@Schema(description="The type of this observation - Currently only 'MeasureTimeseriesObservation' objects are produced.", example="MeasureTimeseriesObservation")
	public String getObservationType() {
		return null;
	}
	@Schema(description="The OMSF phenomenonTime - Begin Date of this time series data.", example="2019-11-09")
	public LocalDate getPhenomenonTimeStart() {
		return null;
	}
	@Schema(description="The OMSF phenomenonTime - End Date of this time series data.", example="2019-11-15")
	public LocalDate getPhenomenonTimeEnd() {
		return null;
	}
	@Schema(description="The OMSF observedProperty - Property being observed.", example="Depth to water level, ft below land surface")
	public String getObservedPropertyName() {
		return null;
	}
	@Schema(description="The OMSF observedPropertyReference - A URI pointing to the remote definition of the Property being observed.", example="https://waterdata.usgs.gov/nwisweb/rdf?parmCd=72019")
	public String getObservedPropertyReference() {
		return null;
	}
	@Schema(description="The statistic represented in thes time series.", example="DAILY MAXIMUM VALUES")
	public String getStatistic() {
		return null;
	}
	@Schema(description=" A URI pointing to the remote definition of the statistic represented in thes time series.", example="https://waterdata.usgs.gov/nwisweb/rdf?statCd=00001")
	public String getStatisticReference() {
		return null;
	}
	@Schema(description="The OMSF timeStep - An Array of Dates for the individual time series observations.")
	public LocalDate[] getTimeStep() {
		return null;
	}
	@Schema(description="The OMSF result - Name of the Unit of Meassure.", example="ft")
	public String getUnitOfMeasureName() {
		return null;
	}
	@Schema(description="The OMSF result - A URI pointing to the remote definition of the Unit of Meassure.", example="http://www.opengis.net/def/uom/UCUM/ft")
	public String getUnitOfMeasureReference() {
		return null;
	}
	@Schema(description="The OMSF result - An Array of actual measurements.")
	public String[] getResult() {
		return null;
	}
	@Schema(description="An Array of reasons a result is masked.")
	public String[] getNilReason() {
		return null;
	}
	@Schema(description="An Array of approval(s) for the result. This is an array of size 1 to many per result")
	public String[] getApprovals() {
		return null;
	}
	@Schema(description="An Array of qualifiers(s) for the result. This is null or an array of size 1 to many per result")
	public String[] getQualifiers() {
		return null;
	}
	@Schema(description="An Array of grade(s) for the result. This is an array of size 1 to many per result")
	public String[] getGrades() {
		return null;
	}
}
