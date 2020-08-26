# ChessMasters
Welcome to Chess Masters! The ultimate command-line Chess application!

## Basic Rundown
Our application covers all the basics of chess as well as some of the more advanced moves involved in Chess.

### Commands
Commands are as follows:
> - To execute a move, simply enter the piece location followed by the destination (ex: a2 a4)
> - A castle can be performed by moving the king to the rook's square. Both king and queen-side castling are supported.
> - En Passant is also available. When a pawn is moved forward two spaces and could be intercepted by another pawn, a '*' is shown. If this square is moved into by a pawn, an en passant is performed.
> - At any time, enter 'moves', 'history', or 'dump' to receive a list of your run commands.
> - Enter 'forfeit' to concede the match.
> - Type 'fen' to receive a printout of your board in FEN notation.
> - Type 'board' to see the current board.
> - Type 'options' to edit settings like color, board flipping, and unicode.
> - If at any time you're a little lost, enter 'help' or '?'

### Draw Conditions
The 50-move rule is enforced automatically as well as most cases where checkmate is not possible.
In the case where pawns may be blocking Kings or Bishops from getting to the opponent's king, our methods fall short.
The simple check checks for cases listed under 'Impossibility of Checkmate' [here](https://en.wikipedia.org/wiki/Draw_(chess)#Draws_in_all_games).
Stalemate has also been implemented where the player is not in check, but has no legal moves.

#### Windows Information
Most environments require that you execute something similar to `java -jar ChessMasters.jar`
in order to run executables. The jar is indeed able to be double-clicked to execute in a Windows environment.

#### Potential Future Additions
We may implement file saving/loading so you can continue previous games.

##### Included Libraries
- [Jansi](https://github.com/fusesource/jansi) - For colors on Windows
- [TravjaUtils](https://github.com/Travja/TravjaUtils) - For easy I/O
- [JUnit](https://junit.org/junit5/) - For testing