/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.model.strategy.Strategy;
import com.nickbenn.onehundred.model.strategy.Strategy.StrategyInitializationException;
import com.nickbenn.onehundred.model.Game;
import com.nickbenn.onehundred.model.Game.Operation;
import java.util.ResourceBundle;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Defines several position-independent command-line options, along with the
 * {@link #parse(String[])} method for populating corresponding fields from the command line at
 * runtime, and accessor methods to interrogate the resulting option values. The options defined
 * include one ({@code --help} or {@code -?}) which is used to display a usage information/help
 * screen.
 */
public class CommandLineOptions {

  private static final String OPTIONS_BUNDLE_NAME = "options";

  private static final String BOUND_SHORT_OPTION = "b";
  private static final String BOUND_LONG_OPTION = "bound";
  private static final String MAX_MOVE_SHORT_OPTION = "m";
  private static final String MAX_MOVE_LONG_OPTION = "max";
  private static final String OPERATION_SHORT_OPTION = "o";
  private static final String OPERATION_LONG_OPTION = "operation";
  private static final String STRATEGY_SHORT_OPTION = "s";
  private static final String STRATEGY_LONG_OPTION = "strategy";
  private static final String HELP_SHORT_OPTION = "?";
  private static final String HELP_LONG_OPTION = "help";

  private final Options options;
  private final String syntax;
  private final String header;
  private final String footer;

  private int bound;
  private int maxMove;
  private Operation operation;
  private Strategy strategy;
  private boolean helpRequested;

  /**
   * Initialize this instance by defining the various options to be recognized by
   * {@link #parse(String[])}.
   */
  public CommandLineOptions() {
    ResourceBundle bundle = ResourceBundle.getBundle(OPTIONS_BUNDLE_NAME);
    options = new Options();
    options.addOption(buildBoundOption(bundle));
    options.addOption(buildMaxMoveOption(bundle));
    options.addOption(buildOperationOption(bundle));
    options.addOption(buildStrategyOption(bundle));
    options.addOption(buildHelpOption(bundle));
    syntax = bundle.getString(Keys.SYNTAX);
    header = bundle.getString(Keys.HEADER);
    footer = bundle.getString(Keys.FOOTER);
  }

  /**
   * Parses the specified {@link String String[]} (presumably containing the command-line arguments
   * passed to the applications entry-point {@code main} method). If a given option is not present
   * in {@code args}, the relevant default value (specified as a constant in the {@link Game} class)
   * will be used.
   *
   * @param args Command-line arguments.
   * @throws ParseException                  If the command-line arguments are not recognized as
   *                                         specifying the options defined here.
   * @throws StrategyInitializationException If the specified (or default) {@link Strategy} cannot
   *                                         be instantiated.
   * @throws IllegalArgumentException        If any of the option values cannot be parsed from the
   *                                         {@link String} representation used in the command line
   *                                         to the appropriate type.
   */
  public void parse(String[] args)
      throws ParseException, StrategyInitializationException, IllegalArgumentException {
    CommandLine commandLine = new DefaultParser().parse(options, args);
    bound = commandLine.hasOption(BOUND_SHORT_OPTION)
        ? Integer.parseInt(commandLine.getOptionValue(BOUND_SHORT_OPTION))
        : Game.DEFAULT_UPPER_BOUND;
    maxMove = commandLine.hasOption(MAX_MOVE_SHORT_OPTION)
        ? Integer.parseInt(commandLine.getOptionValue(MAX_MOVE_SHORT_OPTION))
        : Game.DEFAULT_MAX_MOVE;
    operation = commandLine.hasOption(OPERATION_SHORT_OPTION)
        ? Operation.valueOf(commandLine.getOptionValue(OPERATION_SHORT_OPTION).toUpperCase())
        : Game.DEFAULT_OPERATION;
    strategy = Strategy.newInstance(
        commandLine.hasOption(STRATEGY_SHORT_OPTION)
            ? commandLine.getOptionValue(STRATEGY_SHORT_OPTION).toLowerCase()
            : ConsoleSolitaireReferee.DEFAULT_STRATEGY_KEY
    );
    helpRequested = commandLine.hasOption(HELP_SHORT_OPTION);
  }

  /**
   * Displays the usage syntax and list of available options, with short descriptions of (and
   * default values for) each.
   */
  public void showHelp() {
    new HelpFormatter().printHelp(syntax, header, options, footer);
  }

  /**
   * Returns the target value (for the addition game) or the starting value (for the subtraction
   * game). This value is taken either from the {@code args} passed to the {@link #parse(String[])}
   * method, or from the default value specified in {@link Game#DEFAULT_UPPER_BOUND}.
   *
   * @return (See above.)
   */
  public int getBound() {
    return bound;
  }

  /**
   * Returns the maximum value that can be added (for the addition game) or subtracted (for the
   * subtraction game). This value is taken either from the {@code args} passed to the
   * {@link #parse(String[])} method, or from the default value specified in
   * {@link Game#DEFAULT_MAX_MOVE}.
   *
   * @return (See above.)
   */
  public int getMaxMove() {
    return maxMove;
  }

  /**
   * Returns the game operation or direction (that is, addition or subtraction) as an instance of
   * the {@link Operation} {@code enum}. This value is taken either from the {@code args} passed to
   * the {@link #parse(String[])} method, or from the default value specified in
   * {@link Game#DEFAULT_OPERATION}.
   *
   * @return (See above.)
   */
  public Operation getOperation() {
    return operation;
  }

  /**
   * Returns a key identifying the playing {@link Strategy} used by the computer. This value is
   * taken either from the {@code args} passed to the {@link #parse(String[])} method, or from the
   * default value specified in {@link ConsoleSolitaireReferee#DEFAULT_STRATEGY_KEY}.
   *
   * @return (See above.)
   */
  public Strategy getStrategy() throws StrategyInitializationException {
    return strategy;
  }

  /**
   * Indicates that the {@code --help} or {@code -?} option was passed to {@link #parse(String[])}
   * in {@code args}. This flag is used to display a usage information/help screen <i>instead of</i>
   * starting a game.
   *
   * @return (See above.)
   */
  public boolean isHelpRequested() {
    return helpRequested;
  }

  private static Option buildBoundOption(ResourceBundle bundle) {
    return Option
        .builder()
        .option(BOUND_SHORT_OPTION)
        .longOpt(BOUND_LONG_OPTION)
        .required(false)
        .hasArg()
        .optionalArg(false)
        .type(Integer.class)
        .argName(bundle.getString(Keys.BOUND_ARG_NAME))
        .desc(String.format(
            bundle.getString(Keys.BOUND_DESCRIPTION_FORMAT), Game.DEFAULT_UPPER_BOUND))
        .build();
  }

  private static Option buildMaxMoveOption(ResourceBundle bundle) {
    return Option
        .builder()
        .option(MAX_MOVE_SHORT_OPTION)
        .longOpt(MAX_MOVE_LONG_OPTION)
        .required(false)
        .hasArg()
        .optionalArg(false)
        .type(Integer.class)
        .argName(bundle.getString(Keys.MAX_MOVE_ARG_NAME))
        .desc(String.format(
            bundle.getString(Keys.MAX_MOVE_DESCRIPTION_FORMAT), Game.DEFAULT_MAX_MOVE))
        .build();
  }

  private static Option buildOperationOption(ResourceBundle bundle) {
    return Option
        .builder()
        .option(OPERATION_SHORT_OPTION)
        .longOpt(OPERATION_LONG_OPTION)
        .required(false)
        .hasArg()
        .optionalArg(false)
        .type(Operation.class)
        .argName(bundle.getString(Keys.OPERATION_ARG_NAME))
        .desc(String.format(
            bundle.getString(Keys.OPERATION_DESCRIPTION_FORMAT), Game.DEFAULT_OPERATION))
        .build();
  }

  private static Option buildStrategyOption(ResourceBundle bundle) {
    return Option
        .builder()
        .option(STRATEGY_SHORT_OPTION)
        .longOpt(STRATEGY_LONG_OPTION)
        .required(false)
        .hasArg()
        .optionalArg(false)
        .type(String.class)
        .argName(bundle.getString(Keys.STRATEGY_ARG_NAME))
        .desc(String.format(bundle.getString(Keys.STRATEGY_DESCRIPTION_FORMAT),
            ConsoleSolitaireReferee.DEFAULT_STRATEGY_KEY.toUpperCase()))
        .build();
  }

  private static Option buildHelpOption(ResourceBundle bundle) {
    return Option
        .builder()
        .option(HELP_SHORT_OPTION)
        .longOpt(HELP_LONG_OPTION)
        .required(false)
        .hasArg(false)
        .desc(bundle.getString(Keys.HELP_DESCRIPTION))
        .build();
  }

}
