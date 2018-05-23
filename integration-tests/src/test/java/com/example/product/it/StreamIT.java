package com.example.product.it;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.example.product.api.ProductMessage;
import com.example.product.api.ProductService;
import com.lightbend.lagom.javadsl.client.integration.LagomClientFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.example.product.stream.api.StreamService;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class StreamIT {

    private static final String SERVICE_LOCATOR_URI = "http://localhost:9008";

    private static LagomClientFactory clientFactory;
    private static ProductService productService;
    private static StreamService streamService;
    private static ActorSystem system;
    private static Materializer mat;

    @BeforeClass
    public static void setup() {
        clientFactory = LagomClientFactory.create("integration-test", StreamIT.class.getClassLoader());
        // One of the clients can use the service locator, the other can use the service gateway, to test them both.
        productService = clientFactory.createDevClient(ProductService.class, URI.create(SERVICE_LOCATOR_URI));
        streamService = clientFactory.createDevClient(StreamService.class, URI.create(SERVICE_LOCATOR_URI));

        system = ActorSystem.create();
        mat = ActorMaterializer.create(system);
    }

    @Test
    public void getProduct() throws Exception {
        String answer = await(productService.getProduct("foo").invoke());
        assertEquals("Does not exist, 0.00, 0, foo", answer);

        await(productService.createProduct("bar").invoke(new ProductMessage("Socks", "22.00", "10")));
        String answer2 = await(productService.getProduct("bar").invoke());
        assertEquals("Socks, 22.00, 10, bar", answer2);
    }

    @Test
    public void getProductStream() throws Exception {
        // Important to concat our source with a maybe, this ensures the connection doesn't get closed once we've
        // finished feeding our elements in, and then also to take 3 from the response stream, this ensures our
        // connection does get closed once we've received the 3 elements.
        Source<String, ?> response = await(streamService.directStream().invoke(
                Source.from(Arrays.asList("a", "b", "c"))
                        .concat(Source.maybe())));
        List<String> messages = await(response.take(3).runWith(Sink.seq(), mat));

        assertEquals(Arrays.asList(
                "Does not exist, 0.00, 0, a",
                "Does not exist, 0.00, 0, b",
                "Does not exist, 0.00, 0, c"), messages);
    }

    private <T> T await(CompletionStage<T> future) throws Exception {
        return future.toCompletableFuture().get(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void tearDown() {
        if (clientFactory != null) {
            clientFactory.close();
        }
        if (system != null) {
            system.terminate();
        }
    }
}
