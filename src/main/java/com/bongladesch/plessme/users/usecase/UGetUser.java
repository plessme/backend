package com.bongladesch.plessme.users.usecase;

import org.jboss.logging.Logger;

import com.bongladesch.plessme.users.entity.User;

public class UGetUser {

  private static final Logger LOGGER = Logger.getLogger(UGetUser.class);

  private final IUserRepository userRepository;

  /**
   * Constructor of the 'GetUserAccount' usecase object. All runtime dependencies for this usecase
   * must be injected on creation.
   *
   * @param userRepository Repository interface to manage the persistence of user account data
   */
  public UGetUser(IUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Get user object of the provided ID.
   *
   * @param id ID of the user
   * @return if exists the correponding user, else null
   */
  public User getUser(String id) {
    LOGGER.debug("Find user with ID: " + id);
    User user = userRepository.findById(id);
    if (user != null) {
      LOGGER.debug("User with ID " + id + " found.");
      return user;
    }
    LOGGER.debug("User with ID " + id + " not found.");
    return null;
  }
}
