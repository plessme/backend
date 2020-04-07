package com.bongladesch.plessme.users.usecase;

/**
 * UserAlreadyExistsException will be thrown if a user
 * creates an account with an already used e-mail address.
 */
public class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException(final String email) {
	    super(email);
	}
}