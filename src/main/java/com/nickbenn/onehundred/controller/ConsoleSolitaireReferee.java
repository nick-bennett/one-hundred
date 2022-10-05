/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.model.strategy.Strategy;
import com.nickbenn.onehundred.model.strategy.Strategy.StrategyInitializationException;
import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.view.GamePresentation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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

  /**
   * Key identifying (in {@code strategy.properties}) the subclass of {@link Strategy} used by
   * default.
   */
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
   * Concrete implementation of the Builder pattern used for constructing instances of
   * {@link ConsoleSolitaireReferee}.
   */
  @SuppressWarnings({"unused"})
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
     * Initialize this instance with the required parameter values. As is typical in implementations
     * of this pattern, properties not specified as arguments to this constructor have default
     * values, and may or may not be further specified using the methods of this class, as desired.
     *
     * @param presentation Instance of {@link GamePresentation GamePresentation&lt;T&gt;}
     *                     implementation that will be responsible for constructing view artifacts
     *                     presented to the user.
     * @param bundle       {@link ResourceBundle} providing (potentially localized) {@link String}
     *                     resources used by the {@link ConsoleSolitaireReferee}.
     */
    public Builder(GamePresentation<?> presentation, ResourceBundle bundle) {
      super(presentation);
      this.bundle = Objects.requireNonNull(bundle, NULL_BUNDLE_MESSAGE);
    }

    /**
     * Sets the {@link BufferedReader} instance that will be used by the
     * {@link ConsoleSolitaireReferee} to obtain user input. If not set, a {@link BufferedReader}
     * that reads from {@link System#in} will be used.
     *
     * @param input (See above.)
     * @return This {@link Builder} instance.
     */
    public Builder setInput(BufferedReader input) {
      this.input = Objects.requireNonNull(input);
      return self();
    }

    /**
     * Sets the {@link PrintStream} instance that will be used by the
     * {@link ConsoleSolitaireReferee} to present output to the user. If not set, {@link System#out}
     * will be used.
     *
     * @param output (See above.)
     * @return This {@link Builder} instance.
     */
    public Builder setOutput(PrintStream output) {
      this.output = Objects.requireNonNull(output);
      return self();
    }

    /**
     * Sets the {@link Strategy} instance that will be used by the {@link ConsoleSolitaireReferee}
     * to select the computer player's moves. If not set, an instance of the class identified (in
     * {@code strategy.properties}) by {@link ConsoleSolitaireReferee#DEFAULT_STRATEGY_KEY} will be
     * created and used.
     *
     * @param strategy (See above.)
     * @return This {@link Builder} instance.
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
