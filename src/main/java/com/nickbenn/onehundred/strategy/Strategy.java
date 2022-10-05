/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.strategy;

import com.nickbenn.onehundred.model.Game;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.Random;

/**
 * Declares the {@code abstract} method {@link #getNextMove(Game)} (which must be implemented in a
 * concrete subclass), as well as the concrete method {@link #getRng()} (providing a source of
 * randomness to a concrete implementation), along with the overloaded {@link #newInstance(String)}
 * and {@link #newInstance(String, Random)} {@code static} factory methods.
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
   * concrete subclass specified by the property value (found in {@link #PROPERTIES_FILENAME})
   * corresponding to {@code key}.
   * <p>Invoking {@code Strategy.newInstance(key)} equivalent to invoking
   * {@link #newInstance(String, Random) Strategy.newInstance(key, new Random())}.</p>
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
   * Creates, initializes, and returns an instance of the concrete subclass specified by the
   * property value (found in {@link #PROPERTIES_FILENAME}) corresponding to {@code key}.
   *
   * @param key Property key used to look up (in {@link #PROPERTIES_FILENAME}) fully qualified
   *            class name of the concrete subclass of this class.
   * @param rng Source of randomness
   * @return Instance of specified concrete subclass of this class.
   * @throws StrategyInitializationException If {@code key} does not exist, the corresponding class does not exist or can't be instantiated.
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
   * Computes and returns the next move, according to this strategy.
   *
   * @param game Context of the strategy.
   * @return {@code int} size of the move.
   */
  public abstract int getNextMove(Game game);

  /**
   * Returns the source of randomness, for use by a concrete subclass.
   *
   * @return Source of randomness.
   */
  protected Random getRng() {
    return rng;
  }

  /**
   * Encapsulates an unchecked exception, thrown when instantiation of a concrete subclass of
   * {@link Strategy} fails, <em>for any reason</em>.
   */
  @SuppressWarnings("unused")
  public static class StrategyInitializationException extends RuntimeException {

    public StrategyInitializationException() {
    }

    public StrategyInitializationException(String message) {
      super(message);
    }

    public StrategyInitializationException(Throwable cause) {
      super(cause);
    }

    public StrategyInitializationException(String message, Throwable cause) {
      super(message, cause);
    }

  }

}
