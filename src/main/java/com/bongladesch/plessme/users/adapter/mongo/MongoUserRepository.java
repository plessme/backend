package com.bongladesch.plessme.users.adapter.mongo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.usecase.IUserRepository;

/**
 * MongoUserRepository is a concret implementation of IUserRepositry to manage user data in a
 * MongoDB database.
 */
@ApplicationScoped
public class MongoUserRepository implements IUserRepository {

  private Logger logger;

  /**
   * Default CDI constructor.
   *
   * @param logger logger instance is injected
   */
  @Inject
  public MongoUserRepository(Logger logger) {
    this.logger = logger;
  }

  /**
   * Write a user initially to database by given user data.
   *
   * @param user user data to create the database entry from
   */
  @Override
  public void create(User user) {
    logger.debug("Persist the given user with id: " + user.getId() + " to MongoDB");
    MongoUser mongoUser = new MongoUser(user);
    mongoUser.persist();
  }

  /**
   * Find a user by e-mail in database.
   *
   * @param email the email to search for the user
   * @return user if found, null if not
   */
  @Override
  public User findByEmail(String email) {
    logger.debug("Find a user by given email: " + email + " in MongoDB");
    MongoUser user = MongoUser.findByEmail(email);
    if (user != null) {
      return user.toUser();
    }
    return null;
  }

  /**
   * Find a user by id in database.
   *
   * @param user the id to search for the user
   * @return user if found, null if not
   */
  @Override
  public User findById(String id) {
    logger.debug("Find a user by given id: " + id + " in MongoDB");
    MongoUser user = MongoUser.findById(id);
    if (user != null) {
      return user.toUser();
    }
    return null;
  }
}
