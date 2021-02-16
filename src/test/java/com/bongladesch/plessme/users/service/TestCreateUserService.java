package com.bongladesch.plessme.users.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bongladesch.plessme.common.usecase.IGenerator;
import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.usecase.IIdentityProvider;
import com.bongladesch.plessme.users.usecase.IUserRepository;

/** API Implementation test of CreateUser usecase. */
@QuarkusTest
@Tag("integration")
class TestCreateUserService {

  // Service dependencies
  @InjectMock private IGenerator generator;
  @InjectMock private IUserRepository userRepository;
  @InjectMock private IIdentityProvider identityProvider;

  // Test objects
  private static String validTestUser =
      "{\n"
          + "  \"email\": \"tester@gmail.com\",\n"
          + "  \"password\": \"password\",\n"
          + "  \"firstName\": \"tester\",\n"
          + "  \"lastName\": \"tester\"\n"
          + "}";

  private static String invalidTestUser =
      "{\n"
          + "  \"email\": \"tester@gmail.com\",\n"
          + "  \"firstName\": \"tester\",\n"
          + "  \"lastName\": \"tester\"\n"
          + "}";

  /** Setup test data. */
  @BeforeEach
  private void setup() {
    Mockito.when(generator.generateId()).thenReturn("UUID");
    Mockito.when(identityProvider.createUser(any(User.class))).thenReturn(true);
  }

  /** Test creation of valid an non-existing user. */
  @Test
  void testCreateValidUser() {
    given()
        .body(validTestUser)
        .contentType(ContentType.JSON)
        .when()
        .post("/users")
        .then()
        .statusCode(201)
        .body(is("{\"id\":\"UUID\"}"));
  }

  /** Test creation of invalid user. */
  @Test
  void testCreateInvalidUser() {
    given()
        .body(invalidTestUser)
        .contentType(ContentType.JSON)
        .when()
        .post("/users")
        .then()
        .statusCode(400);
  }

  /** Test creation of user that already exists. */
  @Test
  void testCreateExistingUser() {
    Mockito.when(userRepository.findByEmail("tester@gmail.com"))
        .thenReturn(new User.UserBuilder().email("tester@gmail.com").build());
    given()
        .body(validTestUser)
        .contentType(ContentType.JSON)
        .when()
        .post("/users")
        .then()
        .statusCode(403);
  }
}
