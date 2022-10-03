/*
 * Copyright 2022 Nicholas Bennett. See LICENSE for software license terms.
 */
package com.nickbenn.onehundred.controller;

import com.nickbenn.onehundred.controller.strategy.Strategy;
import com.nickbenn.onehundred.controller.strategy.Strategy.StrategyInitializationException;
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
 *
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

  public void showHelp() {
    new HelpFormatter().printHelp(syntax, header, options, footer);
  }

  public int getBound() {
    return bound;
  }

  public int getMaxMove() {
    return maxMove;
  }

  public Operation getOperation() {
    return operation;
  }

  public Strategy getStrategy() throws StrategyInitializationException {
    return strategy;
  }

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
