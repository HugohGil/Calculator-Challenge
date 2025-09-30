# Calculator Challenge

A Spring Boot project with 3 modules, provides basic calculator functionalities using RESTful API and Apache Kafka for communication between the modules.

## Requirements

- Java 17+
- Maven
- Docker for Kafka

## How to Install
```bash
mvn clean install
```

## How to Run

Using docker:
```bash
docker compose up
```
The API will be available at http://localhost:8080/calculator

### API Calls Example
```http
GET http://localhost:8080/calculator/sum?a=10&b=5
GET http://localhost:8080/calculator/subtract?a=10&b=5
GET http://localhost:8080/calculator/multiply?a=10&b=5
GET http://localhost:8080/calculator/divide?a=10&b=5
```
