package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.controller.strategy.StrategyInitializationException;
import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.model.Game.State;
import com.nickbenn.onehundred.model.exception.IllegalConfigurationException;
import com.nickbenn.onehundred.view.GamePresentation;
import com.nickbenn.onehundred.view.TextGamePresentation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;
import org.apache.commons.cli.ParseException;

public class ConsoleSession extends Session {

  private static final String BUNDLE_NAME = "session";

  private final CommandLineOptions options;
  private final BufferedReader reader;

  public ConsoleSession(String[] args) {
    super(args);
    options = new CommandLineOptions();
    reader = new BufferedReader(new InputStreamReader(System.in));
  }

  @Override
  public void run() {
    ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
    try {
      options.parse(getArgs());
      if (options.isHelpRequested()) {
        options.printHelp();
      } else {
        play(bundle);
      }
    } catch (StrategyInitializationException | ParseException | IllegalConfigurationException e) {
      System.out.println(e.getMessage());
      options.printHelp();
    } catch (IllegalArgumentException e) {
      //noinspection ThrowablePrintedToSystemOut
      System.out.println(e);
      options.printHelp();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void play(ResourceBundle bundle)
      throws StrategyInitializationException, IOException {
    Game.State state = Game.State.PLAYER_ONE_MOVE;
    String playAgainPrompt = bundle.getString(Keys.PLAY_AGAIN);
    String negativeResponse = bundle.getString(Keys.NEGATIVE_RESPONSE);
    GamePresentation<String> presentation = new TextGamePresentation(options.getOperation());
    do {
      playOnce(bundle, state, presentation);
      state = (state == State.PLAYER_ONE_MOVE)
          ? State.PLAYER_TWO_MOVE
          : State.PLAYER_ONE_MOVE;
    } while (keepPlaying(playAgainPrompt, negativeResponse));
  }

  private void playOnce(
      ResourceBundle bundle, Game.State state, GamePresentation<String> presentation)
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

  private boolean keepPlaying(String prompt, String negativeResponse)
      throws IOException {
    System.out.print(prompt);
    String response = reader.readLine().trim().toLowerCase();
    return (!response.startsWith(negativeResponse));
  }

}
