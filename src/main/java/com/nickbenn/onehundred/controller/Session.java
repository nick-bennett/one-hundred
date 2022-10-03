/*
 * Copyright 2022 Nicholas Bennett. Licensed for use under the MIT License.
 */
package com.nickbenn.onehundred.controller;

import java.util.Arrays;

/**
 * Generalizes launch of user interaction session and access to command-line arguments. Concrete
 * subclasses must implement {@link #run()} for management of user interaction (through completion).
 * Subclasses
 */
public abstract class Session {

  private final String[] args;

  /**
   * Stores a safe copy of the {@code args} array for access via the {@link #getArgs()} method.
   *
   * @param args Command-line arguments.
   */
  protected Session(String[] args) {
    this.args = Arrays.copyOf(args, args.length);
  }

  public abstract void run();

  protected String[] getArgs() {
    return args;
  }

}
