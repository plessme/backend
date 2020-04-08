package com.bongladesch.plessme.users.usecase;

import com.bongladesch.plessme.users.entity.User;

/** IIdentityProvider interface provides the methods to the identity providers API calls. */
public interface IIdentityProvider {

    /**
    * Create an enabled user with the necessary role to access plessme as "user".
    *
    * @param user input user data
    */
    void createUser(User user);
}
