package org.example;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;
import io.lettuce.core.*;

@OpenAPIDefinition(
        info = @Info(
                title = "libraryserver",
                version = "0.0"
        )
)
public class Application {

    public static void main(String[] args) {
        Client redisClient = RedisClient.create("://localhost");
        StatefulConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();
        //syncCommands.set("key", "Hello, Redis!");
        System.out.println(syncCommands.get("key"));
        connection.close();
        redisClient.shutdown();
        Micronaut.run(Application.class, args);
    }
}