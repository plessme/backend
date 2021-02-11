package com.bongladesch.plessme.common.usecase;

/**
 * IGenerator defines an interface for a generater implementation util for unique IDs and other
 * common generated things like timestamps etc.
 */
public interface IGenerator {

  /**
   * Generate a unique ID and return as string.
   *
   * @return generated ID
   */
  String generateId();

  /**
   * Generate timestamp by current system time.
   *
   * @return generated timestamp as Long
   */
  Long generateTimestamp();
}
