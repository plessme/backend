package com.bongladesch.plessme.common.adapter.util;

import io.quarkus.test.Mock;
import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MockGenerator extends BasicGenerator {

  @Override
  public String generateId() {
    return "UUID";
  }

  @Override
  public Long generateTimestamp() {
    return 123L;
  }
}
