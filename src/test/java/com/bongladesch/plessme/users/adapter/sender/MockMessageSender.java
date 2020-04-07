package com.bongladesch.plessme.users.adapter.sender;

import javax.enterprise.context.ApplicationScoped;

import com.bongladesch.plessme.users.adapter.vertx.VertxMessageSender;

import io.quarkus.test.Mock;

@Mock
@ApplicationScoped
public class MockMessageSender extends VertxMessageSender {

    public MockMessageSender() {
        super(null, null);
    }

    @Override
    public void userCreated(String id) {}

}