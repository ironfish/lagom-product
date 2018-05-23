package com.example.product.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public class ProductEntity extends PersistentEntity<ProductCommand, ProductEvent, ProductState> {

    @Override
    public Behavior initialBehavior(Optional<ProductState> snapshotState) {

        @SuppressWarnings("unchecked")
        BehaviorBuilder b = newBehaviorBuilder(snapshotState.orElse(
                new ProductState(
                        "Does not exist",
                        "0.00",
                        "0",
                        LocalDateTime.now().toString())));


        b.setCommandHandler(ProductCommand.CreateProduct.class, (cmd, ctx) ->
                ctx.thenPersist(new ProductEvent.ProductCreated(
                        entityId(),
                        cmd.getName(),
                        cmd.getCost(),
                        cmd.getRating()),
                        evt -> ctx.reply(Done.getInstance())));

        b.setEventHandler(ProductEvent.ProductCreated.class, evt ->
                new ProductState(
                        evt.getName(),
                        evt.getCost(),
                        evt.getRating(),
                        LocalDateTime.now().toString()));

        b.setReadOnlyCommandHandler(ProductCommand.GetProduct.class, (cmd, ctx) -> {
            final String msg = state().getName() + ", "
                    + state().getCost() + ", "
                    + state().getRating() + ", "
                    + cmd.getName();
            ctx.reply(msg);
        });

        return b.build();
    }
}
