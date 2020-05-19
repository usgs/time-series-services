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
		responseCode = "404",
		description = Http404Response.HTTP_404_DESCRIPTION,
		content = @Content(schema = @Schema(implementation = OgcException.class)))
public @interface Http404Response {
	public static final String HTTP_404_DESCRIPTION = "The requested data was not found.";
}
