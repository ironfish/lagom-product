package com.example.product.impl;

import com.example.product.api.ProductMessage;
import com.example.product.api.ProductService;
import org.junit.Test;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class ProductServiceTest {
    @Test
    public void shouldStoreProduct() throws Exception {
        withServer(defaultSetup().withCassandra(), server -> {
            ProductService service = server.client(ProductService.class);

            String msg1 = service.getProduct("1234").invoke().toCompletableFuture().get(5, SECONDS);
            assertEquals("Does not exist, 0.00, 0, 1234", msg1);

            service.createProduct("1234").invoke(
                    new ProductMessage("Socks", "23.87", "7")).toCompletableFuture().get(5, SECONDS);
            String msg2 = service.getProduct("1234").invoke().toCompletableFuture().get(5, SECONDS);
            assertEquals("Socks, 23.87, 7, 1234", msg2);

            String msg3 = service.getProduct("5555").invoke().toCompletableFuture().get(5, SECONDS);
            assertEquals("Does not exist, 0.00, 0, 5555", msg3);
        });
    }
}
