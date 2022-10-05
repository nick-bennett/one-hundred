/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.model.exception;

/**
 * Represents the exceptional condition resulting from an attempt to make an invalid move in an
 * ongoing game.
 * <p>All constructors in this class correspond directly to the constructors with the same parameter
 * types in {@link IllegalArgumentException}.</p>
 *
 * @see com.nickbenn.onehundred.model.Game#play(int)
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public class IllegalMoveException extends IllegalArgumentException {

  public IllegalMoveException() {
  }

  /**
   * @param message
   */
  public IllegalMoveException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public IllegalMoveException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public IllegalMoveException(String message, Throwable cause) {
    super(message, cause);
  }

}
