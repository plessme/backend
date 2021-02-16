package com.bongladesch.plessme.users.adapter.keycloak;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class KeycloakTestResource implements QuarkusTestResourceLifecycleManager {

  static String keycloakUsername = "keycloak";
  static String keycloakPassword = "keycloak";
  static GenericContainer<?> keycloak =
      new GenericContainer<>(DockerImageName.parse("jboss/keycloak:12.0.2"))
          .withExposedPorts(8080)
          .withEnv("KEYCLOAK_USER", keycloakUsername)
          .withEnv("KEYCLOAK_PASSWORD", keycloakPassword)
          .withEnv(
              "KEYCLOAK_IMPORT",
              "/tmp/kc/plessme-realm.json -Dkeycloak.profile.feature.upload_scripts=enabled")
          .withClasspathResourceMapping(
              "plessme-realm.json", "/tmp/kc/plessme-realm.json", BindMode.READ_ONLY);

  @Override
  public Map<String, String> start() {
    keycloak.start();
    return Map.of(
        "plessme.keycloak.url",
        "http://"
            + keycloak.getContainerIpAddress()
            + ":"
            + keycloak.getFirstMappedPort()
            + "/auth");
  }

  @Override
  public void stop() {
    keycloak.stop();
  }
}
