import org.apache.camel.builder.RouteBuilder;

public class KafkaRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("kafka:topic1")
            .setBody()
                .simple("Received message from topic1")
            .log("${body}")
            .to("kafka:topic2");
    }
}
