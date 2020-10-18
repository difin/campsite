# Volcanic Getaways

## This backend application manages reservations on a camping site which has _N_ camping spots.

System requirements:

- Java 9+
- Maven
- Internet access

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
* Maven (Dependency Management & build)
* Spring Boot Test, JUnit, Hamcrest, Mockito, WebFluxTest
* H2 Database
* Liquibase

## How to Build
Run in the root directory of this project:
```
mvn clean install
```

## How to Run Tests
Run in the root directory of this project:
```
mvn test
```

## How to Run the Application from Command Line

```
cd target                      

java -jar volcanic_getaways.jar
```