package gov.usgs.wma.waterdata.openapi.responses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import gov.usgs.wma.waterdata.OgcException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
		responseCode = "200",
		description = Http200Response.HTTP_400_DESCRIPTION,
		content = @Content(schema = @Schema(implementation = OgcException.class)))
public @interface Http200Response {
	public static final String HTTP_400_DESCRIPTION = "A query parameter has an invalid value.";
}
