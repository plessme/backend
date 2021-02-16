package com.bongladesch.plessme.users.usecase;

import com.bongladesch.plessme.users.entity.User;

/** IUserRepository provides the methods to handle users data in the database within. */
public interface IUserRepository {

  /**
   * Create a user into the database by given user data. The password of the user is not to store
   * since the the credentials are handled at the identity provider.
   *
   * @param user user data to create the database entry within
   */
  void create(User user);

  /**
   * Find a user by his "email" address in the database.
   *
   * @param email Email address of the user
   * @return the user with incomming email address if exist, else "null"
   */
  User findByEmail(String email);

  /**
   * Find a user by his "id" in the database.
   *
   * @param id ID of the user
   * @return the user with incomming ID if exists, else "null"
   */
  User findById(String id);
}
