package com.bongladesch.plessme.users.service;

import io.quarkus.oidc.UserInfo;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MockUserInfo extends UserInfo {

  @Override
  public String getString(String s) {
    return "UUID";
  }
}
