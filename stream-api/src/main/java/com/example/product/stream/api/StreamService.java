package com.example.product.stream.api;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.namedCall;

public interface StreamService extends Service {

    ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> directStream();

    ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> autonomousStream();

    @Override
    default Descriptor descriptor() {
        return named("stream")
                .withCalls(
                        namedCall("direct-stream", this::directStream),
                        namedCall("auto-stream", this::autonomousStream)
                ).withAutoAcl(true);
    }
}
