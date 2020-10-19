# Volcanic Getaways

This backend application manages reservations of a camping site which has _N_ spots (_N_=3 by default).
It provides REST API for interacting with clients.

## Git Repository and Default Branch Names
I wanted to store my solution in a public GitHub repository to easily share it with reviewers, but to make it unsearchable for confidentiality of the home test.
Google isn't supposed to index branches other than 'master' and 'main', that's why my only branch has been called 'prime'. 
I also named the repository itself with a name that is not directly linked to the home test.

## System Requirements to Build the Application:

- JDK 11+
- Maven
- Internet Access (for download dependencies by Maven)
- Writable application's directory (for build artifacts)
- Writable User's HOME (~/) directory

## Technologies Used
* Java
* Spring 
    * Spring-Core
    * Spring-Boot
    * Spring-Web
    * Spring-WebFlux (Reactive Streams)
    * Spring-Data-JPA 
    * Cron
    * Spring Validation
    * Spring Boot provided REST Exception Handling
    * Spring-Boot-provided logging
* Maven
* Spring Boot Test, JUnit, Hamcrest, Mockito, WebFluxTest
* H2 Database
* Liquibase
* Swagger
* Lombok
* Jackson, JSON

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

Property|Default Value|Description|Notes
--------|-----|-----------|-----
server.port|9000|port to access the application|
server.servlet.context-path|/api|context root|
spring.datasource.url|jdbc:h2:file:~/volcanic_getaways/h2|Includes path where to store H2 database file. Can be changed to use different location on disk or to be in-memory.|if this property is changed, liquibase.url property in the same file must be updated to the same value.
app.spots-num|3|number of spots = max number of reservations for the same date|  

## Important Notes

* This application allows to book a stay in the following inclusive date's range: [tomorrow, one month from now].
<br/><br/>Please note, when providing departure dates in any routes, that date is exclusive.
For example, to book dates [2020-November-01, 2020-November-02, 2020-November-03], the dates range needs to be arrival=[2020-November-01] and departure=[2020-November-04], because the departure date is not considered a day of the stay, that's the date when the client leaves. <br/> <br/>
* When application starts, it inserts 'Managed Dates' for the period of 1 month from now into the corresponding database table, if these dates are not there for this period.
<br/><br/>Managed Dates are dates that are available for reservation.<br/><br/> 
    * The same process runs as a batch job daily to make sure the system is always able to handle one month of dates from now.
    
## REST API

* Under default configuration: http://localhost:9000/api/
* The REST API is reactive. All routes return Flux\<T\> or Mono\<T\> streams. 
  * Flux is a lazy-evaluated reactive non-blocking stream of 0 or more elements.
  * Mono is same, but 0 or 1 elements.
* To discover the API Routes and models and to try executing the routes you start up the application and navigate to http://localhost:9000/api/swagger-ui.html
 
## Testing

This application contains following tests types:<br/>
* Unit Tests for Services and Models classes.<br/> <br/> 
  Unit Tests have been developed for the most important services and models.<br/> 
  Unfortunately, I didn't have enough time to develop Unit Tests for all classes.<br/><br/>
* Integration tests. <br/><br/> 
  This includes simple single-threaded tests and massive multi-threaded tests for testing against deadlocks and correctness under concurrent API calls.   
