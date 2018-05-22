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

            String msg1 = service.get_product("Socks").invoke().toCompletableFuture().get(5, SECONDS);
            assertEquals("Nice, Socks!", msg1);

            service.create_product("Socks").invoke(new ProductMessage("Cool")).toCompletableFuture().get(5, SECONDS);
            String msg2 = service.get_product("Socks").invoke().toCompletableFuture().get(5, SECONDS);
            assertEquals("Cool, Socks!", msg2);

            String msg3 = service.get_product("Shirts").invoke().toCompletableFuture().get(5, SECONDS);
            assertEquals("Nice, Shirts!", msg3);
        });
    }
}
