package com.example.product.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

public interface ProductEvent extends Jsonable, AggregateEvent<ProductEvent> {

    AggregateEventShards<ProductEvent> TAG =
            AggregateEventTag.sharded(ProductEvent.class, 4);

    @Override
    default AggregateEventTagger<ProductEvent> aggregateTag() {
        return TAG;
    }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    public final class ProductCreated implements ProductEvent {

        public final String name;
        public final String message;

        @JsonCreator
        public ProductCreated(String name, String message) {
            this.name = Preconditions.checkNotNull(name, "name");
            this.message = Preconditions.checkNotNull(message, "message");
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
    }

}
