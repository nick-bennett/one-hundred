## Description

_One Hundred_ is a simple mathematical game, in which players take turns adding to or subtracting from a running total until the target value (for an addition game) or zero (for subtraction) is reached exactly. The last player to move (that is, the player whose addition move causes the total to reach the target, or whose subtraction move causes the result to reach zero) is the winner.

In this implementation the players are the user and the computer, with the user making the first move in the first game, and the first move alternating between the computer and the user in successive games.

## Running the application

## Options

Gameplay can be customized by specifying one or more of the following
options on the command line:

| Option                            | Description                                                                                                                                                                                        |
|:----------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `-?`, `--help`                    | Displays this usage and instructions screen.                                                                                                                                                       |
| `-b`, `--bound `*`<upper bound>`* | Specifies the target sum (for addition game) or initial value (for subtraction game); the default is 100.                                                                                          |
| `-m`, `--max `*`<maximum move size>`* | Specifies the maximum number that can be added to (for addition game) or subtracted from (for subtraction game) the current value in any single move; the default is 10.                           |
| `-o`, `--operation `*`<move operation>`* | Specifies the arithmetic operation used in a game. Specify ADDITION for an addition game, and `SUBTRACTION` for a subtraction game; the default is `ADDITION`. (This value is not case-sensitive.) |
| `-s`, `--strategy `*`<computer strategy>`* | Specifies the move strategy used by the computer. The supported values are `OPTIMAL` and `RANDOM`; the default is `OPTIMAL`. (This value is not case-sensitive.) |

[Javadocs](api/)