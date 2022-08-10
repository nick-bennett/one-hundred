package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.model.Game.Operation;
import com.nickbenn.onehundred.view.GamePresentation;

import java.util.Objects;

/**
 *
 */
public abstract class Referee {

  private final GamePresentation<?> presentation;

  private final Game game;

  /**
   * @param builder
   */
  protected Referee(Builder<?> builder) {
    presentation = builder.presentation;
    game = new Game(builder.operation, builder.target, builder.maxMove, builder.initialState);
  }

  /**
   *
   */
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

  /**
   * @return
   */
  protected Game getGame() {
    return game;
  }

  /**
   *
   */
  protected abstract void presentState();

  /**
   *
   */
  protected abstract void presentNextMove();

  /**
   * @return
   */
  protected abstract int getMove();

  /**
   * @param move
   */
  protected void applyMove(int move) {
    game.play(move);
  }

  /**
   * @param state
   * @param move
   */
  protected abstract void presentCompletedMove(Game.State state, int move);

  /**
   * @param presentation
   */
  protected abstract void presentError(Object presentation);

  /**
   * @return
   */
  protected GamePresentation<?> getPresentation() {
    return presentation;
  }

  /**
   * @param <B>
   */
  @SuppressWarnings("UnusedReturnValue")
  protected abstract static class Builder<B extends Builder<B>> {

    private static final String NULL_PRESENTATION_MESSAGE =
        "presentation must be a non-null reference to an instance of GamePresentation.";

    private final GamePresentation<?> presentation;
    private Operation operation = Game.DEFAULT_OPERATION;
    private int target = Game.DEFAULT_UPPER_BOUND;
    private int maxMove = Game.DEFAULT_MAX_MOVE;
    private Game.State initialState = Game.State.PLAYER_ONE_MOVE;

    /**
     * @param presentation
     */
    protected Builder(GamePresentation<?> presentation) {
      this.presentation = Objects.requireNonNull(presentation, NULL_PRESENTATION_MESSAGE);
    }

    /**
     *
     * @param operation
     */
    public B setOperation(Operation operation) {
      this.operation = operation;
      return self();
    }

    /**
     * @param initialState
     * @return
     */
    public B setInitialState(Game.State initialState) {
      this.initialState = initialState;
      return self();
    }

    /**
     * @param target
     * @return
     */
    public B setTarget(int target) {
      this.target = target;
      return self();
    }

    /**
     * @param maxMove
     * @return
     */
    public B setMaxMove(int maxMove) {
      this.maxMove = maxMove;
      return self();
    }

    /**
     * @return
     */
    protected abstract Referee build();

    /**
     * @return
     */
    protected abstract B self();

  }

}
