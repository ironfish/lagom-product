package com.example.product.impl;

import com.example.product.api.ProductService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class ProductModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(ProductService.class, ProductServiceImpl.class);
    }
}
