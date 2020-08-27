package edu.neumont.chessmasters.controllers;

import edu.neumont.chessmasters.ChessMasters;
import edu.neumont.chessmasters.exceptions.IncompleteMoveException;
import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.GameSettings;
import edu.neumont.chessmasters.models.Move;
import edu.neumont.chessmasters.models.pieces.Piece;
import edu.neumont.chessmasters.models.pieces.PieceColor;
import me.travja.utils.menu.Menu;
import me.travja.utils.menu.MenuOption;
import me.travja.utils.utils.FileUtils;
import me.travja.utils.utils.IOUtils;

import java.io.EOFException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerMove {

    private Board board;
    private static PlayerMove inst;
    private String status = null;
    private boolean gameOver = false;
    private GameSettings options;
    public String positionOne;
    public String positionTwo;

    public String getColorName() {
        return getColorName(board.getCounter());
    }

    public static String getColorName(int halfturn) {
        return halfturn % 2 == 0 ? "White" : "Black";
    }

    public PieceColor getColor() {
        return getColor(board.getCounter());
    }

    public static PieceColor getColor(int halfturn) {
        return halfturn % 2 == 0 ? PieceColor.WHITE : PieceColor.BLACK;
    }

    public int getTurn() {
        return getTurn(board.getCounter());
    }

    public static int getTurn(int halfturn) {
        return halfturn / 2 + 1;
    }

    public boolean isWhite() {
        return isWhite(board.getCounter());
    }

    public static boolean isWhite(int halfturn) {
        return halfturn % 2 == 0;
    }

    public boolean isBlack() {
        return isBlack(board.getCounter());
    }

    public static boolean isBlack(int halfturn) {
        return halfturn % 2 == 1;
    }

    public GameSettings getSettings() {
        return options;
    }


    public PlayerMove() {
        this(new Board());
    }

    private PlayerMove(Board board) {
        //inst = this;
        this.board = board;
        this.setStatus(null);
        this.gameOver = false;
    }

    public static PlayerMove inst() {
        if (inst == null) inst = new PlayerMove();
        return inst;
    }

    public static PlayerMove inst(Board board) {
        inst = new PlayerMove(board);
        return inst;
    }

    public Board getBoard() {
        return board;
    }

    public void run(GameSettings options) throws EOFException {
        this.options = options;
        gameOver = false;
        boolean keepPlaying;
        do {
            keepPlaying = RequestMove();
            if (this.gameOver) {
                this.flushStatus();
                System.out.println("\n" + board.toString(PieceColor.WHITE, options.traceMoves) + "\n");
                return;
            }
        } while (keepPlaying);
    }

    public void setGameOver() {
        this.gameOver = true;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatusIfEmpty(String status) {
        if (this.getStatus() == null) this.setStatus(status);
    }

    public String getStatus() {
        return status;
    }

    public void flushStatus() {
        if (this.getStatus() != null) {
            System.out.println(this.getStatus());
        }
        this.setStatus(null);
    }

    /**
     * Does not return until a valid move is made or the user forfeits.
     * 0. Increment the half-turn counter
     * 1. Clear en passant targets for this player
     * 2. Print board
     * 2.1 If we're in a stalemate, say so and end the game
     * 3. Do-while(not a valid move or a forfeit):
     * 3.1 Request user input
     * 3.2 Check whether it's a predefined command
     * 3.2.1 Execute command
     * 3.2.2 Continue if it wasn't a forfeit
     * 3.3 Attempt to parse as a move
     * 3.3.1 If successful, attempt to apply the resultant move
     * 3.3.1.1 If move is successful, return
     * 3.3.2 If either of these fails, continue prompting
     */
    public boolean RequestMove() throws EOFException {
        // 1. Clear en passant targets for this player
        board.clearPassant(getColor());

        // 2. Print board
        System.out.println("\n\n" + board.toString(options.flip ? getColor() : PieceColor.WHITE, options.traceMoves));
        // 2.1 End-game logic, assuming we have not already reached end-game
        if (!gameOver) {
            // 2.1.1 If we're in a stalemate, say so and end the game
            if (board.checkStalemate(getColor())) {
                this.setStatus(getColorName() + " has been forced into a stalemate. 1/2-1/2");
                this.setGameOver();
                return false;
                // 2.1.2 If the board is in a dead position, say so and end the game.
            } else if (board.isDeadPosition()) {
                setStatus("DRAW! Checkmate is no longer possible.");
                this.setGameOver();
                return false;
            } else if (board.getMovesSinceCap() >= 50) {
                setStatus("It has been 50 moves since the last capture or pawn advancement. The game ends in a draw");
                this.setGameOver();
                return false;
            }
        }

        // 3. Do-while(not a valid move or a forfeit):
        do {
            // 3.1 Request user input
            System.out.println("\n" + getColorName() + " to move");
            String input = IOUtils.promptForString(getTurn() + (isWhite() ? ". " : "... ")).toLowerCase();
            // 3.2 Check whether it's a predefined command
            switch (input) {
                case "exit":
                    // 3.2.1 Execute command
                    System.exit(0); // exits application
                case "forfeit":
                    // 3.2.1 Execute command
                    this.setStatus(getColorName() + " has elected to forfeit. " + (isWhite() ? "0-1" : "1-0"));
                    this.flushStatus();
                    this.setGameOver();
                    return false; // exits method, indicating game is over
                case "board":
                    System.out.println("\n\n" + board.toString(options.flip ? getColor() : PieceColor.WHITE, options.traceMoves));
                    continue; // skips rest of loop and asks again for a move
                case "moves":
                case "history":
                case "dump":
                    dumpMoveLog();
                    continue;
                case "help":
                case "?":
                    helpMenu();
                    continue; // skips rest of loop and asks again for a move
                case "fen":
                    System.out.println(board.toFEN());
                    continue;
                case "options":
                    editOptions();
                    continue;
                case "save":
                    saveGame();
                    continue;
                case "load":
                    loadGame();
                    continue;
                    // 3.2.2 Continue if it wasn't a forfeit
                default:
                    break; // carries on with loop
            }

            try {

                // 3.3 Attempt to parse as a move
                Move move;
                try {
                    move = Move.fromFreeform(input, board);
                } catch (IncompleteMoveException e) {
                    // we'll prompt for more input one more time
                    // if they don't complete the move, screw them
                    //System.out.print(isWhite() ? "  " : "    "); // i guess the next line trims prompts
                    input += " " + IOUtils.promptForString("to: ");
                    move = Move.fromFreeform(input, board);
                }

                // 3.3.1 If successful, attempt to apply the resultant move
                if (AttemptMove(move, board, getColor())) {
                    // 0. Increment the half-turn counter
                    board.incrCounter();
                    return true; // exits the method, indicating a successful move
                } else {
                    this.setStatusIfEmpty("Illegal move");
                    this.flushStatus();

                    // 3.3.2 If either of these fails, continue prompting
                    continue;
                }

            } catch (Exception e) {
                this.setStatusIfEmpty("Invalid move syntax or unrecognized command: \"" + input + "\". Try specifying a source and a destination square, like so: a2 a4");
                this.flushStatus();

                // 3.3.2 If either of these fails, continue prompting
                continue;
            }

        } while (true); // continue looping until something returns true or false

        // we better not get here tho
        //throw new IllegalStateException("We escaped a do-while(true) in PlayerMove");
    }

    // true for success, false for failure
    // 0. Ensure that the source square has a piece
    // 1. Ensure that the piece to move is of the correct color
    // 2. Attempt the move
    public boolean AttemptMove(Move move, Board board, PieceColor color) {
        Piece p = board.getSquare(move.from);
        // 0. Ensure that the source square has a piece
        if (p == null) {
            this.setStatus("The source square (" + move.from.toString() + ") is empty");
            return false;
        }
        // 1. Ensure that the piece to move is of the correct color
        if (p.getColor() != color) {
            this.setStatus("The requested piece (" + p.getNotation() + move.from.toString() + ") is of the wrong color");
            return false;
        }
        // 2. Attempt the move
        return board.movePiece(move);
    }

    private void helpMenu() {
        StringBuilder helper = new StringBuilder(" --[ Helper commands ]-- ");

        helper.append("\n\nTo move, simply input your target piece (a2) and your destination (a4). Ex: 'a2 a4' moves the piece at a2 to a4")
                .append("\n\nTo quit/forfeit the game simply type 'quit' whenever\n")
                .append("\nEn Passant: When a '*' is displayed, an en passant is possible. This occurs when a pawn takes its initial move two spaces,\n")
                .append("but could be intercepted by an opposing piece. If the opportunity is not taken, it is lost.\n");
        //System.out.println(
        setStatus(helper.toString());
        this.flushStatus();
    }

    public void dumpMoveLog() {
        System.out.println("\nMove history:\n\n" + board.getMoveHistory() + "\n");
    }


    public void saveGame() {
        FileUtils.write("save-ChessMasters", board.toFEN());


        if (FileUtils.readFileFully("save-ChessMasters") == board.toFEN()) {
            System.out.println("your game is saved");
        }


    }


    public void loadGame() {
        String boardFen = FileUtils.readFileFully("save-ChessMasters");


        Board savedBoard = new Board(boardFen);
        System.out.println(savedBoard);

    }


    private void editOptions() {
        MenuOption colors = new MenuOption("Colors: \t" + (getSettings().color != null ? getSettings().color : "auto"), () -> {
            Boolean input;
            try {
                input = ChessMasters.parseTristate(new ArrayList<>(Arrays.asList(IOUtils.promptForString("Enter a new value for 'Colors': "))), "color");
                getSettings().color = input;
                System.out.println("Setting updated to " + (input == null ? "auto" : input));
            } catch (EOFException e) {
                e.printStackTrace();
            }
        });
        MenuOption unicode = new MenuOption("Unicode: \t" + (getSettings().unicode != null ? getSettings().unicode : "auto"), () -> {
            Boolean input;
            try {
                input = ChessMasters.parseTristate(new ArrayList<>(Arrays.asList(IOUtils.promptForString("Enter a new value for 'Unicode': "))), "unicode");
                getSettings().unicode = input;
                System.out.println("Setting updated to " + (input == null ? "auto" : input));
            } catch (EOFException e) {
                e.printStackTrace();
            }
        });
        MenuOption trace = new MenuOption("Trace: \t" + (getSettings().traceMoves != null ? getSettings().traceMoves : "auto"), () -> {
            Boolean input;
            try {
                input = ChessMasters.parseTristate(new ArrayList<>(Arrays.asList(IOUtils.promptForString("Enter a new value for 'Trace': "))), "trace");
                getSettings().traceMoves = input;
                System.out.println("Setting updated to " + (input == null ? "auto" : input));
            } catch (EOFException e) {
                e.printStackTrace();
            }
        });
        MenuOption flip = new MenuOption("Flip: \t" + getSettings().flip, () -> {
            boolean input;
            try {
                Boolean temp = ChessMasters.parseTristate(new ArrayList<>(Arrays.asList(IOUtils.promptForString("Enter a new value for 'Flip': "))), "flip");
                input = temp != null ? temp : getSettings().flip;
                getSettings().flip = input;
                if (temp != null)
                    System.out.println("Setting updated to " + input);
                else
                    System.out.println("Setting was not updated.");
            } catch (EOFException e) {
                e.printStackTrace();
            }
        });
        MenuOption debug = new MenuOption("Debug: \t" + getSettings().debug, () -> {
            boolean input;
            try {
                Boolean temp = ChessMasters.parseTristate(new ArrayList<>(Arrays.asList(IOUtils.promptForString("Enter a new value for 'Debug': "))), "debug");
                input = temp != null ? temp : getSettings().debug;
                getSettings().debug = input;
                if (temp != null)
                    System.out.println("Setting updated to " + input);
                else
                    System.out.println("Setting was not updated.");
            } catch (EOFException e) {
                e.printStackTrace();
            }
        });

        Menu menu = new Menu("What would you like to edit?", colors, unicode, trace, flip, debug)
                .setPerpetual(() -> {
                    colors.setOption("Colors: \t" + (getSettings().color != null ? getSettings().color : "auto"));
                    unicode.setOption("Unicode: \t" + (getSettings().unicode != null ? getSettings().unicode : "auto"));
                    trace.setOption("Trace: \t" + (getSettings().traceMoves != null ? getSettings().traceMoves : "auto"));
                    flip.setOption("Flip: \t" + getSettings().flip);
                    debug.setOption("Debug: \t" + getSettings().debug);

                    ChessMasters.loadSettings(getSettings());
                });
        menu.open(true);
    }

}
