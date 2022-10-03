/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.controller.strategy;

import com.nickbenn.onehundred.model.Game;

import java.util.Random;

/**
 * Implements a random strategy for the normal (non-mis&#x00e8;re) form of the One Hundred game.
 * This strategy selects move as follows:
 * <ul>
 *   <li><p>If an available move results in an immediate win, this move will be selected.</p></li>
 *   <li><p>If no available move results in an immediate win, one of the available moves will be
 *   selected at random, with all such moves equally likely to be selected (assuming that the source
 *   of randomness is unbiased).</p></li>
 * </ul>
 */
@SuppressWarnings("unused")
public class RandomStrategy extends Strategy {

  /**
   * Initializes this strategy with the specified source of randomness.
   *
   * @param rng Source of randomness for random moves.
   */
  public RandomStrategy(Random rng) {
    super(rng);
  }

  @Override
  public int getNextMove(Game game) {
    int winGap = game.getUpperBound() - game.getCurrentCount();
    int maxMove = game.getMaxMove();
    return (winGap <= maxMove) ? winGap : (getRng().nextInt(maxMove) + 1);
  }

}
