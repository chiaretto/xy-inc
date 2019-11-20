[![codecov](https://codecov.io/gh/chiaretto/xy-inc/branch/master/graph/badge.svg?token=mvBT1vyowK)](https://codecov.io/gh/chiaretto/xy-inc)

# XYInc

Meta Data Management System

  - Create/Edit/List/Delete new Models (MetaData Model)
  - Create/Edit/Lista/Delete new metadata for the MetaData Models.

# Tecnologies
- Java 8 
- Springboot 2
- JUnit 5
- Swagger
- MongoDB

# Requeriments
  - Docker

# Run

  - clone this repository
```sh
$ git clone ...
```
  - execute in folder
```sh
$ docker-compose up
```

Basic users

| Role | User | Pass |
| ------ | ------ | ------ |
| USER | user | user123 |
| ADMIN | admin | admin123 |

How to use
  - After run docker, access the url API DOCS Swagger
  ```http://localhost:8080/swagger-ui.html```
  - Access Mongo Express
  ```http://localhost:8081/```
  - Access Admin API to Create Models
  ```http://localhost:8080/admin/model```
  - Access Admin API
  ```http://localhost:8080/api/{model}```

# Architecture

- Present
![](https://github.com/chiaretto/xy-inc/blob/master/docs/arq-atual.png?raw=true)

- Evolution to Baas
![](https://github.com/chiaretto/xy-inc/blob/master/docs/arq-bass.png?raw=true)

# Infrastructure

- Present
![](https://github.com/chiaretto/xy-inc/blob/master/docs/infra-atual.png?raw=true)

- Evolution to Autoscalling
![](https://github.com/chiaretto/xy-inc/blob/master/docs/infr-nova.png?raw=true)
