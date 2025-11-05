package com.acme.myspringbootproject;

import org.apache.camel.builder.RouteBuilder;

import org.springframework.stereotype.Component;

@Component
public class HttpRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        rest("/say")
            .get("/hello").to("direct:hello");

        from("direct:hello")
            .setBody()
                .simple("Hello Camel from ${routeId}")
            .log("${body}")
            .to("http://localhost/bye");
    }
}
