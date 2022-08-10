package com.nickbenn.onehundred.controller;

import java.util.Arrays;

public abstract class Session {

  private final String[] args;

  protected Session(String[] args) {
    this.args = Arrays.copyOf(args, args.length);
  }

  public abstract void run();

  protected String[] getArgs() {
    return args;
  }

}
