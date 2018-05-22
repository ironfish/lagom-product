package com.example.product.impl;

import akka.Done;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class CreateProductEntityTest {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("CreateProductEntityTest");
    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void tesProduct() {
        PersistentEntityTestDriver<ProductCommand, ProductEvent, ProductState> driver =
                new PersistentEntityTestDriver<>(system, new ProductEntity(), "prod-1");

        PersistentEntityTestDriver.Outcome<ProductEvent, ProductState> outcome1 = driver.run(new ProductCommand.GetProduct("Socks"));
        assertEquals("Nice, Socks!", outcome1.getReplies().get(0));
        assertEquals(Collections.emptyList(), outcome1.issues());

        PersistentEntityTestDriver.Outcome<ProductEvent, ProductState> outcome2 = driver.run(new ProductCommand.CreateProduct("Cool"),
                new ProductCommand.GetProduct("Shirts"));
        assertEquals(1, outcome2.events().size());
        assertEquals(new ProductEvent.ProductCreated("prod-1", "Cool"), outcome2.events().get(0));
        assertEquals("Cool", outcome2.state().getMessage());
        assertEquals(Done.getInstance(), outcome2.getReplies().get(0));
        assertEquals("Cool, Shirts!", outcome2.getReplies().get(1));
        assertEquals(2, outcome2.getReplies().size());
        assertEquals(Collections.emptyList(), outcome2.issues());
    }
}
