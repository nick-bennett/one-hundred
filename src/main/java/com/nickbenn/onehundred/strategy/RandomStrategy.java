package com.nickbenn.onehundred.strategy;

import com.nickbenn.onehundred.model.Game;

import java.util.Random;

@SuppressWarnings("unused")
public class RandomStrategy implements Strategy {

    private final Random rng;

    public RandomStrategy() {
        this(new Random());
    }

    public RandomStrategy(Random rng) {
        this.rng = rng;
    }

    @Override
    public int getNextMove(Game game) {
        int winGap = game.getTarget() - game.getSum();
        int maxMove = game.getMaxMove();
        return (winGap <= maxMove) ? winGap : (rng.nextInt(maxMove) + 1);
    }
}
