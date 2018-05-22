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
     * Example: curl http://localhost:9000/api/get_product/Socks
     */
    ServiceCall<NotUsed, String> get_product(String id);


    /**
     * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
     * "hi_Socks"}' http://localhost:9000/api/create_product/Socks
     */
    ServiceCall<ProductMessage, Done> create_product(String id);

    /**
     * This gets published to Kafka.
     */
    Topic<ProductEvent> productEvents();

    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("product").withCalls(
                pathCall("/api/product/:id", this::get_product),
                pathCall("/api/product/:id", this::create_product)
        ).withTopics(
                topic("product-events", this::productEvents)
                        // Kafka partitions messages, messages within the same partition will
                        // be delivered in order, to ensure that all messages for the same user
                        // go to the same partition (and hence are delivered in order with respect
                        // to that user), we configure a partition key strategy that extracts the
                        // name as the partition key.
                        .withProperty(KafkaProperties.partitionKeyStrategy(), ProductEvent::getName)
        ).withAutoAcl(true);
        // @formatter:on
    }
}