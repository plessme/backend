package com.bongladesch.plessme.users.adapter.keycloak;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.bongladesch.plessme.common.adapter.util.BasicGenerator;
import com.bongladesch.plessme.users.entity.User;

@QuarkusTest
@QuarkusTestResource(KeycloakTestResource.class)
@Tag("integration")
public class TestKeycloakIdentityProvider {

  // Usecase dependencies
  @Inject KeycloakIdentityProvider identityProvider;
  @Inject private BasicGenerator generator;

  // Test objects
  private User validUser;

  // Initialize test data
  @BeforeEach
  private void setup() {
    validUser =
        new User.UserBuilder()
            .id(generator.generateId())
            .created(generator.generateTimestamp())
            .email("tester@gmail.com")
            .password("password")
            .firstName("tester")
            .lastName("tester")
            .build();
  }

  /** Test creation of valid user in Keycloak */
  @Test
  public void testUserCreation() {
    // When
    boolean rt = identityProvider.createUser(validUser);
    // Then
    assertTrue(rt);
    // TODO extend assertion when GET method of client is available
  }
}
