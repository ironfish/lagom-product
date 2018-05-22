package com.example.product.stream.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.example.product.api.ProductService;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.example.product.stream.api.StreamService;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class StreamServiceImpl implements StreamService {

    private final ProductService productService;
    private final StreamRepository repository;

    @Inject
    public StreamServiceImpl(ProductService productService, StreamRepository repository) {
        this.productService = productService;
        this.repository = repository;
    }

    @Override
    public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> directStream() {
        return products -> completedFuture(
                products.mapAsync(8, name ->  productService.get_product(name).invoke()));
    }

    @Override
    public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> autonomousStream() {
        return products -> completedFuture(
                products.mapAsync(8, name -> repository.getMessage(name).thenApply( message ->
                        String.format("%s, %s!", message.orElse("Hello"), name)
                ))
        );
    }
}
