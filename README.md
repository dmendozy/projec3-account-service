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
## Account
| HTTP Verb  |     `/accounts`  |      `/accounts/{accountId}`      |   
| ---------- | :---------------: | :---------------: |
| **POST**| ADD new account | - |  
| **GET**| GET all accounts | GET account by Id |
| **PUT**| - | EDIT account by Id|  
| **DELETE**| - |DELETE account by Id|  

## Account Type
| HTTP Verb  |     `/accounts/type`  |      `/accounts/type/{accountTypeId}`      |   
| ---------- | :---------------: | :---------------: |
| **POST**| ADD new accountType | - |  
| **GET**| GET all accountTypes | GET accountType by Id |
| **PUT**| - | EDIT accountType by Id|  
| **DELETE**| - |DELETE accountType by Id|  

# Operations
| HTTP Verb  |   `accounts/balance/{accountId}` |`accounts/customer/{customerId}` |`accounts/{bankId}/{firstDate}/{finishDate}` | `accounts/transfer/{account1}/{account2}/{amount}`| `accounts/pay/credit/{accountId}/{creditId}/{amount}`|
| ---------- | :---------------: |:---------------: |:---------------: |:---------------: |:---------------: |
| **GET**| GET balance from account |GET customer by customerId |GET customers date range from a bank |Account transfer |Pay credit from account |

| HTTP Verb  |  `accounts/deposit/{accountId}/{amount}`  |`accounts/withdraw/{accountId}/{amount}`  |`accounts/atm/deposit/{accountId}/{amount}`  |`accounts/atm/withdraw/{accountId}/{amount}`  |`accounts/bank/atm/deposit/{accountId}/{amount}/{bankId}` |`accounts/bank/atm/withdraw/{accountId}/{amount}/{bankId}` |
| ---------- | :---------------: |:---------------: |:---------------: |:---------------: |:---------------: |:---------------: |
| **PUT**| Deposit money  |Withdraw money  |Deposit from ATM  |Withdraw from ATM  |Deposit from ATM to other bank  |Withdraw from ATM to other bank |



# Architecture

![Architecture](https://raw.githubusercontent.com/dmendozy/config-service/master/files/arch.png)

# Authors

* **Danny Mendoza Yenque** - *Everis Bootcamp Microservices July 2020* - [DannyMendoza](https://github.com/dmendozy)
