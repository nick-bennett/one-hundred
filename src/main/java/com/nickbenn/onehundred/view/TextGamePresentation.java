package com.nickbenn.onehundred.view;

import com.nickbenn.onehundred.model.Game;

import java.util.ResourceBundle;

public class TextGamePresentation implements GamePresentation<String> {

    private final String winStatePattern;
    private final String playStatePattern;
    private final String summaryPattern;
    private final String moveReportPattern;
    private final String movePromptPattern;
    private final String illegalMovePattern;

    public TextGamePresentation(ResourceBundle bundle) {
        winStatePattern = bundle.getString(Keys.WIN_STATE);
        playStatePattern = bundle.getString(Keys.PLAY_STATE);
        summaryPattern = bundle.getString(Keys.GAME_SUMMARY);
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
        return String.format(summaryPattern, game.getTarget(), game.getSum(), game.getTarget() - game.getSum(), next);
    }

    @Override
    public String movePresentation(int move, String player) {
        return String.format(moveReportPattern, move, player);
    }

    @Override
    public String movePrompt(Game game) {
        return String.format(movePromptPattern, Math.min(game.getMaxMove(), game.getTarget() - game.getSum()));
    }

    @Override
    public String illegalMoveNotification(Game game) {
        return String.format(illegalMovePattern, game.getTarget(), game.getMaxMove());
    }

}
