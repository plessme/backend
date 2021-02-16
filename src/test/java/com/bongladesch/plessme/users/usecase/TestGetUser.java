package com.bongladesch.plessme.users.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.bongladesch.plessme.users.adapter.mongo.MongoUserRepository;
import com.bongladesch.plessme.users.entity.User;

/*
 * Test implementation for usecase "GetUser".
 */
class TestGetUser {

  // Usecase dependencies
  private IUserRepository mockedUserRepository;

  // Test objects
  private UGetUser getUser;
  private User user;

  // Initialize test data
  public TestGetUser() {
    this.user =
        new User.UserBuilder()
            .id("UUID")
            .created(123L)
            .email("tester@gmail.com")
            .password("password")
            .firstName("tester")
            .lastName("tester")
            .build();
  }

  /** Setup clean usecase before eeach test. */
  @BeforeEach
  private void setup() {
    mockedUserRepository = Mockito.mock(MongoUserRepository.class);
    getUser = new UGetUser(mockedUserRepository);
  }

  /** Test get method on existing ID. */
  @Test
  void testGetUser() {
    // Given
    Mockito.when(mockedUserRepository.findById(any(String.class))).thenReturn(user);
    // When
    User rt = getUser.getUser(user.getId());
    // Then
    assertEquals(user, rt);
  }

  /** Test get method on not existing ID. */
  @Test
  void testGetUserNotExists() {
    // Given
    Mockito.when(mockedUserRepository.findById(any(String.class))).thenReturn(null);
    // When
    User rt = getUser.getUser(user.getId());
    // Then
    assertEquals(null, rt);
  }
}
