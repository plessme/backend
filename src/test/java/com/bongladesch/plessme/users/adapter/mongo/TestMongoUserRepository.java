package com.bongladesch.plessme.users.adapter.mongo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.bongladesch.plessme.common.adapter.util.BasicGenerator;
import com.bongladesch.plessme.users.entity.User;

@QuarkusTest
@QuarkusTestResource(MongoTestResource.class)
@Tag("integration")
class TestMongoUserRepository {

  // Usecase dependencies
  private MongoUserRepository userRepository;
  private BasicGenerator generator;

  // Test objects
  private User validUser;

  @Inject
  public TestMongoUserRepository(Logger logger) {
    // Initialize dependencies
    this.userRepository = new MongoUserRepository(logger);
    this.generator = new BasicGenerator();
    // Initialize test data
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

  /** Test creation of a user in MongoDB and checking of find method returns expected user */
  @Test
  void testCreateAndFindUser() {
    // When
    userRepository.create(validUser);
    // Then
    User mongoUser = userRepository.findByEmail(validUser.getEmail());
    assertEquals(validUser, mongoUser);
  }

  /** Test find user by not existent email */
  @Test
  void testFindByEmailNotExists() {
    // When
    User mongoUser = userRepository.findByEmail("not-exists");
    // Then
    assertNull(mongoUser);
  }
}
