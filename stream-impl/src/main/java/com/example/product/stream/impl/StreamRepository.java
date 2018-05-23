package com.example.product.stream.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class StreamRepository {
    private final CassandraSession uninitialisedSession;

    // Will return the session when the Cassandra tables have been successfully created
    private volatile CompletableFuture<CassandraSession> initialisedSession;

    @Inject
    public StreamRepository(CassandraSession uninitialisedSession) {
        this.uninitialisedSession = uninitialisedSession;
        // Eagerly create the session
        session();
    }

    private CompletionStage<CassandraSession> session() {
        // If there's no initialised session, or if the initialised session future completed
        // with an exception, then reinitialise the session and attempt to create the tables
        if (initialisedSession == null || initialisedSession.isCompletedExceptionally()) {
            initialisedSession = uninitialisedSession.executeCreateTable(
                    "CREATE TABLE IF NOT EXISTS product (id text PRIMARY KEY, name text, cost text, rating text)"
            ).thenApply(done -> uninitialisedSession).toCompletableFuture();
        }
        return initialisedSession;
    }

    CompletionStage<Done> updateProduct(String id, String name, String cost, String rating) {
        return session().thenCompose(session ->
                session.executeWrite("INSERT INTO product (id, name, cost, rating) VALUES (?, ?, ?, ?)",
                        id, name, cost, rating)
        );
    }

    CompletionStage<Optional<String>> getProduct(String name) {
        return session().thenCompose(session ->
                session.selectOne("SELECT name FROM product WHERE name = ?", name)
        ).thenApply(maybeRow -> maybeRow.map(row -> row.getString("name")));
    }
}
