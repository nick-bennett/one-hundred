package com.nickbenn.onehundred.view;

import com.nickbenn.onehundred.model.Game;

import java.util.ResourceBundle;

/**
 *
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
   *
   * @param bundle
   */
  public TextGamePresentation(ResourceBundle bundle) {
    winStatePattern = bundle.getString(Keys.WIN_STATE);
    playStatePattern = bundle.getString(Keys.PLAY_STATE);
    summaryPattern = bundle.getString(Keys.GAME_SUMMARY);
    nextMovePattern = bundle.getString(Keys.NEXT_MOVE);
    moveReportPattern = bundle.getString(Keys.MOVE_REPORT);
    movePromptPattern = bundle.getString(Keys.MOVE_PROMPT);
    illegalMovePattern = bundle.getString(Keys.ILLEGAL_MOVE);
  }

  /**
   *
   * @param game
   * @param playerOne
   * @param playerTwo
   * @return
   */
  @Override
  public String stateRepresentation(Game game, String playerOne, String playerTwo) {
    String player = game.getState().isTerminal()
        ? ((game.getState() == Game.State.PLAYER_ONE_WIN) ? playerOne : playerTwo)
        : ((game.getState() == Game.State.PLAYER_ONE_MOVE) ? playerOne : playerTwo);
    String next = game.getState().isTerminal()
        ? String.format(winStatePattern, player)
        : String.format(playStatePattern, player);
    return String.format(summaryPattern, game.getUpperBound(), game.getCurrentCount(),
        game.getUpperBound() - game.getCurrentCount(), next);
  }

  /**
   *
   * @param player
   * @return
   */
  @Override
  public String nextMoveNotice(String player) {
    return String.format(nextMovePattern, player);
  }

  /**
   *
   * @param move
   * @param player
   * @return
   */
  @Override
  public String movePresentation(int move, String player) {
    return String.format(moveReportPattern, move, player);
  }

  /**
   *
   * @param game
   * @return
   */
  @Override
  public String movePrompt(Game game) {
    return String.format(movePromptPattern,
        Math.min(game.getMaxMove(), game.getUpperBound() - game.getCurrentCount()));
  }

  /**
   *
   * @param game
   * @return
   */
  @Override
  public String illegalMoveNotification(Game game) {
    return String.format(illegalMovePattern, game.getUpperBound(), game.getMaxMove());
  }

}
