package com.bongladesch.plessme.users.service.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestUserApiObject {

  // Test objects
  private UserJSON user;

  // Initialize test data
  public TestUserApiObject() {
    user = new UserJSON();
    user.setEmail("tester@gmail.com");
    user.setPassword("password");
    user.setFirstName("tester");
    user.setLastName("tester");
  }

  /** Test toString() method return expected string */
  @Test
  void testToString() {
    // When
    String s = user.toString();
    // Then
    assertEquals(
        "{\n\t'email':'tester@gmail.com',\n\t'password':'********',\n\t'firstName':'tester',\n\t'lastName':'tester'\n}",
        s);
  }
}
