package com.acme.myspringbootproject;

import org.apache.camel.builder.RouteBuilder;

import org.springframework.stereotype.Component;

@Component
public class KafkaRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("kafka:topic1")
            .setBody()
                .simple("Received message from topic1: ${body}")
            .log("${body}")
            .to("kafka:topic2");

        from("timer:sendMessage?period=5000")
            .setBody()
                .simple("Send message to topic1 every 5 seconds")
            .to("kafka:topic1");
    }
}
