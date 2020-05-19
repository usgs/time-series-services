package gov.usgs.wma.waterdata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Application Version", description="Display")
@RestController
public class RedirectController {

	@Value("${site.url.base}")
	private String serverUrl;

	@Value("${swagger.apiDocsUrl}")
	private String swaggerApiDocsUrl;

	@Operation(
			description="Return the web service version information.",
			responses= {
					@ApiResponse(content=@Content(schema=@Schema(implementation=Version.class)))
			})
	@GetMapping(
			value="version",
			produces=MediaType.APPLICATION_JSON_VALUE
	)
	public RedirectView getVersion(RedirectAttributes attributes) {
		return new RedirectView(serverUrl + "about/info", false, true);
	}

	@GetMapping(value="swagger")
	@Hidden
	public RedirectView getSwagger() {
		return new RedirectView(serverUrl + "swagger-ui/index.html?url=" + swaggerApiDocsUrl, false, true);
	}
}