/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.controller.strategy;

import com.nickbenn.onehundred.model.Game;

import java.util.Random;

/**
 * Implements an optimal strategy for the normal (non-mis&#x00e8;re) form of the One Hundred game.
 * Note that a winning move is not always possible in all game states; a random move will be
 * selected by this strategy when this is the case.
 */
@SuppressWarnings("unused")
public class OptimalStrategy extends Strategy {

  /**
   * Initializes this strategy with the specified source of randomness.
   *
   * @param rng Source of randomness for random moves.
   */
  public OptimalStrategy(Random rng) {
    super(rng);
  }

  @Override
  public int getNextMove(Game game) {
    int maxMove = game.getMaxMove();
    int modulus = maxMove + 1;
    int gap = Math.abs(game.getUpperBound() * ((1 + game.getOperation().sign()) >> 1) - game.getCurrentCount());
    int remainder = gap % modulus;
    return (remainder > 0) ? remainder : Math.min(gap, getRng().nextInt(maxMove) + 1);
  }

}
