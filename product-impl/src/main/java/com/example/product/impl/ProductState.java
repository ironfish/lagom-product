package com.example.product.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

@SuppressWarnings("serial")
@Value
@JsonDeserialize
final class ProductState implements CompressedJsonable {

    private final String name;
    private final String cost;
    private final String rating;
    private final String timestamp;

    @JsonCreator
    ProductState(String name, String cost, String rating, String timestamp) {
        this.name = Preconditions.checkNotNull(name, "name");
        this.cost = Preconditions.checkNotNull(cost, "cost");
        this.rating = Preconditions.checkNotNull(rating, "rating");
        this.timestamp = Preconditions.checkNotNull(timestamp, "timestamp");
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

    String getTimestamp() {
        return timestamp;
    }
}
