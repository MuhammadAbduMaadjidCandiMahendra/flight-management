# Flight Booking and Management

## 1. Technology used:
* CLI Application
* Spring Boot JPA
* MySQL

## 2. How to run
1. Change URL, user, and password inside [application.properties](src/main/resources/application.properties)
```
spring.datasource.url
spring.datasource.username
spring.datasource.password
```
2. Open terminal/CMD in the project directory and run the command:
```mvn spring-boot:run```

3. Spring Boot will start the application and created the database and tables automatically.
4. If error occurs during database startup, please do workaround:
   * create a database with the name `flight_management` and rerunning the application.
   * or create a database with the name `flight_management` and run script provided in [application-DDL.sql](db/application-DDL.sql)