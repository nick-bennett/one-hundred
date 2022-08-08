package com.nickbenn.onehundred.view;

import com.nickbenn.onehundred.model.Game;

/**
 * @param <T>
 */
public interface GamePresentation<T> {

  /**
   * @param game
   * @param playerOne
   * @param playerTwo
   * @return
   */
  T stateRepresentation(Game game, String playerOne, String playerTwo);

  /**
   *
   * @param player
   * @return
   */
  T nextMoveNotice(String player);

  /**
   * @param move
   * @param player
   * @return
   */
  T movePresentation(int move, String player);

  /**
   * @param game
   * @return
   */
  T movePrompt(Game game);

  /**
   * @param game
   * @return
   */
  T illegalMoveNotification(Game game);

}
