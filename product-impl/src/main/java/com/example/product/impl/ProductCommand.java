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
        public final String message;

        @JsonCreator
        public CreateProduct(String message) {
            this.message = Preconditions.checkNotNull(message, "message");
        }

        @JsonCreator
        public String getMessage() {
            return message;
        }
    }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class GetProduct implements ProductCommand, PersistentEntity.ReplyType<String> {

        public final String name;

        @JsonCreator
        public GetProduct(String name) {
            this.name = Preconditions.checkNotNull(name, "name");
        }

//        @JsonCreator
        public String getName() {
            return name;
        }
    }
}
