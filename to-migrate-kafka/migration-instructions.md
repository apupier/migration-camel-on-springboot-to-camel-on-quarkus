# Test that the current project is working on your system

Follow instructions in readme.md

# Migration

## Generic Spring Boot to Quarkus

```bash
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-spring-to-quarkus:RELEASE -Drewrite.activeRecipes=org.openrewrite.quarkus.spring.SpringBootToQuarkus -Drewrite.exportDatatables=true
```

## Update boms

Remove org.apache.camel.springboot:camel-spring-boot-bom

Add
com.redhat.quarkus.platform:quarkus-bom
com.redhat.quarkus.platform:quarkus-camel-bom

## Update core dependencies

* Delete parent spring-boot-starter-parent
* Delete spring-boot-starter-actuator
* Replace org.apache.camel.springboot:camel-spring-boot-starter by org.apache.camel.quarkus:camel-quarkus-core


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

## Update Camel textual debug configuration

In the camel.debug profile of the pom.xml, for the jvmarguments for Quarkus Maven plugin, use:

```
-Dcamel.main.shutdownTimeout=30 -Dquarkus.camel.source-location-enabled=true -javaagent:target/dependency/jolokia-agent-jvm-javaagent.jar=port=7878,host=localhost
```