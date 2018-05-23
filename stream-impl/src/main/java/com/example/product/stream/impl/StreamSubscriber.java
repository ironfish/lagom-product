package com.example.product.stream.impl;

import akka.Done;
import akka.stream.javadsl.Flow;
import com.example.product.api.ProductEvent;
import com.example.product.api.ProductService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class StreamSubscriber {

    @Inject
    public StreamSubscriber(ProductService productService, StreamRepository repository) {
        // Create a subscriber
        productService.productEvents().subscribe()
                // And subscribe to it with at least once processing semantics.
                .atLeastOnce(
                        // Create a flow that emits a Done for each name it processes
                        Flow.<ProductEvent>create().mapAsync(1, event -> {

                            if (event instanceof ProductEvent.ProductCreated) {
                                ProductEvent.ProductCreated productCreated = (ProductEvent.ProductCreated) event;
                                return repository.updateProduct(
                                        productCreated.getId(),
                                        productCreated.getName(),
                                        productCreated.getCost(),
                                        productCreated.getRating());

                            } else {
                                // Ignore all other events
                                return CompletableFuture.completedFuture(Done.getInstance());
                            }
                        })
                );

    }
}
