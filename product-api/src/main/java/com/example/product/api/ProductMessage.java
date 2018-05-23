package com.example.product.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.Value;

@Value
@JsonDeserialize
public class ProductMessage {

    private final String name;
    private final String cost;
    private final String rating;

    @JsonCreator
    public ProductMessage(String name, String cost, String rating) {
        this.name = Preconditions.checkNotNull(name, "name");
        this.cost = Preconditions.checkNotNull(cost, "cost");
        this.rating = Preconditions.checkNotNull(rating, "rating");
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }

    public String getRating() {
        return rating;
    }
}
