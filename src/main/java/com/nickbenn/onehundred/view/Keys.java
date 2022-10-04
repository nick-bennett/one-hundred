/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.view;

/**
 * Constants used as property keys for user-facing strings, read from the {@code addition} or
 * {@code subtraction} resource bundle, according to the {@link
 * com.nickbenn.onehundred.model.Game.Operation} configuration of the game.
 */
public abstract class Keys {

  /** Key for format {@code String} used to present game status before each move. */
  public static final String GAME_SUMMARY = "game_summary";
  /** Key for format {@code String} used to indicate player moving next. */
  public static final String NEXT_MOVE = "next_move";
  /** Key for format {@code String} used to display winning player. */
  public static final String WIN_STATE = "win_state";
  /** Key for format {@code String} used to display current player. */
  public static final String PLAY_STATE = "play_state";
  /** Key for format {@code String} used to display most recently completed move. */
  public static final String MOVE_REPORT = "move_report";
  /** Key for format {@code String} used to display move prompt. */
  public static final String MOVE_PROMPT = "move_prompt";
  /** Key for format {@code String} used to indicate that the user attempted on illegal move. */
  public static final String ILLEGAL_MOVE = "illegal_move";

}
