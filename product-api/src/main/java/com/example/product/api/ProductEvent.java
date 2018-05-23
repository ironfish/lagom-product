package com.example.product.api;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Preconditions;
import lombok.Value;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = com.example.product.api.ProductEvent.ProductCreated.class, name = "product-created")
})
public interface ProductEvent {

    String getId();
    String getName();
    String getCost();
    String getRating();

    @Value
    final class ProductCreated implements ProductEvent {
        private final String id;
        private final String name;
        private final String cost;
        private final String rating;

        @JsonCreator
        public ProductCreated(String id, String name, String cost, String rating) {
            this.id = Preconditions.checkNotNull(id, "id");
            this.name = Preconditions.checkNotNull(name, "name");
            this.cost = Preconditions.checkNotNull(cost, "cost");
            this.rating = Preconditions.checkNotNull(rating, "rating");
        }

        public String getId() {
            return id;
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
}
