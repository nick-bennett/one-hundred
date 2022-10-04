/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.model.Game.Operation;
import com.nickbenn.onehundred.model.Game.State;
import com.nickbenn.onehundred.view.GamePresentation;

import java.util.Objects;

/**
 * Encapsulates the enforcement of the rules of the game, along with updates to the game state. In
 * this {@code abstract} class, no assumptions are made regarding the UI mechanisms; a concrete
 * subclass must coordinate any necessary user interaction functionality, leveraging a
 * {@link GamePresentation GamePresentation&lt;T&gt;} instance, provided via the
 * {@link Builder#Builder(GamePresentation)} instantiation.
 */
public abstract class Referee {

  private final GamePresentation<?> presentation;

  private final Game game;

  /**
   * Initializes this instance&mdash;specifically, instantiating {@link Game} and incorporating the
   * {@link GamePresentation GamePresentation&lt;T&gt;} instance passed to
   * {@link Builder#Builder(GamePresentation)}.
   *
   * @param builder Previously configured instance of {@link Builder}.
   */
  protected Referee(Builder<?> builder) {
    presentation = builder.presentation;
    game = new Game(builder.operation, builder.target, builder.maxMove, builder.initialState);
  }

  /**
   * Orchestrates the changes of state and user interaction through completion of a single
   * {@link Game}, using an instance of {@link GamePresentation GamePresentation&lt;T&gt;} provided
   * via {@link Builder#Builder(GamePresentation)}. In many cases, there will be no need to override
   * this method.
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
   * Returns the {@link Game} instance representing the game currently being played.
   *
   * @return
   */
  protected Game getGame() {
    return game;
  }

  /**
   * Presents the current state of the game, incorporating content provided by the encapsulated
   * {@link GamePresentation GamePresentation&lt;T&gt;}. A concrete subclass must implement this
   * method appropriately for the user interaction mode employed by that subclass, with view objects
   * of subtypes of that specified in the {@link GamePresentation GamePresentation&lt;T&gt;} type
   * parameter.
   */
  protected abstract void presentState();

  /**
   * Presents preparatory information for the next move, incorporating content provided by the
   * encapsulated {@link GamePresentation GamePresentation&lt;T&gt;}. A concrete subclass must
   * implement this method appropriately for the user interaction mode employed by that subclass,
   * with view objects of subtypes of that specified in the
   * {@link GamePresentation GamePresentation&lt;T&gt;} type parameter.
   */
  protected abstract void presentNextMove();

  /**
   * Obtains the move from the current player (which may be the computer). A concrete subclass must
   * implement this method appropriately for the user interaction mode employed by that subclass,
   * with view objects of subtypes of that specified in the
   * {@link GamePresentation GamePresentation&lt;T&gt;} type parameter.
   *
   * @return
   */
  protected abstract int getMove();

  /**
   * Updates the state of the current {@link Game} instance, according to the specified
   * {@code move}. Under most conditions, this method need not be overridden; if it is, it should be
   * invoked by the overriding method.
   *
   * @param move Number added to (or subtracted from) the total.
   */
  protected void applyMove(int move) {
    game.play(move);
  }

  /**
   * Presents the most recently completed move, incorporating content provided by the encapsulated
   * {@link GamePresentation GamePresentation&lt;T&gt;}. A concrete subclass must implement this
   * method appropriately for the user interaction mode employed by that subclass, with view objects
   * of subtypes of that specified in the {@link GamePresentation GamePresentation&lt;T&gt;} type
   * parameter.
   *
   * @param state {@link State} resulting from the completed move.
   * @param move Most recent addition (or subtraction).
   */
  protected abstract void presentCompletedMove(State state, int move);

  /**
   * Presents an error related to the most recently <i>attempted</i> move, incorporating content
   * provided by the encapsulated {@link GamePresentation GamePresentation&lt;T&gt;}. A concrete
   * subclass must implement this method appropriately for the user interaction mode employed by
   * that subclass, with view objects of subtypes of that specified in the
   * {@link GamePresentation GamePresentation&lt;T&gt;} type parameter.
   *
   * @param presentation View object returned from
   * {@link GamePresentation#illegalMoveNotification(Game)}.
   */
  protected abstract void presentError(Object presentation);

  /**
   * Returns the {@link GamePresentation GamePresentation&lt;T&gt;} instance for use by subclasses.
   * This method should generally not be overridden.
   *
   * @return
   */
  protected GamePresentation<?> getPresentation() {
    return presentation;
  }

  /**
   * Implements (partially) the Builder pattern for constructing instances of {@link Referee}.
   * A concrete subclass of {@link Referee} should also contain (as {@code static} nested classes) a
   * definition of a concrete subclass of {@link Builder}, with the type parameter specifying the
   * same {@code Builder} subclass, to facilitate the use of the "simulated self-type" idiom.
   *
   * @param <B> Self-type parameter, specifying (when subclassing) the actual subclass type.
   */
  protected abstract static class Builder<B extends Builder<B>> {

    private static final String NULL_PRESENTATION_MESSAGE =
        "presentation must be a non-null reference to an instance of GamePresentation.";

    private final GamePresentation<?> presentation;
    private Operation operation = Game.DEFAULT_OPERATION;
    private int target = Game.DEFAULT_UPPER_BOUND;
    private int maxMove = Game.DEFAULT_MAX_MOVE;
    private State initialState = State.PLAYER_ONE_MOVE;

    /**
     * Initializes this instance with the specified {@link GamePresentation}, which will be provided
     * to the {@link Referee} instance created by this builder.
     *
     * @param presentation Non-{@code null} reference to a
     * {@link GamePresentation GamePresentation&lt;T&gt;} instance.
     */
    protected Builder(GamePresentation<?> presentation) {
      this.presentation = Objects.requireNonNull(presentation, NULL_PRESENTATION_MESSAGE);
    }

    /**
     * Specifies the {@link Operation} to be used for the {@link Game} managed by {@link Referee}
     * created by this builder. If this method is not invoked, the default is
     * {@link Operation#ADDITION}.
     *
     * @param operation
     * @return This {@link Builder} instance.
     */
    public B setOperation(Operation operation) {
      this.operation = operation;
      return self();
    }

    /**
     * Specifies the initial game state. {@code initialState} must be either
     * {@link State#PLAYER_ONE_MOVE} (the default) or {@link State#PLAYER_TWO_MOVE}.
     *
     * @param initialState
     * @return This {@link Builder} instance.
     */
    public B setInitialState(State initialState) {
      this.initialState = Objects.requireNonNull(initialState);
      return self();
    }

    /**
     * @param target
     * @return This {@link Builder} instance.
     */
    public B setTarget(int target) {
      this.target = target;
      return self();
    }

    /**
     * @param maxMove
     * @return This {@link Builder} instance.
     */
    public B setMaxMove(int maxMove) {
      this.maxMove = maxMove;
      return self();
    }

    /**
     * @return Fully initialized {@link Referee} instance.
     */
    protected abstract Referee build();

    /**
     * @return
     */
    protected abstract B self();

  }

}
