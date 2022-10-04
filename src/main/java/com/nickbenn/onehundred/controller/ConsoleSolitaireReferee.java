/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.controller.strategy.Strategy;
import com.nickbenn.onehundred.controller.strategy.Strategy.StrategyInitializationException;
import com.nickbenn.onehundred.view.GamePresentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Manages user interaction&mdash;with corresponding updates to the game state&mdash;through
 * completion of a single solitaire (user against computer) game. As the game progresses, an
 * instance of {@link GamePresentation} is used to construct the text content displayed to the user,
 * and user input is read from the console (or some other {@link java.io.InputStream}); all of this
 * is orchestrated in a simple invocation of {@link #play()}. The actions of the computer opponent
 * are provided by an instance of {@link Strategy}.
 */
public final class ConsoleSolitaireReferee extends Referee {

  public static final String DEFAULT_STRATEGY_KEY = "optimal";

  private final Strategy strategy;
  private final BufferedReader input;
  private final PrintStream output;
  private final String playerName;
  private final String computerName;

  private ConsoleSolitaireReferee(Builder builder) {
    super(builder);
    strategy = builder.strategy;
    input = builder.input;
    output = builder.output;
    ResourceBundle bundle = builder.bundle;
    playerName = bundle.getString(Keys.PLAYER_NAME);
    computerName = bundle.getString(Keys.COMPUTER_NAME);
  }

  @Override
  protected void presentState() {
    output.print(getPresentation().stateRepresentation(getGame(), playerName, computerName));
  }

  @Override
  protected void presentNextMove() {
    output.print(getPresentation().nextMoveNotice(
        (getGame().getState() == Game.State.PLAYER_ONE_MOVE) ? playerName : computerName));
  }

  @Override
  protected int getMove() {
    try {
      Game game = getGame();
      return (game.getState() == Game.State.PLAYER_ONE_MOVE) ? getUserMove()
          : strategy.getNextMove(game);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void presentCompletedMove(Game.State state, int move) {
    System.out.print(getPresentation().movePresentation(move,
        (state == Game.State.PLAYER_ONE_MOVE ? playerName : computerName)));
  }

  @Override
  protected void presentError(Object presentation) {
    System.out.print(presentation);
  }

  private int getUserMove() throws IOException {
    output.print(getPresentation().movePrompt(getGame()));
    String input = this.input.readLine().trim();
    return Integer.parseInt(input);
  }

  /**
   *
   */
  @SuppressWarnings({"UnusedReturnValue", "unused"})
  public static class Builder extends Referee.Builder<Builder> {

    private static final String NULL_STRATEGY_MESSAGE =
        "strategy must be a non-null reference to an instance of a Strategy implementation.";
    private static final String NULL_BUNDLE_MESSAGE =
        "bundle must be a non-null reference to an instance of ResourceBundle.";
    private static final String NULL_INPUT_MESSAGE =
        "input parameter must be a non-null reference to an instance of InputStream.";

    private final ResourceBundle bundle;

    private BufferedReader input;
    private PrintStream output;
    private Strategy strategy;

    /**
     * @param presentation
     * @param bundle
     * @throws StrategyInitializationException
     */
    public Builder(GamePresentation<?> presentation, ResourceBundle bundle) {
      super(presentation);
      this.bundle = Objects.requireNonNull(bundle, NULL_BUNDLE_MESSAGE);
    }

    public Builder setInput(BufferedReader input) {
      this.input = Objects.requireNonNull(input);
      return self();
    }

    public Builder setOutput(PrintStream output) {
      this.output = Objects.requireNonNull(output);
      return self();
    }

    /**
     * @param strategy
     * @return
     */
    public Builder setStrategy(Strategy strategy) {
      this.strategy = Objects.requireNonNull(strategy, NULL_STRATEGY_MESSAGE);
      return self();
    }

    @Override
    protected Builder self() {
      return this;
    }

    @Override
    public ConsoleSolitaireReferee build() throws StrategyInitializationException {
      input = (input != null) ? input : new BufferedReader(new InputStreamReader(System.in));
      output = (output != null) ? output : System.out;
      strategy = (strategy != null) ? strategy : Strategy.newInstance(DEFAULT_STRATEGY_KEY);
      return new ConsoleSolitaireReferee(this);
    }

  }

}
