# reactive-spring-webflux
Spring Webflux

#### Install Mongo DB in MAC

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
```

#### Install Mongo DB in Windows

- Follow the steps in the below link to install Mongo db in Windows.

https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/

#### Section 10 - Set up the MoviesInfoService Service

##### Lecture 35 - Project Setup

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
