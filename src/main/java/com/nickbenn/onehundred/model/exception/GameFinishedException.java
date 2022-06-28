package com.nickbenn.onehundred.model.exception;

public class GameFinishedException extends IllegalStateException {

    public GameFinishedException(String message) {
        super(message);
    }

}
