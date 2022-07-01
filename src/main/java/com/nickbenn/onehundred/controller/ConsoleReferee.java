package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.model.exception.IllegalConfigurationException;
import com.nickbenn.onehundred.strategy.Strategy;
import com.nickbenn.onehundred.view.GamePresentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

/**
 * Manages user interaction&mdash;with corresponding updates to the game state&mdash;through completion of a single
 * game. As the game progresses, an instance of {@link GamePresentation} is used to display the current state, and
 * prompt the user for the next move; all of this is orchestrated in a simple invocation of the {@link #play()}. This
 * class also uses an instance of {@link Strategy} for the computer's moves.
 */
public final class ConsoleReferee extends Referee {

    private static final String NULL_STRATEGY_MESSAGE =
            "strategy must be a non-null reference to an instance of a Strategy implementation.";
    private static final String NULL_BUNDLE_MESSAGE =
            "bundle must be a non-null reference to an instance of ResourceBundle.";
    private static final String NULL_INPUT_MESSAGE =
            "input parameter must be a non-null reference to an instance of InputStream.";
    private static final String DEFAULT_STRATEGY_KEY = "optimal";

    private final Strategy strategy;
    private final BufferedReader reader;
    private final String playerName;
    private final String computerName;

    /**
     * This is a summary.
     *
     * @param builder
     */
    private ConsoleReferee(Builder builder) {
        super(builder);
        if (builder.strategy == null) {
            throw new IllegalConfigurationException(NULL_STRATEGY_MESSAGE);
        }
        if (builder.bundle == null) {
            throw new IllegalConfigurationException(NULL_BUNDLE_MESSAGE);
        }
        if (builder.input == null) {
            throw new IllegalConfigurationException(NULL_INPUT_MESSAGE);
        }
        strategy = builder.strategy;
        reader = new BufferedReader(new InputStreamReader(builder.input));
        ResourceBundle bundle = builder.bundle;
        playerName = bundle.getString(Keys.PLAYER_NAME);
        computerName = bundle.getString(Keys.COMPUTER_NAME);
    }

    @Override
    void presentState() {
        System.out.print(getPresentation().stateRepresentation(getGame(), playerName, computerName));
    }

    @Override
    void presentNextMove() {
        System.out.print(getPresentation().nextMoveNotice(
                (getGame().getState() == Game.State.PLAYER_ONE_MOVE) ? playerName : computerName));
    }

    @Override
    int getMove() {
        try {
            Game game = getGame();
            return (game.getState() == Game.State.PLAYER_ONE_MOVE) ? getUserMove() : strategy.getNextMove(game);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void presentCompletedMove(Game.State state, int move) {
        System.out.print(getPresentation().movePresentation(move,
                (state == Game.State.PLAYER_ONE_MOVE ? playerName : computerName)));
    }

    @Override
    void presentError(Object presentation) {
        System.out.print(presentation);
    }

    private int getUserMove() throws IOException {
        System.out.print(getPresentation().movePrompt(getGame()));
        String input = reader.readLine().trim();
        return Integer.parseInt(input);
    }

    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public static class Builder extends Referee.Builder<Builder> {

        private final ResourceBundle bundle;

        private Strategy strategy;
        private InputStream input;

        public Builder(GamePresentation<?> presentation, ResourceBundle bundle)
                throws Strategy.StrategyInitializationException {
            super(presentation);
            this.bundle = bundle;
            strategy = Strategy.newInstance(DEFAULT_STRATEGY_KEY);
            input = System.in;
        }

        public Builder setStrategy(Strategy strategy) {
            this.strategy = strategy;
            return self();
        }

        public Builder setInputStream(InputStream input) {
            this.input = input;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ConsoleReferee build() {
            return new ConsoleReferee(this);
        }

    }

}
