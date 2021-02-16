package com.bongladesch.plessme.users.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bongladesch.plessme.common.adapter.util.BasicGenerator;
import com.bongladesch.plessme.common.usecase.IGenerator;
import com.bongladesch.plessme.users.adapter.keycloak.KeycloakIdentityProvider;
import com.bongladesch.plessme.users.adapter.mongo.MongoUserRepository;
import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.entity.User.UserBuilder;

/*
 * Test implementation for usecase "CreateUser".
 */
public class TestCreateUser {

  // Usecase dependencies
  private IGenerator mockedGenerator;
  private IUserRepository mockedUserRepository;
  private IIdentityProvider mockedIdentityProvider;

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
    mockedGenerator = Mockito.mock(BasicGenerator.class);
    mockedUserRepository = Mockito.mock(MongoUserRepository.class);
    mockedIdentityProvider = Mockito.mock(KeycloakIdentityProvider.class);
    createUserUsecase =
        new UCreateUser(mockedGenerator, mockedUserRepository, mockedIdentityProvider);
  }

  /**
   * Test the creation of an user with valid input. Expected the user is created and ID and creation
   * timestamp is added to the returned user object.
   */
  @Test
  void testCreateUser() {
    // Given
    Mockito.when(mockedGenerator.generateId()).thenReturn("UUID");
    Mockito.when(mockedGenerator.generateTimestamp()).thenReturn(123L);
    Mockito.doNothing().when(mockedUserRepository).create(any(User.class));
    Mockito.when(mockedIdentityProvider.createUser(any(User.class))).thenReturn(true);
    // When
    User user = createUserUsecase.create(validUser);
    // Then
    assertEquals("UUID", user.getId());
    assertEquals(123L, user.getCreated());
    assertEquals("tester@gmail.com", user.getEmail());
    assertEquals("password", user.getPassword());
    assertEquals("tester", user.getFirstName());
    assertEquals("tester", user.getLastName());
  }

  /** Create an already existing user. Expected a "UserAlreadyExists" exception. */
  @Test
  void testUserAlreadyExists() {
    // Given
    Mockito.when(mockedUserRepository.findByEmail(any(String.class))).thenReturn(validUser);
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
  void testInvalidUserNoPassword() {
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
  void testInvalidUserNullPassword() {
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
  void testInvalidUserEmptyEmail() {
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
  void testInvalidUserNullEmail() {
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
