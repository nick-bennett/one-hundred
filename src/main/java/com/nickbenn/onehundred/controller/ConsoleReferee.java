package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.model.exception.IllegalMoveException;
import com.nickbenn.onehundred.strategy.Strategy;
import com.nickbenn.onehundred.view.GamePresentation;
import com.nickbenn.onehundred.view.Keys;

import java.util.InputMismatchException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Manages user interaction&mdash;with corresponding updates to the game state&mdash;through completion of a single
 * game. As the game progresses, an instance of {@link GamePresentation} is used to display the current state, and
 * prompt the user for the next move; all of this is orchestrated in a simple invocation of the {@link #play()}. This
 * class also uses an instance of {@link Strategy} for the computer's moves; typically, this will be an instance of
 * {@link com.nickbenn.onehundred.strategy.OptimalStrategy}.
 */
public class ConsoleReferee {

    private static final Pattern ALL_CHARACTERS = Pattern.compile(".*");
    private final Strategy strategy;
    private final GamePresentation<?> presentation;
    private final Scanner scanner;
    private final Game game;
    private final String playerName;
    private final String computerName;
    private final String firstMovePattern;

    /**
     * This is a summary.
     *
     * @param strategy
     * @param presentation
     * @param scanner
     * @param bundle
     * @param target
     * @param maxMove
     * @param initialState
     */
    public ConsoleReferee(Strategy strategy, GamePresentation<?> presentation, Scanner scanner, ResourceBundle bundle,
                          int target, int maxMove, Game.State initialState) {
        this.strategy = strategy;
        this.presentation = presentation;
        this.scanner = scanner;
        game = new Game(target, maxMove, initialState);
        playerName = bundle.getString(Keys.PLAYER_NAME);
        computerName = bundle.getString(Keys.COMPUTER_NAME);
        firstMovePattern = bundle.getString(Keys.FIRST_MOVE);
    }

    /**
     *
     */
    public void play() {
        Game.State state;
        System.out.printf(firstMovePattern,
                (game.getState() == Game.State.PLAYER_ONE_MOVE) ? playerName : computerName);
        while (!(state = game.getState()).isTerminal()) {
            if (state == Game.State.PLAYER_ONE_MOVE) {
                getAndApplyUserMove();
            } else {
                getAndApplyComputerMove();
            }
        }
        System.out.print(presentation.stateRepresentation(game, playerName, computerName));
    }

    private void getAndApplyUserMove() {
        System.out.print(presentation.stateRepresentation(game, playerName, computerName));
        boolean validMove = false;
        do {
            System.out.print(presentation.movePrompt(game));
            int move = 0;
            try {
                move = scanner.nextInt();
                game.play(move);
                System.out.print(presentation.movePresentation(move, playerName));
                validMove = true;
            } catch (IllegalMoveException | InputMismatchException e) {
                scanner.skip(ALL_CHARACTERS);
                System.out.print(presentation.illegalMoveNotification(game));
            }
        } while (!validMove);
    }

    private void getAndApplyComputerMove() {
        int move = strategy.getNextMove(game);
        game.play(move);
        System.out.print(presentation.movePresentation(move, computerName));
    }

}
