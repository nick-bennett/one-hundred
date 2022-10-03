/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.model.exception;

/**
 *
 */
public class IllegalConfigurationException extends IllegalArgumentException {

  /**
   *
   */
  public IllegalConfigurationException() {
  }

  /**
   * @param message
   */
  public IllegalConfigurationException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public IllegalConfigurationException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public IllegalConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

}
