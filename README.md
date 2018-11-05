# Football Fantasies

Implements a RESTful service to allow users to interact with fantasy football data.

## Getting Started

TBD

### Interacting with the data

A step by step series of examples that tell you how to get a development env running

Get all Players:

```
$ curl localhost:8080/players
```

Get one Player:

```
$ curl localhost:8080/players/{player id}
```

Create Player:

```
$ curl -X POST localhost:8080/players -H 'Content-type:application/json' -d '{"firstName":"Emmanuel", "lastName":"Sanders", "eligiblePosition":"WR"}'
```

Update Player:

```
$ curl -X PUT localhost:8080/players/{player id} -H 'Content-type:application/json' -d '{"firstName":"Emmanuel", "lastName":"Sanders", "eligiblePosition":"WR"}'
```

Delete Player:

```
$ curl -X DELETE localhost:8080/players/{player id}
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Boot](https://spring.io/projects/spring-boot) - Used to create stand-alone applications on the Spring platform.

## Authors

* **Bunreth Nhong** - *Initial work* - [Github](https://github.com/bnhong)