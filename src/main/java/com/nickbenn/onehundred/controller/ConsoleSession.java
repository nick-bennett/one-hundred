/*
 * Copyright 2022 Nicholas Bennett. Licensed for use under the MIT License.
 */
package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.model.strategy.Strategy.StrategyInitializationException;
import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.model.Game.State;
import com.nickbenn.onehundred.view.GamePresentation;
import com.nickbenn.onehundred.view.TextGamePresentation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ResourceBundle;
import org.apache.commons.cli.ParseException;

/**
 *
 */
public class ConsoleSession extends Session {

  private static final String BUNDLE_NAME = "session";

  private final CommandLineOptions options;
  private final BufferedReader input;
  private final PrintStream output;
  private final ResourceBundle bundle;

  /**
   * Initializes this instance with the provided {@code args} and connects to the standard input
   * device for subsequent user interaction.
   *
   * @param args Command-line arguments.
   */
  public ConsoleSession(String[] args) {
    super(args);
    options = new CommandLineOptions();
    input = new BufferedReader(new InputStreamReader(System.in));
    output = System.out;
    bundle = ResourceBundle.getBundle(BUNDLE_NAME);
  }

  @Override
  public void run() {
    try {
      options.parse(getArgs());
      if (options.isHelpRequested()) {
        options.showHelp();
      } else {
        play();
      }
    } catch (StrategyInitializationException | ParseException | IllegalArgumentException e) {
      output.println(e.getMessage());
      options.showHelp();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void play() throws StrategyInitializationException, IOException {
    Game.State state = Game.State.PLAYER_ONE_MOVE;
    String playAgainPrompt = bundle.getString(Keys.PLAY_AGAIN);
    String negativeResponse = bundle.getString(Keys.NEGATIVE_RESPONSE);
    GamePresentation<String> presentation = new TextGamePresentation(options.getOperation());
    do {
      playOnce(state, presentation);
      state = (state == State.PLAYER_ONE_MOVE)
          ? State.PLAYER_TWO_MOVE
          : State.PLAYER_ONE_MOVE;
    } while (keepPlaying(playAgainPrompt, negativeResponse));
  }

  private void playOnce(Game.State state, GamePresentation<String> presentation)
      throws StrategyInitializationException {
    new ConsoleSolitaireReferee.Builder(presentation, bundle)
        .setInitialState(state)
        .setTarget(options.getBound())
        .setMaxMove(options.getMaxMove())
        .setOperation(options.getOperation())
        .setStrategy(options.getStrategy())
        .build()
        .play();
  }

  private boolean keepPlaying(String prompt, String negativeResponse) throws IOException {
    output.print(prompt);
    String response = input.readLine().trim().toLowerCase();
    return (!response.startsWith(negativeResponse));
  }

}
