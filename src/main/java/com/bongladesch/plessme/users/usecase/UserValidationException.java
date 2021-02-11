package com.bongladesch.plessme.users.usecase;

/**
 * UserValidationException will be thrown if a user creates an account with insufficient user data.
 */
public class UserValidationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public UserValidationException(final String message) {
    super(message);
  }
}
