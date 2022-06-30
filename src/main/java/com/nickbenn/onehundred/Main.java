package com.nickbenn.onehundred;

import com.nickbenn.onehundred.controller.ConsoleReferee;
import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.strategy.Strategy;
import com.nickbenn.onehundred.view.GamePresentation;
import com.nickbenn.onehundred.view.Keys;
import com.nickbenn.onehundred.view.TextGamePresentation;

import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Implements entry point for console-mode implementation of <strong>"One Hundred"</strong> game.
 * <p>&lt; = less-than sign. &gt; = greater-than sign. &amp; = ampersand.</p>
 */
public class Main {

    private static final String BUNDLE_NAME = "strings";
    private static final String DEFAULT_STRATEGY = "optimal";
    private static final int DEFAULT_TARGET = 100;
    private static final int DEFAULT_MAX_MOVE = 6;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        int target = (args.length > 0) ? Integer.parseInt(args[0]) : DEFAULT_TARGET;
        int maxMove = (args.length > 1) ? Integer.parseInt(args[1]) : DEFAULT_MAX_MOVE;
        String strategyKey = (args.length > 2) ? args[2] : DEFAULT_STRATEGY;
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        try (Scanner scanner = new Scanner(System.in)) {
            Game.State state = Game.State.PLAYER_ONE_MOVE;
            String playAgainPrompt = bundle.getString(Keys.PLAY_AGAIN);
            String negativeResponse = bundle.getString(Keys.NEGATIVE_RESPONSE);
            Strategy strategy = Strategy.newInstance(strategyKey);
            GamePresentation<String> presentation = new TextGamePresentation(bundle);
            do {
                ConsoleReferee referee = new ConsoleReferee(
                        strategy, presentation, scanner, bundle, target, maxMove, state);
                referee.play();
                state = (state == Game.State.PLAYER_ONE_MOVE)
                        ? Game.State.PLAYER_TWO_MOVE
                        : Game.State.PLAYER_ONE_MOVE;
            } while (keepPlaying(scanner, playAgainPrompt, negativeResponse));
        } catch (Strategy.StrategyInitializationException e) {
            System.out.printf(bundle.getString(Keys.STRATEGY_INITIALIZATION_ERROR), strategyKey, e);
            e.printStackTrace();
        }
    }

    private static boolean keepPlaying(Scanner scanner, String prompt, String negativeResponse) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        System.out.print(prompt);
        String response = scanner.nextLine().trim().toLowerCase();
        return (!response.startsWith(negativeResponse));
    }
}
