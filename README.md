# Water Data for the Nation
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1b57ae37d61a48078ee9b7b64204b463)](https://www.codacy.com/manual/usgs_wma_dev/time-series-services?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=usgs/time-series-services&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/USGS/time-series-services.svg?branch=master)](https://travis-ci.org/USGS/time-series-services)

Water Data for the Nation time series data streaming service.

## Development
This is a Spring Boot project. All of the normal caveats relating to a Spring Boot application apply.

## Configuration
This application is configured to be run as a jar. It can also be run using the command ``` mvn spring-boot:run ``` in the project root directory.
 
To run in a development environment, create an application.yml file in
the project root directory containing the following (shown are example values):
```.yml
SERVER_PORT: "8080"
SERVER_CONTEXT_PATH: "/api/observations"
SWAGGER_DISPLAY_HOST: ""
SWAGGER_DISPLAY_PATH: "/api/observations"
SWAGGER_DISPLAY_PROTOCOL: "https"
ROOT_LOG_LEVEL: "INFO"
```