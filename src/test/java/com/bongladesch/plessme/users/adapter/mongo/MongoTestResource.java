package com.bongladesch.plessme.users.adapter.mongo;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoTestResource implements QuarkusTestResourceLifecycleManager {

  static String username = "mongo";
  static String password = "mongo";
  static GenericContainer<?> mongo =
      new GenericContainer<>(DockerImageName.parse("mongo:4.4"))
          .withExposedPorts(27017)
          .withEnv("MONGO_INITDB_ROOT_USERNAME", username)
          .withEnv("MONGO_INITDB_ROOT_PASSWORD", password);

  @Override
  public Map<String, String> start() {
    mongo.start();
    return Map.of(
        "quarkus.mongodb.connection-string",
        "mongodb://"
            + username
            + ":"
            + password
            + "@"
            + mongo.getContainerIpAddress()
            + ":"
            + mongo.getFirstMappedPort());
  }

  @Override
  public void stop() {
    mongo.stop();
  }
}
