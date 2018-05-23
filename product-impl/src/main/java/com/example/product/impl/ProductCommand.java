package com.example.product.impl;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

public interface ProductCommand extends Jsonable {

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class CreateProduct implements ProductCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
        private final String name;
        private final String cost;
        private final String rating;

        @JsonCreator
        CreateProduct(String name, String cost, String rating) {
            this.name = Preconditions.checkNotNull(name, "name");
            this.cost = Preconditions.checkNotNull(cost, "cost");
            this.rating = Preconditions.checkNotNull(rating, "rating");
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

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class GetProduct implements ProductCommand, PersistentEntity.ReplyType<String> {

        private final String name;

        @JsonCreator
        GetProduct(String name) {
            this.name = Preconditions.checkNotNull(name, "name");
        }

        String getName() {
            return name;
        }
    }
}
