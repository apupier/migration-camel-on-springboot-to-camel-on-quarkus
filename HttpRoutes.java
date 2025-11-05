import org.apache.camel.builder.RouteBuilder;

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
