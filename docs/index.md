## Description

_One Hundred_ is a simple mathematical game, in which players take turns adding to or subtracting from a running total until the target value (for an addition game) or zero (for subtraction) is reached exactly. The last player to move (that is, the player whose addition move causes the total to reach the target, or whose subtraction move causes the result to reach zero) is the winner.

In this implementation the players are the user and the computer, with the user making the first move in the first game, and the first move alternating between the computer and the user in successive games.

## Building the application

### Distribution archives  

The code in this repository is configured for building with Gradle, using the Gradle wrapper. To build `.tar` and `.zip` distribution archives (including `.jar` files for the application code and all supporting libraries, as well as launcher scripts for Windows, OS X, and Linux), execute the following:

```shell
gradlew assembleDist
```

### Installation

To build a distribution in a form that is executable directly from a command shell (Bash, ZShell, Windows Command Prompt, PowerShell, etc.) or the development environment, without having to extract files from the archives mentioned above, use the `installDist` task: 

```shell
gradlew installDist
```

After successful execution of the `installDist` task, the `build/install/one-hundred` directory will have the same contents as the distribution archives mentioned above, but in an unarchived form, ready for execution. 

## Running the application

After executing the `assembleDist` or `installDist` task (and after extracting the archive contents in the target environment, in the former case), the executable files are in the `bin` directory, named `one-hundred` and `one-hundred.bat`, and can be executed to launch the application:

```shell
one-hundred
```

## Command-line options

Gameplay can be customized by specifying one or more of the following
options on the command line:

| Option                                     | Description                                                                                                                                                                                        |
|:-------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `-?`, `--help`                             | Displays a usage and instructions screen (i.e. the contents of this table).                                                                                                                        |
| `-b`, `--bound` *`<upper bound>`*          | Specifies the target sum (for addition game) or initial value (for subtraction game); the default is 100.                                                                                          |
| `-m`, `--max` *`<maximum move size>`*      | Specifies the maximum number that can be added to (for addition game) or subtracted from (for subtraction game) the current value in any single move; the default is 10.                           |
| `-o`, `--operation` *`<move operation>`*   | Specifies the arithmetic operation used in a game. Specify ADDITION for an addition game, and `SUBTRACTION` for a subtraction game; the default is `ADDITION`. (This value is not case-sensitive.) |
| `-s`, `--strategy` *`<computer strategy>`* | Specifies the move strategy used by the computer. The supported values are `OPTIMAL` and `RANDOM`; the default is `OPTIMAL`. (This value is not case-sensitive.)                                   |

For example, the following would launch the application for a subtraction game, starting at 50, with a maximum of 8 subtracted in each move:

```shell
one-hundred -o subtraction -b 50 -m 8
```

## [Javadocs](api/)  
