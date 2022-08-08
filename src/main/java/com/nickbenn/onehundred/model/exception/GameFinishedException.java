package com.nickbenn.onehundred.model.exception;

/**
 *
 */
public class GameFinishedException extends IllegalStateException {

  /**
   *
   */
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
