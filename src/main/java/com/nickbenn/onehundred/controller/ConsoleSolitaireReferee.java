package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.controller.strategy.Strategy;
import com.nickbenn.onehundred.controller.strategy.Strategy.StrategyInitializationException;
import com.nickbenn.onehundred.view.GamePresentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Manages user interaction&mdash;with corresponding updates to the game state&mdash;through
 * completion of a single solitaire (player vs&#x2D; computer) game. As the game progresses, an
 * instance of {@link GamePresentation} is used to construct the text content displayed to the user,
 * and user input is read from {@link System#in}; all of this is orchestrated in a simple invocation
 * of {@link #play()}. The actions of the computer opponent are provided by an instance of
 * {@link Strategy}.
 */
public final class ConsoleSolitaireReferee extends Referee {

  public static final String DEFAULT_STRATEGY_KEY = "optimal";

  private final Strategy strategy;
  private final BufferedReader reader;
  private final String playerName;
  private final String computerName;

  /**
   * This is a summary.
   *
   * @param builder
   */
  private ConsoleSolitaireReferee(Builder builder) {
    super(builder);
    strategy = builder.strategy;
    reader = new BufferedReader(new InputStreamReader(builder.input));
    ResourceBundle bundle = builder.bundle;
    playerName = bundle.getString(Keys.PLAYER_NAME);
    computerName = bundle.getString(Keys.COMPUTER_NAME);
  }

  @Override
  protected void presentState() {
    System.out.print(getPresentation().stateRepresentation(getGame(), playerName, computerName));
  }

  @Override
  protected void presentNextMove() {
    System.out.print(getPresentation().nextMoveNotice(
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
    System.out.print(getPresentation().movePrompt(getGame()));
    String input = reader.readLine().trim();
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

    private Strategy strategy;
    private InputStream input;

    /**
     * @param presentation
     * @param bundle
     * @throws StrategyInitializationException
     */
    public Builder(GamePresentation<?> presentation, ResourceBundle bundle)
        throws StrategyInitializationException {
      super(presentation);
      this.bundle = Objects.requireNonNull(bundle, NULL_BUNDLE_MESSAGE);
      strategy = Strategy.newInstance(DEFAULT_STRATEGY_KEY);
      input = System.in;
    }

    /**
     * @param strategy
     * @return
     */
    public Builder setStrategy(Strategy strategy) {
      this.strategy = Objects.requireNonNull(strategy, NULL_STRATEGY_MESSAGE);
      return self();
    }

    /**
     * @param input
     * @return
     */
    public Builder setInputStream(InputStream input) {
      this.input = Objects.requireNonNull(input, NULL_INPUT_MESSAGE);
      return self();
    }

    @Override
    protected Builder self() {
      return this;
    }

    @Override
    public ConsoleSolitaireReferee build() {
      return new ConsoleSolitaireReferee(this);
    }

  }

}
