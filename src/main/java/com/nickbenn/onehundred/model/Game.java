package com.nickbenn.onehundred.model;

import com.nickbenn.onehundred.model.exception.GameFinishedException;
import com.nickbenn.onehundred.model.exception.IllegalConfigurationException;
import com.nickbenn.onehundred.model.exception.IllegalMoveException;

/**
 *
 */
@SuppressWarnings("unused")
public class Game {

  /**  */
  public static final int DEFAULT_UPPER_BOUND = 100;
  /**  */
  public static final int DEFAULT_MAX_MOVE = 10;
  /**  */
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
   * @param upperBound
   * @param maxMove
   * @param initialState
   * @throws IllegalConfigurationException
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
   * @param move
   */
  public void play(int move) {
    state = state.play(upperBound, maxMove, currentCount, move, operation);
    currentCount += move * operation.getSign();
    firstMove = false;
  }

  /**
   *
   * @return
   */
  public Operation getOperation() {
    return operation;
  }

  /**
   * @return
   */
  public int getUpperBound() {
    return upperBound;
  }

  /**
   * @return
   */
  public int getMaxMove() {
    return maxMove;
  }

  /**
   * @return
   */
  public int getCurrentCount() {
    return currentCount;
  }

  /**
   * @return
   */
  public State getState() {
    return state;
  }

  /**
   * @return
   */
  public boolean isFirstMove() {
    return firstMove;
  }

  public int getTarget() {
    return target;
  }

  public int getRemaining() {
    return (operation == Operation.ADDITION)
        ? target - currentCount
        : currentCount;
  }

  /**
   *
   */
  public enum State {

    /**
     *
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
      public State nextMoveState() {
        return PLAYER_TWO_MOVE;
      }

      @Override
      public State nextWinState() {
        return PLAYER_ONE_WIN;
      }

    },

    /**
     *
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
      public State nextMoveState() {
        return PLAYER_ONE_MOVE;
      }

      @Override
      public State nextWinState() {
        return PLAYER_TWO_WIN;
      }

    },
    /**
     *
     */
    PLAYER_ONE_WIN,
    /**
     *
     */
    PLAYER_TWO_WIN;

    private static final String NO_MOVES_ALLOWED_FORMAT =
        "Game is already in a terminal state (sum = %d); no further moves allowed.";
    private static final String TARGET_OVERSHOOT_FORMAT =
        "A move of (%3$d) with a current count of (%2$d) would result in a count of %4$d, outside the allowed range (0-%1$d).";
    private static final String MOVE_TOO_LARGE_FORMAT =
        "Attempted move (%2$d) exceeds the maximum allowed (%1$d).";

    /**
     * @return
     */
    public boolean isInitial() {
      return false;
    }

    /**
     * @return
     */
    public boolean isTerminal() {
      return true;
    }

    /**
     * @return
     */
    public State nextMoveState() {
      return null;
    }

    /**
     * @return
     */
    public State nextWinState() {
      return this;
    }

    /**
     * @param upperBound
     * @param maxMove
     * @param count
     * @param move
     * @return
     * @throws GameFinishedException
     * @throws IllegalMoveException
     */
    public State play(int upperBound, int maxMove, int count, int move, Operation operation)
        throws GameFinishedException, IllegalMoveException {
      State nextState;
      if (isTerminal()) {
        throw new GameFinishedException(String.format(NO_MOVES_ALLOWED_FORMAT, count));
      }
      if (move <= 0 || move > maxMove) {
        throw new IllegalMoveException(
            String.format(MOVE_TOO_LARGE_FORMAT, maxMove, move));
      }
      int sign = operation.getSign();
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

  public enum Operation {

    /**  */
    ADDITION(1),
    /**  */
    SUBTRACTION(-1);

    private final int sign;

    Operation(int sign) {
      this.sign = sign;
    }

    public int getSign() {
      return sign;
    }

  }

}
