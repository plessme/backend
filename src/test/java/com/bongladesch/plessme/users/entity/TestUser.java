package com.bongladesch.plessme.users.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.bongladesch.plessme.users.service.json.UserJSON;

class TestUser {

  // Test objects
  private User user1;
  private User user2;
  private UserJSON userJson;

  // Initialize test data
  public TestUser() {
    user1 =
        new User.UserBuilder()
            .id("UUID")
            .created(123L)
            .email("tester@gmail.com")
            .password("password")
            .firstName("tester")
            .lastName("tester")
            .build();
    user2 =
        new User.UserBuilder()
            .id("AnotherUUID")
            .created(123L)
            .email("another@gmail.com")
            .password("password")
            .firstName("tester")
            .lastName("tester")
            .build();
    userJson = new UserJSON();
  }

  /** Test equals() method with identical user */
  @Test
  void testEqualUser() {
    // When
    boolean rt = user1.equals(user1);
    // Then
    assertTrue(rt);
  }

  /** Test equals() method with different user */
  @Test
  void testNotEqualUser() {
    // When
    boolean rt = user1.equals(user2);
    // Then
    assertFalse(rt);
  }

  /** Test equals() method with non user object */
  @Test
  void testEqualNoUserObject() {
    // When
    boolean rt = user1.equals(userJson);
    // Then
    assertFalse(rt);
  }

  /** Test equals() method with null object */
  @Test
  void testEqualNullUserObject() {
    // When
    boolean rt = user1.equals(null);
    // Then
    assertFalse(rt);
  }

  /** Test hashCode() method */
  @Test
  void testHashCode() {
    // When
    int code = user1.hashCode();
    // Then
    assertEquals(user1.getId().hashCode(), code);
  }
}
