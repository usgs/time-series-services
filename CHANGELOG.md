# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html). (Patch version X.Y.0 is implied if not specified.)

## [Unreleased](https://github.com/USGS/time-series-services/compare/time-series-services/compare/0.7.0...master)
### Changed
-   Add links to swagger output for collections/{collectionsId}/items/{featureId}
-   Changed /collections/{collectionId}/items to make agencyCodes, nationalAquiferCodes, and monitoringLocationNumbers into lists

## [0.7.0](https://github.com/USGS/time-series-services/compare/time-series-services-0.6.0...time-series-services-0.7.0) - 2020-08-19
### Added
-   New end point: /collections/{collectionId}/items/{featureId}/observations/discrete-data
### Changed
-   Removed deprecated end point: /monitoring-location/{monitoringLocationId}/time-series/{timeSeriesId}
-   Performance improvement on end point: /collections/{collectionId}/items/{featureId}/observations/statistical-time-series
-   Unapproved time series data older than 1095 days is not returned from the services (USGS rule)
-   Added filtering to /collections/{collectionId}/items
-   Added nationalAquiferCode to feature properties
-   Deprecated end point: /monitoring-location/{monitoringLocationId}

## [0.6.0](https://github.com/USGS/time-series-services/compare/time-series-services-0.5.0...time-series-services-0.6.0) - 2020-04-29
### Changed
-   Modified collections endpoint to populate properties field from properties column in network table.
### Added
    discrete-data included in observation types returned by Observations endpoint.

## [0.5.0](https://github.com/USGS/time-series-services/compare/time-series-services-0.4.0...time-series-services-0.5.0) - 2020-04-10
### Added
-   Feature Observations endpoint.
-   Properties field to collection.
-   Links to collections that a feature belongs to.
-   Query parameter validation per OGC standard.
### Changed
-   Updated http exception handling to conform to OGC standard.
-   Readme.md and Swagger documentation cleanup.

## [0.4.0](https://github.com/USGS/time-series-services/compare/time-series-services-0.2.0...time-series-services-0.4.0) - 2020-03-12
### Added
-   Time Series GeoJSON endpoint.
-   Feature Statistical Time Series collection
-   Feature Statistical Time Series observations
-   Collections GeoJSON endpoint.

## [0.2.0](https://github.com/usgs/time-series-services/compare/time-series-services-0.1.0...time-series-services-0.2.0) - 2019-12-10
### Changed
-   Replaced SpringFox with SpringDoc.
-   Modified Version Controller to work in deployed environment.
-   Added GeoJSON endpoint.

## [0.1.0](https://github.com/usgs/time-series-services/tree/time-series-services-0.1.0) - 2019-11-27
### Added
-   Added base features of an IOW spring-boot application.
