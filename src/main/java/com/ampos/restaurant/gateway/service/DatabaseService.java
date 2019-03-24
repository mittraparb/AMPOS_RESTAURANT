package com.ampos.restaurant.gateway.service;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.helpers.TransactionTemplate;
import org.neo4j.kernel.configuration.BoltConnector;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class DatabaseService {
    private GraphDatabaseService graphDatabaseService;

    public GraphDatabaseService getGraphDatabaseService() {
        if (graphDatabaseService == null) {
            BoltConnector bolt = new BoltConnector("0");
            graphDatabaseService = new GraphDatabaseFactory()
                    .newEmbeddedDatabaseBuilder(new File("cache.db"))
                    .setConfig(GraphDatabaseSettings.pagecache_memory, "512M")
                    .setConfig(GraphDatabaseSettings.string_block_size, "60")
                    .setConfig(GraphDatabaseSettings.array_block_size, "300")
                    .setConfig(bolt.type, "BOLT")
                    .setConfig(bolt.enabled, "true")
                    .setConfig(bolt.listen_address, "localhost:7687")
                    .newGraphDatabase();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    graphDatabaseService.shutdown();
                }
            });
        }
        return graphDatabaseService;
    }

    public <T> T[] execute(String query, Function<Result, T[]> parsingFunction) {
        return new TransactionTemplate()
                .retries(4)
                .backoff(3, TimeUnit.SECONDS)
                .with(graphDatabaseService)
                .execute((Function<Transaction, T[]>) transaction -> parsingFunction.apply(graphDatabaseService.execute(query)));
    }

    public <T> T[] execute(String query, Map<String, Object> parameters, Function<Result, T[]> parsingFunction) {
        return new TransactionTemplate()
                .retries(4)
                .backoff(3, TimeUnit.SECONDS)
                .with(graphDatabaseService)
                .execute((Function<Transaction, T[]>) transaction -> parsingFunction.apply(graphDatabaseService.execute(query, parameters)));
    }

    public <T> T executeSingle(String query, Function<Result, T> parsingFunction) {
        return new TransactionTemplate()
                .retries(4)
                .backoff(3, TimeUnit.SECONDS)
                .with(graphDatabaseService)
                .execute((Function<Transaction, T>) transaction -> parsingFunction.apply(graphDatabaseService.execute(query)));
    }

    public <T> T executeSingle(String query, Map<String, Object> parameters, Function<Result, T> parsingFunction) {
        return new TransactionTemplate()
                .retries(4)
                .backoff(3, TimeUnit.SECONDS)
                .with(graphDatabaseService)
                .execute((Function<Transaction, T>) transaction -> parsingFunction.apply(graphDatabaseService.execute(query, parameters)));
    }
}
