package com.bongladesch.plessme.users.usecase;

import com.bongladesch.plessme.common.usecase.IGenerator;
import com.bongladesch.plessme.common.usecase.ILogger;
import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.entity.User.UserBuilder;

/**
* Usecase implementation of 'CreateUser'. This usecase creates a user account on registration of
* new users. The account is created based on provided user data.
*/
public final class UCreateUser {

    private final ILogger logger;
    private final IGenerator generator;
    private final IUserRepository repository;
    private final IIdentityProvider identityProvider;

    /**
    * Constructor of the 'CreateUserAccount' usecase object. All runtime dependencies for this
    * usecase must be injected on creation.
    *
    * @param repository Repository interface to manage the persistence of user account data
    * @param generator Generator interface to generate UUID and timetamp
    * @param logger Logging interface to log on different levels during the usecase process
    * @param identityProvider Interface to the identity provider to create login
    */
    public UCreateUser(
            final ILogger logger,
            final IGenerator generator,
            final IUserRepository repository,
            final IIdentityProvider identityProvider) {
        this.logger = logger;
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
            logger.info("User with email-address " + userInput.getEmail() + " already exists");
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
                .lastName(userInput.getLastName())
                .build();
        User user = userBuilder.build();
        // Create login at identity provider
        identityProvider.createUser(user);
        logger.debug(
                "User with email "
                        + user.getEmail()
                        + " added to identity provider with id: "
                        + user.getId());
        // Persist user data to database
        repository.create(user);
        logger.info("Created new valid user account with id: " + user.getId() + " in user repository");
        return user;
    }
}
