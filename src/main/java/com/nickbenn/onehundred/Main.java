package com.nickbenn.onehundred;

import com.nickbenn.onehundred.controller.ConsoleSolitaireReferee;
import com.nickbenn.onehundred.controller.Referee;
import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.strategy.Strategy;
import com.nickbenn.onehundred.view.GamePresentation;
import com.nickbenn.onehundred.view.Keys;
import com.nickbenn.onehundred.view.TextGamePresentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ResourceBundle;

/**
 * Implements entry point for console-mode implementation of <strong>"One Hundred"</strong> game.
 */
public class Main {

    private static final String BUNDLE_NAME = "strings";

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        try (
                Reader input =  new InputStreamReader(System.in);
                BufferedReader reader = new BufferedReader(input)
        ) {
            Game.State state = Game.State.PLAYER_ONE_MOVE;
            String playAgainPrompt = bundle.getString(Keys.PLAY_AGAIN);
            String negativeResponse = bundle.getString(Keys.NEGATIVE_RESPONSE);
            GamePresentation<String> presentation = new TextGamePresentation(bundle);
            do {
                playOnce(args, bundle, state, presentation);
                state = (state == Game.State.PLAYER_ONE_MOVE)
                        ? Game.State.PLAYER_TWO_MOVE
                        : Game.State.PLAYER_ONE_MOVE;
            } while (keepPlaying(reader, playAgainPrompt, negativeResponse));
        } catch (Strategy.StrategyInitializationException e) {
            System.out.printf(bundle.getString(Keys.STRATEGY_INITIALIZATION_ERROR), e);
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void playOnce(String[] args, ResourceBundle bundle, Game.State state, GamePresentation<String> presentation) throws Strategy.StrategyInitializationException {
        ConsoleSolitaireReferee.Builder builder =
                new ConsoleSolitaireReferee.Builder(presentation, bundle).setInitialState(state);
        if (args.length > 0) {
            builder.setTarget(Integer.parseInt(args[0]));
            if (args.length > 1) {
                builder.setMaxMove(Integer.parseInt(args[1]));
                if (args.length > 2) {
                    builder.setStrategy(Strategy.newInstance(args[2]));
                }
            }
        }
        Referee referee = builder.build();
        referee.play();
    }

    private static boolean keepPlaying(BufferedReader reader, String prompt, String negativeResponse)
            throws IOException {
        System.out.print(prompt);
        String response = reader.readLine().trim().toLowerCase();
        return (!response.startsWith(negativeResponse));
    }

}
