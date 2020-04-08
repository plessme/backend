package com.bongladesch.plessme.users.adapter.sender;

import com.bongladesch.plessme.users.adapter.vertx.VertxMessageSender;
import io.quarkus.test.Mock;
import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MockMessageSender extends VertxMessageSender {

    public MockMessageSender() {
        super(null, null);
    }

    @Override
    public void userCreated(String id) {}
}
