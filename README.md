Account service
======================
# Getting Started
This project is based on Spring Boot microservices using the reactive stack, read more info there https://spring.io/reactive

## Frameworks 
* Spring Boot
* Spring Data - Reactive Mongo
* Webflux

## Setup
* Java 1.8
* Maven
* Mongo
* Docker

# Building
## Windows
1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop).
2. Create an image and container for account-service using the following code:
```
mvn install
docker build . -t account-service
docker run -p 8090:8090 --name account-service account-service
```
# CRUD

| HTTP Verb  |     `/accounts`  |      `/accounts/{customerId}`      |   
| ---------- | :---------------: | :---------------: |
| **POST**| ADD new account | - |  
| **GET**| GET all accounts | GET account by Id |
| **PUT**| - | EDIT account by Id|  
| **DELETE**| - |DELETE account by Id|  


# Operations
| HTTP Verb  |     `/customers/profile/{customerId}`  |
| ---------- | :---------------: |
| **GET**| GET a detailed customer profile |


# Architecture

![Architecture](https://raw.githubusercontent.com/dmendozy/config-service/master/files/arch.png)

# Authors

* **Danny Mendoza Yenque** - *Everis Bootcamp Microservices July 2020* - [DannyMendoza](https://github.com/dmendozy)
