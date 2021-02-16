package com.bongladesch.plessme.users.service;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.service.json.UserJSON;
import com.bongladesch.plessme.users.usecase.IUserRepository;

/** Test implementation for usecase "GetUser". */
@QuarkusTest
@Tag("integration")
class TestGetinputUsService {

  // Service dependencies
  @InjectMock private IUserRepository userRepository;

  // Test objects
  private static String inputUser =
      "{\n"
          + "  \"email\": \"tester@gmail.com\",\n"
          + "  \"password\": \"password\",\n"
          + "  \"firstName\": \"tester\",\n"
          + "  \"lastName\": \"tester\"\n"
          + "}";

  private User outputUser;

  // Initialize test data
  public TestGetinputUsService() {
    this.outputUser =
        new User.UserBuilder()
            .id("UUID")
            .created(123L)
            .email("tester@gmail.com")
            .password("password")
            .firstName("tester")
            .lastName("tester")
            .build();
  }

  /** Test get user from user repository */
  @Test
  @TestSecurity(
      user = "tester@gmail.com",
      roles = {"user"})
  void testGetUser() {
    // Given
    Mockito.when(userRepository.findById(any(String.class))).thenReturn(outputUser);
    given().body(inputUser).contentType(ContentType.JSON).when().post("/users");
    // When
    UserJSON rt =
        given()
            .when()
            .get("/users")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(UserJSON.class);
    // Then
    assertEquals("UUID", rt.getId());
    assertEquals(123L, rt.getCreated());
    assertEquals("tester@gmail.com", rt.getEmail());
    assertEquals("tester", rt.getFirstName());
    assertEquals("tester", rt.getLastName());
  }

  /** Test get user from user repository that not exists */
  @Test
  @TestSecurity(
      user = "tester@gmail.com",
      roles = {"user"})
  void testGetUserNotExists() {
    // Given
    Mockito.when(userRepository.findById(any(String.class))).thenReturn(null);
    given().body(inputUser).contentType(ContentType.JSON).when().post("/users");
    // When
    given().when().get("/users").then().assertThat().statusCode(404);
  }
}
