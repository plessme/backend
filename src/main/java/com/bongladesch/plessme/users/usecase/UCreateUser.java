package com.bongladesch.plessme.users.usecase;

import org.jboss.logging.Logger;

import com.bongladesch.plessme.common.usecase.IGenerator;
import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.entity.User.UserBuilder;

/**
 * Usecase implementation of 'CreateUser'. This usecase creates a user account on registration of
 * new users. The account is created based on provided user data.
 */
public final class UCreateUser {

  private static final Logger LOGGER = Logger.getLogger(UCreateUser.class);

  private final IGenerator generator;
  private final IUserRepository repository;
  private final IIdentityProvider identityProvider;

  /**
   * Constructor of the 'CreateUserAccount' usecase object. All runtime dependencies for this
   * usecase must be injected on creation.
   *
   * @param repository Repository interface to manage the persistence of user account data
   * @param generator Generator interface to generate UUID and timetamp
   * @param identityProvider Interface to the identity provider to create login
   */
  public UCreateUser(
      final IGenerator generator,
      final IUserRepository repository,
      final IIdentityProvider identityProvider) {
    this.generator = generator;
    this.repository = repository;
    this.identityProvider = identityProvider;
  }

  /**
   * Create a user account by given user data to be able to login. This usecase includes the
   * creation of a 'login' in the identity provider, and sending an async message for other
   * components with the users ID. The user account related data are stored in the repository
   * database.
   *
   * @param userInput User input data to create a new user account
   * @return The generated ID of the new user account
   */
  public User create(final User userInput)
      throws UserValidationException, UserAlreadyExistsException {
    // Validate user input
    UserValidator.validateUser(userInput);
    if (repository.findByEmail(userInput.getEmail()) != null) {
      LOGGER.info("User with email-address " + userInput.getEmail() + " already exists");
      throw new UserAlreadyExistsException(userInput.getEmail());
    }
    // Create new user object and add ID + creation timestamp
    UserBuilder userBuilder = new UserBuilder();
    userBuilder
        .id(generator.generateId())
        .created(generator.generateTimestamp())
        .email(userInput.getEmail())
        .password(userInput.getPassword())
        .firstName(userInput.getFirstName())
        .lastName(userInput.getLastName());
    User user = userBuilder.build();
    // Create login at identity provider
    identityProvider.createUser(user);
    LOGGER.debug(
        "User with email "
            + user.getEmail()
            + " added to identity provider with id: "
            + user.getId());
    // Persist user data to database
    repository.create(user);
    LOGGER.info("Created new valid user account with id: " + user.getId() + " in user repository");
    return user;
  }
}
