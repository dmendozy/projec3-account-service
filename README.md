Bootcamp-project2
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
2. Create an image for MongoDB
```
docker pull mongo:latest
```
3. Create a container for the database
```
docker run -d -p 27017:27017 --name bootcamp-mongodb mongo:latest
```
4. Check
```
docker ps
```
5. Clone each repository called project2-* in my profile
6. Create each image and container using the following code:
```
mvn install
docker build -f Dockerfile -t account-service
docker run -p 8083:8083 --name account-service account-service
```
7. After create all images and container, use docker compose to start all services from project2-config
```
docker-compose up
```

# Architecture

![Architecture](https://raw.githubusercontent.com/dmendozy/project2-config/master/files/architecture.png)

# Authors

* **Danny Mendoza Yenque** - *Everis Bootcamp Microservices July 2020* - [DannyMendoza](https://github.com/dmendozy)


