package com.bongladesch.plessme.users.adapter.identity;

import javax.enterprise.context.ApplicationScoped;

import com.bongladesch.plessme.users.adapter.keycloak.KeycloakIdentityProvider;
import com.bongladesch.plessme.users.entity.User;

import io.quarkus.test.Mock;

@Mock
@ApplicationScoped 
public class MockIdentityProvider extends KeycloakIdentityProvider {

    @Override
    public void createUser(User user) {}
}