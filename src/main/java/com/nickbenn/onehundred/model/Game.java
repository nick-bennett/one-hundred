/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.model;

import com.nickbenn.onehundred.model.exception.GameFinishedException;
import com.nickbenn.onehundred.model.exception.IllegalConfigurationException;
import com.nickbenn.onehundred.model.exception.IllegalMoveException;

/**
 * Encapsulates the configuration and current state of a single game of One Hundred.
 */
@SuppressWarnings({"unused"})
public class Game {

  /**
   * Default target value (for addition game) or starting value (for subtraction game).
   */
  public static final int DEFAULT_UPPER_BOUND = 100;
  /**
   * Default maximum amount allowed to be added or subtracted in each move.
   */
  public static final int DEFAULT_MAX_MOVE = 10;
  /**
   * Default game operation (direction).
   */
  public static final Operation DEFAULT_OPERATION = Operation.ADDITION;

  private static final String INVALID_UPPER_BOUND_MOVE_FORMAT =
      "Game upper bound (%1$d) and max move (%2$d) must both be positive, with upper bound > max move.";
  private static final String INVALID_INITIAL_STATE_FORMAT =
      "%1$s is not a valid initial state.";

  private final Operation operation;
  private final int upperBound;
  private final int maxMove;
  private final int target;

  private boolean firstMove;
  private int currentCount;
  private State state;

  /**
   * Initializes this instance with the specified configuration parameters. Once initialized, the
   * configuration of the game does not change.
   *
   * @param operation    Direction of game (addition or subtraction), specified as one of the
   *                     enumerated values of {@link Operation}.
   * @param upperBound   Target value (for addition game) or starting value (for subtraction game).
   * @param maxMove      Maximum quantity allowed to be added or subtracted in each move.
   * @param initialState Player to make the first move, specified as {@link State#PLAYER_ONE_MOVE}
   *                     or {@link State#PLAYER_TWO_MOVE}.
   * @throws IllegalConfigurationException If {@code upperBound} or {@code maxMove} is non-positive,
   *                                       {@code (maxMove >= upperBound)}, or if
   *                                       {@code initialState} is neither
   *                                       {@link State#PLAYER_ONE_MOVE} nor
   *                                       {@link State#PLAYER_TWO_MOVE}.
   */
  public Game(Operation operation, int upperBound, int maxMove, State initialState)
      throws IllegalConfigurationException {
    if (maxMove <= 0 || maxMove >= upperBound) {
      throw new IllegalConfigurationException(
          String.format(INVALID_UPPER_BOUND_MOVE_FORMAT, upperBound, maxMove));
    }
    if (!initialState.isInitial()) {
      throw new IllegalConfigurationException(
          String.format(INVALID_INITIAL_STATE_FORMAT, initialState));
    }
    this.operation = operation;
    this.upperBound = upperBound;
    this.maxMove = maxMove;
    state = initialState;
    firstMove = true;
    if (operation == Operation.ADDITION) {
      currentCount = 0;
      target = upperBound;
    } else {
      currentCount = upperBound;
      target = 0;
    }
  }

  /**
   * Updates the state of this instance by applying the specified move.
   *
   * @param move Quantity to be added (for an addition game) or subtracted (for a subtraction
   *             game).
   * @throws GameFinishedException If the game is already completed, and thus permits no further
   *                               moves.
   * @throws IllegalMoveException  If {@code move} is non-positive, exceeds the maximum move
   *                               allowed, or would result in a total exceeding the target (for an
   *                               addition game) or less than zero (for a subtraction game).
   */
  public void play(int move) throws GameFinishedException, IllegalMoveException {
    state = state.play(upperBound, maxMove, currentCount, move, operation);
    currentCount += move * operation.sign();
    firstMove = false;
  }

  /**
   * Returns {@link Operation#ADDITION} or {@link Operation#SUBTRACTION}, as set in
   * {@link Game#Game(Operation, int, int, State)}.
   *
   * @return (See above.)
   */
  public Operation getOperation() {
    return operation;
  }

  /**
   * Returns the upper bound&mdash;that is, the target value (for addition) or initial value (for
   * subtraction), as set in {@link Game#Game(Operation, int, int, State)}.
   *
   * @return (See above.)
   */
  public int getUpperBound() {
    return upperBound;
  }

  /**
   * Returns the maximum quantity that may be added or subtracted, as set in
   * {@link Game#Game(Operation, int, int, State)}.
   *
   * @return (See above.)
   */
  public int getMaxMove() {
    return maxMove;
  }

  /**
   * Returns the target value (for an addition game) or starting value (for a subtraction game), as
   * set in {@link Game#Game(Operation, int, int, State)}.
   *
   * @return (See above.)
   */
  public int getTarget() {
    return target;
  }

  /**
   * Returns the current total&mdash;that is, the initial value, with the sum of all moves made in
   * the game so far added or subtracted, according to {@link #getOperation()}.
   *
   * @return (See above.)
   */
  public int getCurrentCount() {
    return currentCount;
  }

  /**
   * Returns the current {@link State}, indicating either the next player to move (for an
   * in-progress game) or the winner player (for a completed game).
   *
   * @return (See above.)
   */
  public State getState() {
    return state;
  }

  /**
   * Returns a flag indicating whether the next move made will be the first so far in the game.
   * Implementations of {@link com.nickbenn.onehundred.view.GamePresentation} or subclasses of
   * {@link com.nickbenn.onehundred.model.strategy.Strategy} may make use of this information,
   * but they are not required to do so.
   *
   * @return (See above.)
   */
  public boolean isFirstMove() {
    return firstMove;
  }

  /**
   * Returns the non-negative difference between the current and target values. For an addition
   * game, this will be equal to
   * {@code (}{@link #getTarget() getTarget() - }{@link #getCurrentCount()}{@code )}; for a
   * subtraction game, it is simply {@link #getCurrentCount()}.
   *
   * @return (See above.)
   */
  public int getRemaining() {
    return (operation == Operation.ADDITION)
        ? target - currentCount
        : currentCount;
  }

  /**
   * Encapsulates the possible states of a single {@link Game}, as well as validation of all
   * state-changing operations.
   */
  public enum State {

    /**
     * Indicates that player 1 is the next to move.
     */
    PLAYER_ONE_MOVE {
      @Override
      public boolean isInitial() {
        return true;
      }

      @Override
      public boolean isTerminal() {
        return false;
      }

      @Override
      State nextMoveState() {
        return PLAYER_TWO_MOVE;
      }

      @Override
      State nextWinState() {
        return PLAYER_ONE_WIN;
      }
    },
    /**
     * Indicates that player 2 is the next to move.
     */
    PLAYER_TWO_MOVE {
      @Override
      public boolean isInitial() {
        return true;
      }

      @Override
      public boolean isTerminal() {
        return false;
      }

      @Override
      State nextMoveState() {
        return PLAYER_ONE_MOVE;
      }

      @Override
      State nextWinState() {
        return PLAYER_TWO_WIN;
      }
    },
    /**
     * Indicates that player 1 has won the game.
     */
    PLAYER_ONE_WIN,
    /**
     * Indicates that player 2 has won the game.
     */
    PLAYER_TWO_WIN;

    private static final String NO_MOVES_ALLOWED_FORMAT =
        "Game is already in a terminal state (sum = %d); no further moves allowed.";
    private static final String TARGET_OVERSHOOT_FORMAT =
        "A move of (%3$d) with a current count of (%2$d) would result in a count of %4$d, outside the allowed range (0-%1$d).";
    private static final String MOVE_TOO_LARGE_FORMAT =
        "Attempted move (%2$d) exceeds the maximum allowed (%1$d).";

    /**
     * Returns a flag indicating whether this state is one of the allowed initial states.
     *
     * @return (See above.)
     */
    public boolean isInitial() {
      return false;
    }

    /**
     * Returns a flag indicating whether this state is one of the terminal states, from which no
     * further moves are possible.
     *
     * @return (See above.)
     */
    public boolean isTerminal() {
      return true;
    }

    State nextMoveState() {
      return null;
    }

    State nextWinState() {
      return this;
    }

    private State play(int upperBound, int maxMove, int count, int move, Operation operation)
        throws GameFinishedException, IllegalMoveException {
      State nextState;
      if (isTerminal()) {
        throw new GameFinishedException(String.format(NO_MOVES_ALLOWED_FORMAT, count));
      }
      if (move <= 0 || move > maxMove) {
        throw new IllegalMoveException(
            String.format(MOVE_TOO_LARGE_FORMAT, maxMove, move));
      }
      int sign = operation.sign();
      int newCount = count + move * sign;
      if (newCount > upperBound || newCount < 0) {
        throw new IllegalMoveException(
            String.format(TARGET_OVERSHOOT_FORMAT, upperBound, count, move, newCount));
      }
      if (newCount == upperBound * ((1 + sign) >> 1)) {
        nextState = nextWinState();
      } else {
        nextState = nextMoveState();
      }
      return nextState;
    }

  }

  /**
   * Encapsulates the possible "directions" of play&mdash;that is, the arithmetic operation
   * performed in all moves of a game using a given {@code Operation} enumerated value.
   */
  public enum Operation {

    ADDITION(1),
    SUBTRACTION(-1);

    private final int sign;

    Operation(int sign) {
      this.sign = sign;
    }

    /**
     * Returns 1 for addition and -1 for subtraction.
     *
     * @return (See above.)
     */
    public int sign() {
      return sign;
    }

  }

}
