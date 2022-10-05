/*
 * Copyright 2022 Nicholas Bennett. Licensed for use under the MIT License.
 */
package com.nickbenn.onehundred;

import com.nickbenn.onehundred.controller.ConsoleSession;

/**
 * Implements entry point for console-mode implementation of <strong>"One Hundred"</strong>, a
 * simple form of the Nim category of mathematical games.
 */
public class ConsoleMain {

  private ConsoleMain() {
    // Prevent creation (and documentation) of a default constructor.
  }

  /**
   * Acts as an entry-point for the application. Command-line argument handling and top-level
   * program execution are delegated to {@link ConsoleSession}.
   *
   * @param args Command-line arguments, which are parsed for game configuration options.
   */
  public static void main(String[] args) {
    new ConsoleSession(args).run();
  }

}
