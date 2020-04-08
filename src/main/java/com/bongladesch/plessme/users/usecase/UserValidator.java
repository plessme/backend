package com.bongladesch.plessme.users.usecase;

import com.bongladesch.plessme.users.entity.User;

/** UserValidator validates a user object regarding all mandatory fields. */
public class UserValidator {

    /**
    * Validates if the users e-mail address and password are not null or emtpy.
    *
    * @param user User that needs to be validated.
    */
    public static void validateUser(User user) throws UserValidationException {
        // Check if email is null or emtpy string
        if (user.getEmail() == null || user.getEmail().length() == 0) {
            throw new UserValidationException("Provided e-mail address is null or emtpy.");
        }
        // Check if password is null or emtpy string
        if (user.getPassword() == null || user.getPassword().length() == 0) {
            throw new UserValidationException("Provided password is null or emtpy.");
        }
    }
}
