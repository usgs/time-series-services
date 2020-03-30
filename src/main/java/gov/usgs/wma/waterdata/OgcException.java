package gov.usgs.wma.waterdata;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * pojo to hold an instance of an ogc exception:
 *   http://schemas.opengis.net/ogcapi/features/part1/1.0/openapi/schemas/exception.yaml
 */
@Component
@JsonPropertyOrder(value={"code","description"})
public class OgcException {

	@JsonProperty("code")
	@NotNull
	protected String code;

	@JsonProperty("description")
	protected String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}