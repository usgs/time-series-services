package gov.usgs.wma.waterdata.controller;

import gov.usgs.wma.waterdata.swagger.SwaggerConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags={SwaggerConfig.VERSION_TAG_NAME})
@Controller
public class VersionController {
	@ApiOperation(value="Return the web service version information.")
	@RequestMapping(
			value="version",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE
	)
	public String getVersion() {
		return "redirect:/about/info";
	}
}