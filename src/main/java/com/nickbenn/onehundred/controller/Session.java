/*
 * Copyright 2022 Nicholas Bennett. Licensed for use under the MIT License.
 */
package com.nickbenn.onehundred.controller;

import java.util.Arrays;

/**
 * Generalizes launch of user interaction session and access to command-line arguments. Concrete
 * subclasses must implement {@link #run()} for management of user interaction (through completion).
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

  /**
   * Handles execution of the application, including processing any command-line options previously
   * passed to {@link Session#Session(String[])}.
   */
  public abstract void run();

  /**
   * Returns the command-line arguments, as passed to {@link Session#Session(String[])}. The
   * reference returned is to a safe copy; thus any changes to the array contents by a consumer of
   * this method will not be reflected in the backing array, and subsequent invocations will return
   * it unchanged.
   *
   * @return
   */
  @SuppressWarnings("JavadocDeclaration")
  protected String[] getArgs() {
    return Arrays.copyOf(args, args.length);
  }

}
