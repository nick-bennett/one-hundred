/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.view;

import com.nickbenn.onehundred.model.Game;

import com.nickbenn.onehundred.model.Game.Operation;
import java.util.ResourceBundle;

/**
 * Implements the {@link GamePresentation} interface for the {@link String} type.
 * <ul>
 *   <li>First item</li>
 *   <li>Second item</li>
 *   <li>...</li>
 * </ul>
 */
public class TextGamePresentation implements GamePresentation<String> {

  private final String winStatePattern;
  private final String playStatePattern;
  private final String summaryPattern;
  private final String nextMovePattern;
  private final String moveReportPattern;
  private final String movePromptPattern;
  private final String illegalMovePattern;

  /**
   * TODO Finish comment.
   * @param operation
   */
  public TextGamePresentation(Operation operation) {
    ResourceBundle bundle = ResourceBundle.getBundle(operation.name().toLowerCase());
    winStatePattern = bundle.getString(Keys.WIN_STATE);
    playStatePattern = bundle.getString(Keys.PLAY_STATE);
    summaryPattern = bundle.getString(Keys.GAME_SUMMARY);
    nextMovePattern = bundle.getString(Keys.NEXT_MOVE);
    moveReportPattern = bundle.getString(Keys.MOVE_REPORT);
    movePromptPattern = bundle.getString(Keys.MOVE_PROMPT);
    illegalMovePattern = bundle.getString(Keys.ILLEGAL_MOVE);
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
