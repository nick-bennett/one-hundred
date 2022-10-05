/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.view;

import com.nickbenn.onehundred.model.Game;

import com.nickbenn.onehundred.model.Game.Operation;
import java.util.ResourceBundle;

/**
 * Implements the {@link GamePresentation GamePresentation&lt;T&gt;} for the {@link String} type.
 * This implementation is intended for use in a console-based game.
 */
public class TextGamePresentation implements GamePresentation<String> {

  private static final String GAME_SUMMARY = "game_summary";
  private static final String NEXT_MOVE = "next_move";
  private static final String WIN_STATE = "win_state";
  private static final String PLAY_STATE = "play_state";
  private static final String MOVE_REPORT = "move_report";
  private static final String MOVE_PROMPT = "move_prompt";
  private static final String ILLEGAL_MOVE = "illegal_move";

  private final String winStatePattern;
  private final String playStatePattern;
  private final String summaryPattern;
  private final String nextMovePattern;
  private final String moveReportPattern;
  private final String movePromptPattern;
  private final String illegalMovePattern;

  /**
   * Initializes this instance with {@link String} content read from a {@link ResourceBundle}
   * corresponding to {@code operation}. The base name of the bundle is the lowercase version of
   * the value returned from {@code operation.name()}; most of the values in the bundle are used as
   * format strings, passed to {@link String#format(String, Object...)} along with relevant
   * arguments from the current game state.
   *
   * @param operation "Direction" of the game (addition or subtraction), specified as one of the
   *                  enumerated values of {@link Operation}.
   */
  public TextGamePresentation(Operation operation) {
    ResourceBundle bundle = ResourceBundle.getBundle(operation.name().toLowerCase());
    winStatePattern = bundle.getString(WIN_STATE);
    playStatePattern = bundle.getString(PLAY_STATE);
    summaryPattern = bundle.getString(GAME_SUMMARY);
    nextMovePattern = bundle.getString(NEXT_MOVE);
    moveReportPattern = bundle.getString(MOVE_REPORT);
    movePromptPattern = bundle.getString(MOVE_PROMPT);
    illegalMovePattern = bundle.getString(ILLEGAL_MOVE);
  }

  @Override
  public String stateRepresentation(Game game, String playerOne, String playerTwo) {
    String player = game.getState().isTerminal()
        ? ((game.getState() == Game.State.PLAYER_ONE_WIN) ? playerOne : playerTwo)
        : ((game.getState() == Game.State.PLAYER_ONE_MOVE) ? playerOne : playerTwo);
    String next = game.getState().isTerminal()
        ? String.format(winStatePattern, player)
        : String.format(playStatePattern, player);
    return String.format(summaryPattern,
        game.getUpperBound(), game.getCurrentCount(), game.getRemaining(), next);
  }

  @Override
  public String nextMoveNotice(String player) {
    return String.format(nextMovePattern, player);
  }

  @Override
  public String movePresentation(int move, String player) {
    return String.format(moveReportPattern, move, player);
  }

  @Override
  public String movePrompt(Game game) {
    return String.format(movePromptPattern, Math.min(game.getMaxMove(), game.getRemaining()));
  }

  @Override
  public String illegalMoveNotification(Game game) {
    return String.format(illegalMovePattern, game.getUpperBound(), game.getMaxMove());
  }

}
