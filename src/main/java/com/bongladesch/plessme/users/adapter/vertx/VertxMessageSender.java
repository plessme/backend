package com.bongladesch.plessme.users.adapter.vertx;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.bongladesch.plessme.common.usecase.ILogger;
import com.bongladesch.plessme.users.usecase.IMessageSender;

import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;

/**
 * VertxMessageSender is a concret implementation of IMessageSender
 * to send messages over VertX event bus async for loose-coupling of components.
 */
@ApplicationScoped
public class VertxMessageSender implements IMessageSender {

    private EventBus eventBus;
    private ILogger logger;

    /**
     * Default constructor for CDI
     * @param eventBus injected event bus
     * @param logger injected logger
     */
    @Inject
    public VertxMessageSender(EventBus eventBus, ILogger logger) {
        this.eventBus = eventBus;
        this.logger = logger;
    }

    /**
     * Send a message "user_created" with the users ID as payload.
     * @param id the users id
     */
    public void userCreated(String id) {
        logger.debug("Send event that user with id: " + id + " is created");
        eventBus.<String>request("user_created", id).onItem().apply(Message::body);
    }
}