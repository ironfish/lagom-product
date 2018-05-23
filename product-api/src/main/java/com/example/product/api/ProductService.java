package com.example.product.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.broker.kafka.KafkaProperties;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;
import static com.lightbend.lagom.javadsl.api.Service.topic;

public interface ProductService extends Service {

    /**
     * Example: curl http://localhost:9000/api/products/product/1234
     */
    ServiceCall<NotUsed, String> getProduct(String id);


    /**
     * Example: curl -H "Content-Type: application/json" -X POST -d '{"name": "Socks", "cost": "13.00", "rating": "10"}' http://localhost:9000/api/products/1234
     */
    ServiceCall<ProductMessage, Done> createProduct(String id);

    /**
     * This gets published to Kafka.
     */
    Topic<ProductEvent> productEvents();

    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("product").withCalls(
                pathCall("/api/products/product/:id", this::getProduct),
                pathCall("/api/products/:id", this::createProduct)
        ).withTopics(
                topic("product-events", this::productEvents)
                        // Kafka partitions messages, messages within the same partition will
                        // be delivered in order, to ensure that all messages for the same user
                        // go to the same partition (and hence are delivered in order with respect
                        // to that user), we configure a partition key strategy that extracts the
                        // id as the partition key.
                        .withProperty(KafkaProperties.partitionKeyStrategy(), ProductEvent::getId)
        ).withAutoAcl(true);
        // @formatter:on
    }
}