# How to build and run a Camel application

This project was generated using [Camel Jbang](https://camel.apache.org/manual/camel-jbang.html). Please, refer to the online documentation for learning more about how to configure the export of your Camel application.

## Development

```bash
./mvnw clean spring-boot:run
```

Then to test:

```bash
curl -X GET http:localhost:8080/say/hello
```


## Build the Maven project

```bash
./mvnw clean package
```

The application could now immediately run:

```bash
java -jar target/myspringbootproject-1.0-SNAPSHOT.jar
```

