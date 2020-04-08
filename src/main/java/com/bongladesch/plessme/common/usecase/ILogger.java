package com.bongladesch.plessme.common.usecase;

/** ILogger is the interface used by the "usecase" layer of PlessMe for logging. */
public interface ILogger {

    /**
    * Write a message to the logs on "debug" level.
    *
    * @param message debug message
    */
    void debug(String message);

    /**
    * Write a message the the logs on "info" level.
    *
    * @param message info message
    */
    void info(String message);

    /**
    * Write a message to the logs on "error" level and print the exceptions stack trace.
    *
    * @param message error message
    * @param exception catched/throwen exception
    */
    void error(String message, Exception exception);
}
