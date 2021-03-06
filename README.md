# reactive-spring-webflux
Spring Webflux

Useful references:

* [Web on Reactive Stack](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
* [Reactor 3 Reference Guide](https://projectreactor.io/docs/core/release/reference/)


## Install Mongo DB in MAC

- Run the below command to install the **MongoDB**.

```
brew services stop mongodb
brew uninstall mongodb

brew tap mongodb/brew
brew install mongodb-community
```

-  How to restart MongoDB in your local machine.

```
brew services restart mongodb-community
brew services start mongodb-community
brew services stop mongodb-community
```

## Install Mongo DB in Windows

- Follow the steps in the below link to install Mongo db in Windows.

https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/

## Section 10 - Set up the MoviesInfoService Service

### Lecture 35 - Project Setup

```
To start mongodb/brew/mongodb-community now and restart at login:
  brew services start mongodb/brew/mongodb-community
Or, if you don't want/need a background service you can just run:
  mongod --config /usr/local/etc/mongod.conf
```

* Start/stop mongo service

``` 
brew services stop mongodb-community
brew services start mongodb-community
```

* Test `MoviesInfoServiceApplication` (make sure it's running)

``` 
movies-info-service (master) $ ./gradlew bootRun
```

## Section 11 - Simple Non Blocking RESTFUL API using Annotated Controller Approach

### Lecture 36 - Building a Simple Non Blocking API - Flux

``` 
$ curl http://localhost:8080/flux
```

## Section 16 - Bean Validation using Validators and ControllerAdvice

### Lecture 64 - Customize the Default Error handling using ControllerAdvice

* Default error response

``` 
responseBody: {"timestamp":"2022-01-04T19:18:18.355+00:00","path":"/v1/movieinfos","status":400,"error":"Bad Request","requestId":"8b2b7f0"}
```

* Custom error response

``` 
responseBody: movieInfo.name must be present,movieInfo.year must be Positive value
```

## Section 20 - Introduction to Functional Web Module in Spring WebFlux

### Lecture 73 - Build a simple RestFull API using Functional Web

* To run movies review service application

``` 
cd movies-review-service
./gradlew bootRun

curl http://localhost:8081/v1/helloworld
```

## Section 30 - Server Sent Events (SSE)

### Lecture 111 - Introduction to Sinks

* [Reactor 3 Reference Guide](https://projectreactor.io/docs/core/release/reference/)
* [4.7. Processors and Sinks](https://projectreactor.io/docs/core/release/reference/#processors)