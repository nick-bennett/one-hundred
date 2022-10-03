# One Hundred 

A console-mode implementation of a simple form of the mathematical game _Nim_.

In this game, two players (the user and the computer) take turns adding to (or subtracting from) a running total. Each addition (or subtraction) may not exceed the maximum move (10 by default). The player that first reaches the target value (100 is the default for the addition game; 0 is the target for the subtraction game, and the players start at 100 by default) _exactly_ wins the game.

After running the Gradle `installDist` task (in the `distribution` category), the `build/install/one-hundred/bin` directory contains a launcher script (`one-hundred` for *n*x, `one-hundred.bat` for Windows). This script can be run to launch the application.

To see the command-line options (for choosing the game "direction", setting the target explicitly, changing the maximum move, etc.), run 

```shell
one-hundred --help
```

or

```shell
one-hundred -?
```

Copyright &copy; 2022 Nicholas Bennett. See LICENSE for software license terms.