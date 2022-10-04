/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.view;

import com.nickbenn.onehundred.model.Game;

/**
 * Declares methods that will construct and return presentations of game status, move prompts, error
 * messages, etc. Implementations of this interface are essentially display "strategies", using an
 * instance of {@link Game} as the context, and constructing view objects suitable for presentation
 * in the UI environment for which the strategy is implemented.
 *
 * @param <T> Type of view object used to present game status and prompts. This will typically be
 *           the base type of view objects in the UI framework for which a specific implementation
 *           is intended&mdash;e.g. {@link String} for a console-mode implementation,
 *           {@code javafx.scene.Node} for a JavaFX implementation, {@link javax.swing.JComponent}
 *           for Swing, etc.
 */
public interface GamePresentation<T> {

  /**
   * Constructs and returns a view object for presenting the current state of {@code game}.
   *
   * @param game Context instance of {@link Game}.
   * @param playerOne Identifier for the first player (the user).
   * @param playerTwo Identifier for the second player (the computer).
   * @return View of current {@link Game} state.
   */
  T stateRepresentation(Game game, String playerOne, String playerTwo);

  /**
   * Constructs and returns a view object suitable for presenting the next player to move.
   *
   * @param player Player to move next.
   * @return View presenting next player.
   */
  T nextMoveNotice(String player);

  /**
   * Constructs and returns a view object suitable for presenting the most recent move.
   *
   * @param move Most recent move (number added or subtracted).
   * @param player Player that made the most recent move.
   * @return View presenting most recently completed move.
   */
  T movePresentation(int move, String player);

  /**
   * Constructs and returns a view object suitable for presenting a move prompt to the user.
   *
   * @param game Context instance of {@link Game}.
   * @return View presenting prompt for next move.
   */
  T movePrompt(Game game);

  /**
   * Constructs and returns a view object suitable for notifying the user that the most recently
   * attempted move was illegal/invalid.
   *
   * @param game Context instance of {@link Game}.
   * @return View presenting illegal move notification.
   */
  T illegalMoveNotification(Game game);

}
