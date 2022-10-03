/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.controller.strategy;

import com.nickbenn.onehundred.model.Game;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.Random;

/**
 * Declares methods that must be implemented in a compl
 */
public abstract class Strategy {

  /** Contains keys and corresponding fully-qualified names of concrete subclasses of this class. */
  public static final String PROPERTIES_FILENAME = "strategies.properties";

  private static final String EXCEPTION_MESSAGE_FORMAT = "Strategy \"%s\" not found";

  private final Random rng;

  /**
   * Initializes the basic state of the strategy with the specified source of randomness. This is
   * intended for use by any concrete subclass that selects (at least under some conditions) a move
   * at random from those available.
   *
   * @param rng Source of randomness.
   */
  protected Strategy(Random rng) {
    this.rng = rng;
  }

  /**
   * Creates, initializes (with a default source of randomness), and returns an instance of the
   * concrete subclass specified by the property value (found in {@link #PROPERTIES_FILENAME}
   * corresponding to {@code key}.
   *
   * @param key Lookup key for fully-qualified name of concrete subclass of {@code Strategy}.
   * @return Instance of {@code Strategy} subclass.
   * @throws StrategyInitializationException If an instance of the specified subclass cannot be
   *                                         created or initialized.
   */
  public static Strategy newInstance(String key) throws StrategyInitializationException {
    return newInstance(key, new Random());
  }

  /**
   * @param key
   * @param rng
   * @return
   * @throws StrategyInitializationException
   */
  public static Strategy newInstance(String key, Random rng)
      throws StrategyInitializationException {
    try (
        InputStream input = Strategy.class
            .getClassLoader()
            .getResourceAsStream(PROPERTIES_FILENAME)
    ) {
      Properties properties = new Properties();
      properties.load(input);
      String className = properties.getProperty(key);
      //noinspection unchecked
      Class<? extends Strategy> klass = (Class<? extends Strategy>) Class.forName(className);
      Constructor<?> constructor = klass.getConstructor(Random.class);
      return (Strategy) constructor.newInstance(rng);
    } catch (InvocationTargetException e) {
      throw new StrategyInitializationException(
          String.format(EXCEPTION_MESSAGE_FORMAT, key), e.getCause());
    } catch (Exception e) {
      throw new StrategyInitializationException(String.format(EXCEPTION_MESSAGE_FORMAT, key), e);
    }
  }

  /**
   * @param game
   * @return
   */
  public abstract int getNextMove(Game game);

  /**
   * @return
   */
  protected Random getRng() {
    return rng;
  }

  /**
   *
   */
  public static class StrategyInitializationException extends Exception {

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

}
