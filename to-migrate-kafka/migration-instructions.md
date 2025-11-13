# Test that the current project is working on your system

Follow instructions in readme.md

# Migration

## Generic Spring Boot to Quarkus

```bash
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-spring-to-quarkus:RELEASE -Drewrite.activeRecipes=org.openrewrite.quarkus.spring.SpringBootToQuarkus -Drewrite.exportDatatables=true
```

## Update boms

Remove spring-boot-dependencies

Replace org.apache..camel.springboot:camel-spring-boot-bom by 
org.apache.camel.quarkus:camel-quarkus-bom:3.29.0

## Update core dependencies

* Replace org.apache.camel.springboot:camel-spring-boot-starter by org.apache.camel.quarkus:camel-quarkus-core
* Delete camel-test-spring-junit5

## Update component dependencies

Replace
```xml
        <dependency>
            <groupId>org.apache.camel.springboot</groupId>
            <artifactId>camel-direct-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.camel.springboot</groupId>
            <artifactId>camel-kafka-starter</artifactId>
        </dependency>
```
by
```xml
        <dependency>
            <groupId>org.apache.camel.quarkus</groupId>
            <artifactId>camel-quarkus-direct</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.camel.quarkus</groupId>
            <artifactId>camel-quarkus-kafka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.camel.quarkus</groupId>
            <artifactId>camel-quarkus-timer</artifactId>
        </dependency>
```


## Remove "Main" annotated class

Remove CamelApplication.java class

## Configure Quarkus Maven plugin

```xml
<plugin> 
  <groupId>${quarkus.platform.group-id}</groupId> 
  <artifactId>quarkus-maven-plugin</artifactId> 
  <version>${quarkus.platform.version}</version> 
  <extensions>true</extensions> 
  <executions> 
    <execution> 
      <goals> 
        <goal>build</goal> 
        <goal>generate-code</goal> 
        <goal>generate-code-tests</goal> 
     </goals> 
   </execution> 
  </executions> 
</plugin>
```

## Clean application.properties

Remove `camel.main.run-controller`

Given that a Kafka instance is provided by default as a dev service, we can rely on it for the broker configuration and have the property kept only for the production time, so the property can be replace with:

`%prod.camel.component.kafka.brokers=localhost:9092`


## Update Camel textual debug configuration (if in VS Code)

In the camel.debug profile of the pom.xml, for the jvmarguments for Quarkus Maven plugin, use:

```xml
                    <plugin>
                        <groupId>io.quarkus.platform</groupId>
                        <artifactId>quarkus-maven-plugin</artifactId>
                        <version>3.29.2</version>
                        <configuration>
                            <jvmArgs>-Dcamel.main.shutdownTimeout=30 -Dquarkus.camel.source-location-enabled=true -javaagent:target/dependency/jolokia-agent-jvm-javaagent.jar=port=7878,host=localhost</jvmArgs>
                        </configuration>
                    </plugin>
```

In .vscode/tasks.json, replace content:

```json
{
    "version": "2.0.0",
    "tasks": [
		{
            "label": "Start Camel application with Maven Quarkus Dev with camel.debug profile",
            "type": "shell",
            "command": "mvn", // mvn binary of Maven must be available on command-line
            "args": [
                "compile",
                "quarkus:dev",
                "'-Pcamel.debug'" // This depends on your project. The goal here is to have camel-debug on the classpath.
            ],
            "problemMatcher": "$camel.debug.problemMatcher",
            "presentation": {
                "reveal": "always"
            },
            "isBackground": true // Must be set as background as the Maven commands doesn't return until the Camel application stops.
        },
        {
            "label": "Build a Camel Quarkus application as a Native executable debug-ready",
            "detail": "This task will build Camel Quarkus application with JMX and Camel Debugger enabled using GraalVM",
            "type": "shell",
            "command": "./mvnw",
            "args": [
                "install",
                "-Dnative",
                "'-Dquarkus.native.monitoring=jmxserver,jmxclient'",
                "'-Dquarkus.camel.debug.enabled=true'",
                "'-Pcamel.debug'" // This depends on your project
            ],
            "problemMatcher": [],
            "presentation": {
                "reveal": "always"
            }
        },
        {
            "label": "Start Camel native application debug-ready",
            "detail": "This task will start Camel native application with Maven Quarkus Native and camel.debug profile",
            "type": "shell",
            "command": "./target/*-runner",
            "problemMatcher": "$camel.debug.problemMatcher",
            "presentation": {
                "reveal": "always"
            },
            "isBackground": true
        },
		{
            "label": "Deploy on OpenShift",
            "type": "shell",
            "command": "./mvnw",
            "args": [
                "package",
                "-Dquarkus.kubernetes.deploy=true"
            ],
            "problemMatcher": [],
            "presentation": {
                "reveal": "always"
            }
        }

    ]
}
```

in .vscode/launch.json, replace content by:

```json
{
    "version": "0.2.0",
    "configurations": [
		{
            "name": "Run Camel Quarkus JVM application and attach Camel debugger",
            "type": "apache.camel",
            "request": "attach",
            "preLaunchTask": "Start Camel application with Maven Quarkus Dev with camel.debug profile"
        },
        {
            "name": "Run Camel native application and attach Camel debugger",
            "type": "apache.camel",
            "request": "attach",
            "preLaunchTask": "Start Camel native application debug-ready"
        }
    ],
    "compounds": []
}
```

# Launch in dev mode

```bash
mvn clean quarkus:dev
```
Note that you do not need to start a kafka instance, one is automatically provided by a "dev service".
In this case, you can also access the Quarkus dev UI to view basic information on the Kafka.

# Going further - Natify

## native profile

Add the native profile:

```xml
        <profile>
            <id>native</id>
            <activation>
                <property>
                    <name>native</name>
                </property>
            </activation>
            <properties>
                <skipITs>false</skipITs>
                <quarkus.package.type>native</quarkus.package.type>
            </properties>
        </profile>
```

## native build

To build the native binary call:

```bash
mvn clean package -Dnative
```

To start the native application:

```bash
./target/*-runner
```

Note that this time you need a Kafka instance, for instance by running `docker run -p 9092:9092 apache/kafka:4.1.0`
