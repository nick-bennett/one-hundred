package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.view.GamePresentation;

import java.util.Objects;

public abstract class Referee {

    private final GamePresentation<?> presentation;
    private final Game game;

    protected Referee(Builder<?> builder) {
        presentation = builder.presentation;
        game = new Game(builder.target, builder.maxMove, builder.initialState);
    }

    public void play() {
        Game.State state;
        while (!(state = game.getState()).isTerminal()) {
            presentState();
            presentNextMove();
            while (true) {
                try {
                    int move = getMove();
                    applyMove(move);
                    presentCompletedMove(state, move);
                    break;
                } catch (IllegalArgumentException e) {
                    presentError(presentation.illegalMoveNotification(game));
                }
            }
        }
        presentState();
    }

    protected Game getGame() {
        return game;
    }

    protected abstract void presentState();

    protected abstract void presentNextMove();

    protected abstract int getMove();

    protected void applyMove(int move) {
        game.play(move);
    }

    protected abstract void presentCompletedMove(Game.State state, int move);

    protected abstract void presentError(Object presentation);

    protected GamePresentation<?> getPresentation() {
        return presentation;
    }

    @SuppressWarnings("UnusedReturnValue")
    protected abstract static class Builder<B extends Builder<B>> {

        private static final String NULL_PRESENTATION_MESSAGE =
                "presentation must be a non-null reference to an instance of GamePresentation.";

        private final GamePresentation<?> presentation;
        private int target = Game.DEFAULT_TARGET;
        private int maxMove = Game.DEFAULT_MAX_MOVE;
        private Game.State initialState = Game.State.PLAYER_ONE_MOVE;

        protected Builder(GamePresentation<?> presentation) {
            this.presentation = Objects.requireNonNull(presentation, NULL_PRESENTATION_MESSAGE);
        }

        public B setInitialState(Game.State initialState) {
            this.initialState = initialState;
            return self();
        }

        public B setTarget(int target) {
            this.target = target;
            return self();
        }

        public B setMaxMove(int maxMove) {
            this.maxMove = maxMove;
            return self();
        }

        protected abstract Referee build();

        protected abstract B self();

    }

}
