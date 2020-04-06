package gov.usgs.wma.waterdata.springinit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	@Value("${site.url.base}")
	private String serverUrl;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.addServersItem(new Server().url(serverUrl))
				.components(new Components())
				.info(new Info().title("WDFN Observations Services API").description(
						"Documentation for the Water Data for the Nation - Observations Services API."));

	}
}
