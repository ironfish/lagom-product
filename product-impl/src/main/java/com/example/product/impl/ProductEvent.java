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
    final class ProductCreated implements ProductEvent {

        private final String id;
        private final String name;
        private final String cost;
        private final String rating;

        @JsonCreator
        ProductCreated(String id, String name, String cost, String rating) {
            this.id = Preconditions.checkNotNull(id, "id");
            this.name = Preconditions.checkNotNull(name, "name");
            this.cost = Preconditions.checkNotNull(cost, "name");
            this.rating = Preconditions.checkNotNull(rating, "name");
        }

        String getId() {
            return id;
        }

        String getName() {
            return name;
        }

        String getCost() {
            return cost;
        }

        String getRating() {
            return rating;
        }
    }

}
