/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.model.exception;

import com.nickbenn.onehundred.model.Game.Operation;
import com.nickbenn.onehundred.model.Game.State;

/**
 * Represents the exceptional condition arising when the initial configuration of a
 * {@link com.nickbenn.onehundred.model.Game} is not valid.
 * <p>All constructors in this class correspond directly to the constructors with the same parameter
 * types in {@link IllegalArgumentException}.</p>
 *
 * @see com.nickbenn.onehundred.model.Game#Game(Operation, int, int, State)
 */
@SuppressWarnings({"unused"})
public class IllegalConfigurationException extends IllegalArgumentException {

  public IllegalConfigurationException() {
  }

  public IllegalConfigurationException(String message) {
    super(message);
  }

  public IllegalConfigurationException(Throwable cause) {
    super(cause);
  }

  public IllegalConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

}
