package com.example.product.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public class ProductEntity extends PersistentEntity<ProductCommand, ProductEvent, ProductState> {

    @Override
    public Behavior initialBehavior(Optional<ProductState> snapshotState) {

        BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(new ProductState("Nice", LocalDateTime.now().toString())));

        b.setCommandHandler(ProductCommand.CreateProduct.class, (cmd, ctx) ->
                ctx.thenPersist(new ProductEvent.ProductCreated(entityId(), cmd.message),
                        evt -> ctx.reply(Done.getInstance())));

        b.setEventHandler(ProductEvent.ProductCreated.class,
                evt -> new ProductState(evt.message, LocalDateTime.now().toString()));

        b.setReadOnlyCommandHandler(ProductCommand.GetProduct.class,
                (cmd, ctx) -> ctx.reply(state().message + ", " + cmd.name + "!"));

        return b.build();
    }
}
