package com.nickbenn.onehundred.view;

import com.nickbenn.onehundred.model.Game;

public interface GamePresentation<T> {

    T stateRepresentation(Game game, String playerOne, String playerTwo);

    T movePresentation(int move, String player);

    T movePrompt(Game game);

    T illegalMoveNotification(Game game);

}
