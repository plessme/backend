package com.bongladesch.plessme.users.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bongladesch.plessme.common.adapter.logging.JBossLogger;
import com.bongladesch.plessme.common.adapter.util.MockGenerator;
import com.bongladesch.plessme.common.usecase.IGenerator;
import com.bongladesch.plessme.users.adapter.database.MockUserRepository;
import com.bongladesch.plessme.users.adapter.identity.MockIdentityProvider;
import com.bongladesch.plessme.users.adapter.sender.MockMessageSender;
import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.entity.User.UserBuilder;

import org.junit.jupiter.api.Test;

/**
 * Test implementation for usecase "CreateUser".
 */
public class TestCreateUser {

    private JBossLogger logger;
    private IGenerator generator;
    private MockUserRepository userRepository;
    private MockIdentityProvider identityProvider;
    private MockMessageSender messageSender;

    private User sharedUser;

    /**
     * Setup test environment by creating
     * dependencies and mocks to inject on test.
     */
    public TestCreateUser() {
        // Creation of dependencies and mocks
        this.logger = new JBossLogger();
        this.generator = new MockGenerator();
        this.userRepository = new MockUserRepository();
        this.identityProvider = new MockIdentityProvider();
        this.messageSender = new MockMessageSender();
        // Shared valid user object to test creation with valid input
        UserBuilder builder = new UserBuilder();
        builder.email("me@test.com").password("password").firstName("my").lastName("name");
        this.sharedUser = builder.build();
    }

    /**
     * Test the creation of an user with valid input.
     * Expected the user is created and ID and creation
     * timestamp is added to the returned user object.
     */
    @Test
    public void testCreateUser() {
        // Create user and execute usecase with injected mocks
        UCreateUser createUserAccount = new UCreateUser(logger, generator, userRepository, identityProvider, messageSender);
        User user = createUserAccount.create(sharedUser);
        // Assert statements
        assertEquals(user.getId(), "UUID");
        assertEquals(user.getCreated(), 123L);
        assertEquals(user.getEmail(), "me@test.com");
        assertEquals(user.getPassword(), "password");
        assertEquals(user.getFirstName(), "my");
        assertEquals(user.getLastName(), "name");
    }

    /**
     * Create an already existing user.
     * Expected a "UserAlreadyExists" exception.
     */
    @Test
    public void testUserAlreadyExists() {
        // Configure mock to simulate a already existing user account
        userRepository.alreadyExists();
        // Create user account
        UCreateUser createUserAccount = new UCreateUser(logger, generator, userRepository, identityProvider, messageSender);
        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            createUserAccount.create(sharedUser);
        });
        // Assert statements
        String expectedMessage = "me@test.com";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Create a user with invalid user data (empty password)
     * Expect a UserValidationException.
     */
    @Test
    public void testInvalidUserNoPassword() {
        // Create user object
        UserBuilder builder = new UserBuilder();
        builder.email("me@test.com").password("").firstName("my").lastName("name");
        // Create user account
        UCreateUser createUserAccount = new UCreateUser(logger, generator, userRepository, identityProvider, messageSender);
        Exception exception = assertThrows(UserValidationException.class, () -> {
            createUserAccount.create(builder.build());
        });
        // Assert statements
        String expectedMessage = "Provided password is null or emtpy.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Create a user with invalid user data (email is null)
     * Expect a UserValidationException.
     */
    @Test
    public void testInvalidUserEmptyEmail() {
        // Create user object
        UserBuilder builder = new UserBuilder();
        builder.password("password").firstName("my").lastName("name");
        // Create user account
        UCreateUser createUserAccount = new UCreateUser(logger, generator, userRepository, identityProvider, messageSender);
        Exception exception = assertThrows(UserValidationException.class, () -> {
            createUserAccount.create(builder.build());
        });
        // Assert statements
        String expectedMessage = "Provided e-mail address is null or emtpy.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}