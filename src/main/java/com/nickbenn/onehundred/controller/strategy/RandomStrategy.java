/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.controller.strategy;

import com.nickbenn.onehundred.model.Game;

import java.util.Random;

/**
 *
 */
@SuppressWarnings("unused")
public class RandomStrategy implements Strategy {

  private final Random rng;

  /**
   *
   */
  public RandomStrategy() {
    this(new Random());
  }

  /**
   * @param rng
   */
  public RandomStrategy(Random rng) {
    this.rng = rng;
  }

  /**
   * @param game
   * @return
   */
  @Override
  public int getNextMove(Game game) {
    int winGap = game.getUpperBound() - game.getCurrentCount();
    int maxMove = game.getMaxMove();
    return (winGap <= maxMove) ? winGap : (rng.nextInt(maxMove) + 1);
  }

}
