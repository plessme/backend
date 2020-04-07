package com.bongladesch.plessme.users.usecase;

/**
 * IMessageSender provides the necessary methods to send
 * async events to other components.
 */
public interface IMessageSender {

    /**
     * Send the event that a new user is created
     * @param id payload of the event: users ID 
     */
    void userCreated(String id);
}