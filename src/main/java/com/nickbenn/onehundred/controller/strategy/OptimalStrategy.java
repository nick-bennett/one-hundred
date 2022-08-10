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
public class OptimalStrategy implements Strategy {

  private final Random rng;

  /**
   *
   */
  public OptimalStrategy() {
    this(new Random());
  }

  /**
   * @param rng
   */
  public OptimalStrategy(Random rng) {
    this.rng = rng;
  }

  /**
   * @param game
   * @return
   */
  @Override
  public int getNextMove(Game game) {
    int maxMove = game.getMaxMove();
    int modulus = maxMove + 1;
    int gap = Math.abs(game.getUpperBound() * ((1 + game.getOperation().getSign()) >> 1) - game.getCurrentCount());
    int remainder = gap % modulus;
    return (remainder > 0) ? remainder : Math.min(gap, rng.nextInt(maxMove) + 1);
  }

}
