package com.nickbenn.onehundred.model;

import com.nickbenn.onehundred.model.exception.GameFinishedException;
import com.nickbenn.onehundred.model.exception.IllegalConfigurationException;
import com.nickbenn.onehundred.model.exception.IllegalMoveException;

/**
 *
 */
@SuppressWarnings("unused")
public class Game {

    public static final int DEFAULT_TARGET = 100;
    public static final int DEFAULT_MAX_MOVE = 10;

    private static final String INVALID_TARGET_MOVE_FORMAT =
            "Game target (%1$d) and max move (%2$d) must both be positive, with target > max move.";
    private static final String INVALID_INITIAL_STATE_FORMAT =
            "%1$s is not a valid initial state.";
    private final int target;
    private final int maxMove;

    private boolean firstMove;
    private int sum;
    private State state;

    /**
     *
     * @param target
     * @param maxMove
     * @param initialState
     * @throws IllegalConfigurationException
     */
    public Game(int target, int maxMove, State initialState) throws IllegalConfigurationException {
        if (maxMove <= 0 || maxMove >= target) {
            throw new IllegalConfigurationException(String.format(INVALID_TARGET_MOVE_FORMAT, target, maxMove));
        }
        if (!initialState.isInitial()) {
            throw new IllegalConfigurationException(String.format(INVALID_INITIAL_STATE_FORMAT, initialState));
        }
        this.target = target;
        this.maxMove = maxMove;
        state = initialState;
        firstMove = true;
    }

    public void play(int move) {
        state = state.play(target, maxMove, sum, move);
        sum += move;
        firstMove = false;
    }

    public int getTarget() {
        return target;
    }

    public int getMaxMove() {
        return maxMove;
    }

    public int getSum() {
        return sum;
    }

    public State getState() {
        return state;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public enum State {

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
        PLAYER_ONE_WIN,
        PLAYER_TWO_WIN;

        private static final String NO_MOVES_ALLOWED_FORMAT =
                "Game is already in a terminal state (sum = %d); no further moves allowed.";
        private static final String TARGET_OVERSHOOT_FORMAT =
                "Current sum (%2$d) plus move (%3$d) would result in a sum of %4$d, exceeding target (%1$d).";
        private static final String MOVE_TOO_LARGE_FORMAT =
                "Attempted move (%2$d) exceeds the maximum allowed (%1$d).";

        public boolean isInitial() {
            return false;
        }

        public boolean isTerminal() {
            return true;
        }

        public State nextMoveState() {
            return null;
        }

        public State nextWinState() {
            return this;
        }

        public State play(int target, int maxMove, int sum, int move)
                throws GameFinishedException, IllegalMoveException {
            State nextState;
            if (isTerminal()) {
                throw new GameFinishedException(String.format(NO_MOVES_ALLOWED_FORMAT, sum));
            }
            if (move <= 0 || move > maxMove) {
                throw new IllegalMoveException(
                        String.format(MOVE_TOO_LARGE_FORMAT, maxMove, move));
            }
            if (sum + move > target) {
                throw new IllegalMoveException(
                        String.format(TARGET_OVERSHOOT_FORMAT, target, sum, move, sum + move));
            }
            if (sum + move == target) {
                nextState = nextWinState();
            } else {
                nextState = nextMoveState();
            }
            return nextState;
        }

    }

}
