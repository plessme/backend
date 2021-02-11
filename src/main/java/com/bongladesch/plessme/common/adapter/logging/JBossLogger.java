package com.bongladesch.plessme.common.adapter.logging;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

import com.bongladesch.plessme.common.usecase.ILogger;

/**
 * JBossLogger is a concret implementation of the ILogger inteface which is used by the "usecase"
 * layer of the PlessMe components.
 */
@ApplicationScoped
public class JBossLogger implements ILogger {

  /**
   * Initialize loggers instance. Since this bean is "ApplicationScoped" this object is used by the
   * whole application.
   */
  private final Logger LOGGER = Logger.getLogger(JBossLogger.class);

  /**
   * Write a message to the logs on "debug" level.
   *
   * @param message debug message
   */
  @Override
  public void debug(String message) {
    LOGGER.debug(message);
  }

  /**
   * Write a message the the logs on "info" level.
   *
   * @param message info message
   */
  @Override
  public void info(String message) {
    LOGGER.info(message);
  }

  /**
   * Write a message to the logs on "error" level and print the exceptions stack trace.
   *
   * @param message error message
   * @param exception catched/throwen exception
   */
  @Override
  public void error(String message, Exception exception) {
    LOGGER.error(message, exception);
  }
}
