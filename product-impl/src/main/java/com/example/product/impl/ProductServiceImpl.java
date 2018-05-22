package com.example.product.impl;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import com.example.product.api.ProductEvent;
import com.example.product.api.ProductMessage;
import com.example.product.api.ProductService;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

public class ProductServiceImpl implements ProductService {

    private final PersistentEntityRegistry persistentEntityRegistry;

    @Inject
    public ProductServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        persistentEntityRegistry.register(ProductEntity.class);
    }

    @Override
    public ServiceCall<NotUsed, String> get_product(String id) {
        return request -> {
            PersistentEntityRef<ProductCommand> ref = persistentEntityRegistry.refFor(ProductEntity.class, id);
            return ref.ask(new ProductCommand.GetProduct(id));
        };
    }

    @Override
    public ServiceCall<ProductMessage, Done> create_product(String id) {
        return request -> {
            PersistentEntityRef<ProductCommand> ref = persistentEntityRegistry.refFor(ProductEntity.class, id);
            return ref.ask(new ProductCommand.CreateProduct(request.message));
        };
    }

    @Override
    public Topic<ProductEvent> productEvents() {
        return TopicProducer.taggedStreamWithOffset(com.example.product.impl.ProductEvent.TAG.allTags(), (tag, offset) ->

                // Load the event stream for the passed in shard tag
                persistentEntityRegistry.eventStream(tag, offset).map(eventAndOffset -> {

                    // Now we want to convert from the persisted event to the published event.
                    // Although these two events are currently identical, in future they may
                    // change and need to evolve separately, by separating them now we save
                    // a lot of potential trouble in future.
                    com.example.product.api.ProductEvent eventToPublish;

                    if (eventAndOffset.first() instanceof com.example.product.impl.ProductEvent.ProductCreated) {

                        com.example.product.impl.ProductEvent.ProductCreated productCreated =
                                (com.example.product.impl.ProductEvent.ProductCreated) eventAndOffset.first();

                        eventToPublish = new com.example.product.api.ProductEvent.ProductCreated(
                                productCreated.getName(), productCreated.getMessage()
                        );
                    } else {
                        throw new IllegalArgumentException("Unknown event: " + eventAndOffset.first());
                    }

                    // We return a pair of the translated event, and its offset, so that
                    // Lagom can track which offsets have been published.
                    return Pair.create(eventToPublish, eventAndOffset.second());
                })
        );
    }

}
