package gov.usgs.wma.waterdata.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.service.Tag;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableSwagger2
@Profile("swagger")
public class SwaggerConfig {

	public static final String VERSION_TAG_NAME = "Application Version";
	public static final String TAG_DESCRIPTION = "Data Download";

	@Value("${swagger.display.host}")
	private String swaggerDisplayHost;

	@Value("${swagger.display.path}")
	private String swaggerDisplayPath;

	@Value("${swagger.display.scheme}")
	private String swaggerDisplayScheme;

	@Bean
	public Docket timeSeriesServicesApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.protocols(new HashSet<>(Arrays.asList(swaggerDisplayScheme)))
				.host(swaggerDisplayHost)
				.pathProvider(pathProvider())
				.tags(new Tag(VERSION_TAG_NAME, TAG_DESCRIPTION))
				.select()
				.apis(RequestHandlerSelectors.basePackage("gov.usgs.wma.waterdata"))
				.paths(PathSelectors.any())
				.build();
	}

	@Bean
	public PathProvider pathProvider() {
		PathProvider rtn = new ProxyPathProvider();
		return rtn;
	}

	public class ProxyPathProvider extends AbstractPathProvider {
		@Override
		protected String applicationPath() {
			return swaggerDisplayPath;
		}

		@Override
		protected String getDocumentationPath() {
			return swaggerDisplayPath;
		}
	}
}

