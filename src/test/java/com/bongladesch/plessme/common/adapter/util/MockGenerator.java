package com.bongladesch.plessme.common.adapter.util;

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