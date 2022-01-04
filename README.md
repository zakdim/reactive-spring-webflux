# reactive-spring-webflux
Spring Webflux

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

