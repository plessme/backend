package com.bongladesch.plessme.users.adapter.identity;

import com.bongladesch.plessme.users.adapter.keycloak.KeycloakIdentityProvider;
import com.bongladesch.plessme.users.entity.User;
import io.quarkus.test.Mock;
import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MockIdentityProvider extends KeycloakIdentityProvider {

    @Override
    public void createUser(User user) {}
}
