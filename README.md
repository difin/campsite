# Volcanic Getaways

This backend application manages reservations of a camping site which has _N_ camping spots (_N_=3 by default).
This application provides REST API for interacting with clients.

## System Requirements to Build the Application:

- Java 9+
- Maven
- Internet Access (for download dependencies by Maven)
- Writable application's directory (for build artifacts)
- Writable User's HOME (~/) directory

## Technologies Used
* Java 9
* Spring 
    * Spring Core
    * Spring-Boot
    * Spring Web
    * Spring WebFlux (Reactive Streams)
    * Spring-Data-JPA 
    * Cron
    * Validation
    * Exception Handling
    * Logging
* Maven
* Spring Boot Test, JUnit, Hamcrest, Mockito, WebFluxTest
* H2 Database
* Liquibase
* Swagger

## To Build
Run in the root directory of this project:
```
mvn clean install
```

## To Run Tests
Run in the root directory of this project:
```
mvn test
```

## To Run the Application from Command Line

```
cd target                      

java -jar volcanic_getaways.jar
```

## Configuration

This default configurations can be updated in application.yml:

Property|Value|Description
--------|-----|-----------
server.port|9000|port to access the application
server.servlet.context-path|/api|context root
spring.datasource.url|jdbc:h2:file:~/volcanic_getaways/h2|Includes path where to store H2 database file. Can be changed to use different location on disk or to be in-memory. 

## REST API

* Under default configuration: http://localhost:9000/api/
* The REST API is reactive. All routes return Flux\<T\> or Mono\<T\> streams. Flux is a stream of 0 or more elements and Mono is a stream of 0 or 1 element.
* To discover supported API Routes, parameters etc, please start up the application and navigate to http://localhost:9000/api/swagger-ui.htm
 