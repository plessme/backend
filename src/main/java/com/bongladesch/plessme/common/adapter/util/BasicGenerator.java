package com.bongladesch.plessme.common.adapter.util;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import com.bongladesch.plessme.common.usecase.IGenerator;

/**
 * BasicGenerator is a basic, concret implementation of the
 * IGenerator interface which is used by the "usecase"
 * layer of all PlessMe components.
 */
@ApplicationScoped
public class BasicGenerator implements IGenerator {

    /**
     * Generate a unique ID and returns it.
     * @return generated ID as String
     */
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate a timestamp by current  system time.
     * @return generated timestamp as Long
     */
    @Override
    public Long generateTimestamp() {
        return System.currentTimeMillis();
    }
}