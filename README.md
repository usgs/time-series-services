# Water Data for the Nation
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1b57ae37d61a48078ee9b7b64204b463)](https://www.codacy.com/manual/usgs_wma_dev/time-series-services?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=usgs/time-series-services&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.com/usgs/time-series-services.svg?branch=master)](https://travis-ci.com/usgs/time-series-services)
[![codecov](https://codecov.io/gh/usgs/time-series-services/branch/master/graph/badge.svg)](https://codecov.io/gh/usgs/time-series-services)

Water Data for the Nation Observations service.

## Development
This is a Spring Boot project. All of the normal caveats relating to a Spring Boot application apply.

### Environment Variables
To run in a development environment, create an application.yml file in the project root directory containing the following (shown are example values):

```.yml
ROOT_LOG_LEVEL: "INFO"
SERVER_PORT: "8080"
SERVER_CONTEXT_PATH: "/api/observations"
SITE_URL_BASE: "http://localhost:8080/api/observations/"
MONLOC_CONTACT_NAME: "First Last"
MONLOC_CONTACT_EMAIL: "changeMe"
WDFN_DATABASE_ADDRESS: "localhost"
WDFN_DATABASE_PORT: "5432"
WDFN_DATABASE_NAME: "wdfn_db"
WDFN_DB_READ_ONLY_USERNAME: "wdfn_user"
WDFN_DB_READ_ONLY_PASSWORD: "changeMe"
WDFN_SCHEMA_OWNER_USERNAME: "wdfn_owner"
WDFN_SCHEMA_OWNER_PASSWORD: "changeMe"
```

### Running the Demo DB for local development
The short version:
```shell
docker network create --subnet=172.25.0.0/16 wdfn  (only needs to be run once)
docker run -it --network=wdfn -p 127.0.0.1:5437:5432/tcp usgswma/wqp_db:etl
```
The network and the port will need to match the values in the application.yml.

### Unit Testing
To run the JUnit tests via Maven:

```.sh
mvn package
```

### Database Integration Testing
To additionally start up a Docker database and run the integration tests via Maven, use:

```.sh
docker network create --subnet=172.25.0.0/16 wdfn  (only needs to be run once)
mvn verify -DTESTING_DATABASE_PORT=5437 -DTESTING_DATABASE_ADDRESS=localhost -DTESTING_DATABASE_NETWORK=wdfn
```
**Note:  If you configure your IDE to run integration tests, make sure the configuration is pointed at a local Docker
db, not a cloud hosted db.  Integration tests will delete/modify records in the db they are pointed at.**
### Maven DOCKER_HOST Error

If maven verify returns an error like this "... no DOCKER_HOST environment variable ..."
then the user running mvn command has not been granted rights to the docker group.

The following commands will ensure the docker group exists and assign it to the current user.

```.sh
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
```
