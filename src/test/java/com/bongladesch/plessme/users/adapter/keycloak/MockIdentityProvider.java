package com.bongladesch.plessme.users.adapter.keycloak;

import com.bongladesch.plessme.users.entity.User;
import io.quarkus.test.Mock;
import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MockIdentityProvider extends KeycloakIdentityProvider {

  @Override
  public boolean createUser(User user) {
    return true;
  }
}
