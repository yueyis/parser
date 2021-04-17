## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Docker Support](#setup)

## General info
This project is simple Lorem ipsum dolor generator.

## Technologies
Project is created with <b>Spring Boot</b> and can be run in <b>Docker</b>.
* Spring Boot
* Docker

## Build & Deploy

To package the source code to jar using Maven, run the following command:

```
./mvnw package
```

We can run the below plain command to start the application.
```
java -jar target/parser-1.0.jar
```

We can also add additional layer to run the Spring Boot application in Docker.
```
docker build -t yueyis/parser .
docker run -d -p 8088:8088 yueyis/parser
```