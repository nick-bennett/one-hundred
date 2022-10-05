/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.model.exception;

/**
 * Represents the exceptional condition resulting from an attempt to make a move in a game that is
 * already completed.
 * <p>All constructors in this class correspond directly to the constructors with the same parameter
 * types in {@link IllegalStateException}.</p>
 *
 * @see com.nickbenn.onehundred.model.Game#play(int)
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public class GameFinishedException extends IllegalStateException {

  public GameFinishedException() {
  }

  /**
   * @param message
   */
  public GameFinishedException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public GameFinishedException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public GameFinishedException(String message, Throwable cause) {
    super(message, cause);
  }

}
