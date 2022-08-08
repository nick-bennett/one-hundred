package com.nickbenn.onehundred.model.exception;

/**
 *
 */
public class IllegalMoveException extends IllegalArgumentException {

  /**
   *
   */
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
