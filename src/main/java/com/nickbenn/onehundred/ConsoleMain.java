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

  /**
   * Entry-point method.
   *
   * @param args Command-line arguments, which are passed to the
   * {@link ConsoleSession#ConsoleSession(String[])} constructor.
   */
  public static void main(String[] args) {
    new ConsoleSession(args).run();
  }

}
