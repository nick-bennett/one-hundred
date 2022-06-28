package com.nickbenn.onehundred.strategy;

import com.nickbenn.onehundred.model.Game;

import java.util.Random;

public class OptimalStrategy implements Strategy {

    private final Random rng;

    public OptimalStrategy() {
        this(new Random());
    }

    public OptimalStrategy(Random rng) {
        this.rng = rng;
    }

    @Override
    public int getNextMove(Game game) {
        int maxMove = game.getMaxMove();
        int modulus = maxMove + 1;
        int gap = game.getTarget() - game.getSum();
        int remainder = gap % modulus;
        return (remainder > 0) ? remainder : Math.min(gap, rng.nextInt(maxMove) + 1);
    }

}
