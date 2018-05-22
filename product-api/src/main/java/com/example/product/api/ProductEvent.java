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

    String getName();
    String getMessage();

    @Value
    final class ProductCreated implements ProductEvent {
        final String name;
        final String message;

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
