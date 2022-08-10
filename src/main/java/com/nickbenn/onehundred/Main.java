package com.nickbenn.onehundred;

import com.nickbenn.onehundred.controller.ConsoleSession;

/**
 * Implements entry point for console-mode implementation of <strong>"One Hundred"</strong>, a
 * simple example of "Nim" (which is really an entire category of mathematical games).
 */
public class Main {

  /**
   * Entry-point method.
   *
   * @param args Command-line arguments, which are parsed using
   * {@link com.nickbenn.onehundred.controller.CommandLineOptions}.
   */
  public static void main(String[] args) {
    new ConsoleSession(args).run();
  }

}
