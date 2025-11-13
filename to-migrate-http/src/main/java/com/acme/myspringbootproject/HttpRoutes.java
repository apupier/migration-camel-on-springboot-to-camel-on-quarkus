package com.acme.myspringbootproject;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import org.springframework.stereotype.Component;

@Component
public class HttpRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        rest("/say")
            .get("/hello").to("direct:hello")
            .get("/bye").to("direct:bye");

        from("direct:hello")
            .routeId("route-hello")
            .setBody()
                .simple("Hello Camel from ${routeId}")
            .log("${body}")
            .removeHeader(Exchange.HTTP_URI)
            .removeHeader(Exchange.HTTP_PATH)
            .to("http://localhost:8080/say/bye?bridgeEndpoint=true");
        
        from("direct:bye")
            .routeId("route-bye")
            .setBody()
                .simple("Bye from Camel from ${routeId}")
            .log("${body}");
    }
}
