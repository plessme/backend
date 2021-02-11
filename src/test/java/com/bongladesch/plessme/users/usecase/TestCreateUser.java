package com.bongladesch.plessme.users.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bongladesch.plessme.common.adapter.logging.JBossLogger;
import com.bongladesch.plessme.common.adapter.util.MockGenerator;
import com.bongladesch.plessme.common.usecase.IGenerator;
import com.bongladesch.plessme.common.usecase.ILogger;
import com.bongladesch.plessme.users.adapter.keycloak.MockIdentityProvider;
import com.bongladesch.plessme.users.adapter.mongo.MockUserRepository;
import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.entity.User.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Test implementation for usecase "CreateUser".
 */
// @QuarkusTest
public class TestCreateUser {

  // Usecase dependencies
  private ILogger logger;
  private MockUserRepository userRepository;
  private IIdentityProvider identityProvider;
  private IGenerator generator;

  // Test objects
  private UCreateUser createUserUsecase;
  private User validUser;
  private User emptyPassUser;
  private User nullPassUser;
  private User emptyMailUser;
  private User nullMailUser;

  // Initialize test data
  public TestCreateUser() {
    validUser =
        new UserBuilder()
            .email("tester@gmail.com")
            .password("password")
            .firstName("tester")
            .lastName("tester")
            .build();
    emptyPassUser =
        new UserBuilder()
            .email("tester@gmail.com")
            .password("")
            .firstName("tester")
            .lastName("tester")
            .build();
    nullPassUser =
        new UserBuilder()
            .email("tester@gmail.com")
            .password(null)
            .firstName("tester")
            .lastName("tester")
            .build();
    emptyMailUser =
        new UserBuilder()
            .email("")
            .password("password")
            .firstName("tester")
            .lastName("tester")
            .build();
    nullMailUser =
        new UserBuilder()
            .email(null)
            .password("password")
            .firstName("tester")
            .lastName("tester")
            .build();
  }

  /** Setup clean usecase before eeach test. */
  @BeforeEach
  private void setup() {
    logger = new JBossLogger();
    userRepository = new MockUserRepository();
    generator = new MockGenerator();
    identityProvider = new MockIdentityProvider();
    createUserUsecase = new UCreateUser(logger, generator, userRepository, identityProvider);
  }

  /**
   * Test the creation of an user with valid input. Expected the user is created and ID and creation
   * timestamp is added to the returned user object.
   */
  @Test
  public void testCreateUser() {
    // When
    User user = createUserUsecase.create(validUser);
    // Then
    assertEquals(user.getId(), "UUID");
    assertEquals(user.getCreated(), 123L);
    assertEquals(user.getEmail(), "tester@gmail.com");
    assertEquals(user.getPassword(), "password");
    assertEquals(user.getFirstName(), "tester");
    assertEquals(user.getLastName(), "tester");
  }

  /** Create an already existing user. Expected a "UserAlreadyExists" exception. */
  @Test
  public void testUserAlreadyExists() {
    // Given
    userRepository.alreadyExists();
    // When
    Exception exception =
        assertThrows(
            UserAlreadyExistsException.class,
            () -> {
              createUserUsecase.create(validUser);
            });
    // Then
    assertTrue(exception.getMessage().contains("tester@gmail.com"));
  }

  /** Create a user with invalid user data (empty password) Expect a UserValidationException. */
  @Test
  public void testInvalidUserNoPassword() {
    // When
    Exception exception =
        assertThrows(
            UserValidationException.class,
            () -> {
              createUserUsecase.create(emptyPassUser);
            });
    // Then
    assertTrue(exception.getMessage().contains("Provided password is null or emtpy."));
  }

  /** Create a user with invalid user data (null as password) Expect a UserValidationException. */
  @Test
  public void testInvalidUserNullPassword() {
    // When
    Exception exception =
        assertThrows(
            UserValidationException.class,
            () -> {
              createUserUsecase.create(nullPassUser);
            });
    // Then
    assertTrue(exception.getMessage().contains("Provided password is null or emtpy."));
  }

  /**
   * Create a user with invalid user data (email is not passed) Expect a UserValidationException.
   */
  @Test
  public void testInvalidUserEmptyEmail() {
    // When
    Exception exception =
        assertThrows(
            UserValidationException.class,
            () -> {
              createUserUsecase.create(emptyMailUser);
            });
    // Then
    assertTrue(exception.getMessage().contains("Provided e-mail address is null or emtpy."));
  }

  /** Create a user with invalid user data (email is null) Expect a UserValidationException. */
  @Test
  public void testInvalidUserNullEmail() {
    // When
    Exception exception =
        assertThrows(
            UserValidationException.class,
            () -> {
              createUserUsecase.create(nullMailUser);
            });
    // Then
    assertTrue(exception.getMessage().contains("Provided e-mail address is null or emtpy."));
  }
}
