# How to build and run a Camel application

This project was generated using [Camel Jbang](https://camel.apache.org/manual/camel-jbang.html). Please, refer the the online documentation for learning more about how to configure the export of your Camel application.

This is a brief guide explaining how to build, "containerize" and run your Camel application.

## Run in dev

Requirements, a local Kafka instance:

```bash
docker run -p 9092:9092 apache/kafka:4.1.0
```


Then build and start the application with one the two methods:

```bash
./mvnw clean spring-boot:run
```

or in VS Code, use the Launch configuration from Run and debug activity view, `Run Camel Spring Boot application and attach Camel debugger`

## Build the Maven project

```bash
./mvnw clean package
```

The application could now immediately run:

```bash
java -jar target/myspringbootproject-1.0-SNAPSHOT.jar
```

