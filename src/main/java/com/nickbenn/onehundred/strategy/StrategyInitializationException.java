package com.nickbenn.onehundred.strategy;

/**
 *
 */
public class StrategyInitializationException extends Exception {

  /**
   *
   */
  public StrategyInitializationException() {
  }

  /**
   * @param message
   */
  public StrategyInitializationException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public StrategyInitializationException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public StrategyInitializationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public StrategyInitializationException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
