package com.nickbenn.onehundred.strategy;

import com.nickbenn.onehundred.model.Game;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.Random;

/**
 *
 */
public interface Strategy {

  /**  */
  String PROPERTIES_FILENAME = "strategies.properties";

  /**
   *
   * @param key
   * @return
   * @throws StrategyInitializationException
   */
  static Strategy newInstance(String key) throws StrategyInitializationException {
    return newInstance(key, new Random());
  }

  /**
   *
   * @param key
   * @param rng
   * @return
   * @throws StrategyInitializationException
   */
  static Strategy newInstance(String key, Random rng) throws StrategyInitializationException {
    try (InputStream input = Strategy.class.getClassLoader()
        .getResourceAsStream(PROPERTIES_FILENAME)) {
      Properties properties = new Properties();
      properties.load(input);
      String className = properties.getProperty(key);
      //noinspection unchecked
      Class<? extends Strategy> klass = (Class<? extends Strategy>) Class.forName(className);
      Constructor<?> constructor = klass.getConstructor(Random.class);
      return (Strategy) constructor.newInstance(rng);
    } catch (InvocationTargetException e) {
      throw new StrategyInitializationException(
          String.format("Unable to create instance of %s strategy.", key), e.getCause());
    } catch (Exception e) {
      throw new StrategyInitializationException(
          String.format("Unable to create instance of %s strategy.", key), e);
    }
  }

  /**
   *
   * @param game
   * @return
   */
  int getNextMove(Game game);

}
