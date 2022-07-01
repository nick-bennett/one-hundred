package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.model.exception.IllegalConfigurationException;
import com.nickbenn.onehundred.view.GamePresentation;

public abstract class Referee {

    private static final String NULL_PRESENTATION_MESSAGE =
            "Value of presentation parameter must be a non-null reference to an instance of GamePresentation.";

    private final GamePresentation<?> presentation;
    private final Game game;

    Referee(Builder<?> builder) {
        if (builder.presentation == null) {
            throw new IllegalConfigurationException(NULL_PRESENTATION_MESSAGE);
        }
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

    Game getGame() {
        return game;
    }

    abstract void presentState();

    abstract void presentNextMove();

    abstract int getMove();

    void applyMove(int move) {
        game.play(move);
    }

    abstract void presentCompletedMove(Game.State state, int move);

    abstract void presentError(Object presentation);

    GamePresentation<?> getPresentation() {
        return presentation;
    }

    @SuppressWarnings("UnusedReturnValue")
    abstract static class Builder<B extends Builder<B>> {

        final GamePresentation<?> presentation;
        int target;
        int maxMove;
        Game.State initialState;

        protected Builder(GamePresentation<?> presentation) {
            this.presentation = presentation;
            target = Game.DEFAULT_TARGET;
            maxMove = Game.DEFAULT_MAX_MOVE;
            initialState = Game.State.PLAYER_ONE_MOVE;
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

        abstract B self();

    }

}
