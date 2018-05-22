package com.example.product.stream.impl;

import com.example.product.api.ProductService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.example.product.stream.api.StreamService;

public class StreamModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(StreamService.class, StreamServiceImpl.class);
        bindClient(ProductService.class);
        bind(StreamSubscriber.class).asEagerSingleton();
    }
}
