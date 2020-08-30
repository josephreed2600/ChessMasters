package edu.neumont.chessmasters.controllers;

import edu.neumont.chessmasters.ChessMasters;
import edu.neumont.chessmasters.Utils;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerMove {





    private        Board        board;
    private static PlayerMove   inst;
    private        String       status = null;
    private        boolean      gameOver;
    private        GameSettings options;



    public String getColorName() {
        return Utils.Turns.getColorName(board);
    }

    public PieceColor getColor() {
        return Utils.Turns.getColor(board);
    }

    public int getTurn() {
        return Utils.Turns.getTurn(board);
    }

    public boolean isWhite() {
        return Utils.Turns.isWhite(board);
    }

    public boolean isBlack() {
        return Utils.Turns.isBlack(board);
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

    public void run(GameSettings options) {
        this.options = options;
        gameOver = false;
        boolean keepPlaying;
        helpMenu();
        do {
            keepPlaying = RequestMove();
            if (this.gameOver) {
                Utils.clearConsole();
                System.out.println("\n" + board.toString(PieceColor.WHITE, options.traceMoves) + "\n");
                this.flushStatus();
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
     * 2.0 Print status
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
    public boolean RequestMove() {
        // 1. Clear en passant targets for this player
        board.clearPassant(getColor());

        // 2. Print board
        Utils.clearConsole();
        System.out.println("\n\n" + board.toString(options.flip ? getColor() : PieceColor.WHITE, options.traceMoves));
        this.flushStatus();
        // 2.1 End-game logic, assuming we have not already reached end-game
        if (!gameOver) {
            // 2.1.1 If we're in a stalemate, say so and end the game
            if (board.checkStalemate(getColor())) {
                this.setStatus(getColorName() + " has been forced into a stalemate. 1/2-1/2");
                this.setGameOver();
                return false;
                // 2.1.2 If the board is in a dead position, say so and end the game.
            } else if (board.isDeadPosition()) {
                setStatus("DRAW! Checkmate is no longer possible. 1/2-1/2");
                this.setGameOver();
                return false;
            } else if (board.getMovesSinceCap() >= 50) {
                setStatus("It has been 50 moves since the last capture or pawn advancement. The game ends in a draw. 1/2-1/2");
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
                case "quit":
                    // 3.2.1 Execute command
                    System.exit(0); // exits application
                case "forfeit":
                    // 3.2.1 Execute command
                    this.setStatus(getColorName() + " has elected to forfeit. " + (isWhite() ? "0-1" : "1-0"));
                    this.flushStatus();
                    this.setGameOver();
                    return false; // exits method, indicating game is over
                case "board":
                    Utils.clearConsole();
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
                    this.flushStatus();
                    continue; // skips rest of loop and asks again for a move
                case "fen":
                    System.out.println(board.toFEN());
                    continue;
                case "options":
                case "settings":
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
        StringBuilder helper = new StringBuilder();

        helper
                .append("\n\nTo move, simply input your target piece (a2) and your destination (a4). Ex: 'a2 a4' moves the piece at a2 to a4")
                .append("\n\n`forfeit' to give up and optionally play another game")
                .append("\n\n`quit' or `exit' to close the application")
                .append("\n\n`dump' to show the move history")
                .append("\n\n`save' and `load' to save this game or restore the last saved game")
                .append("\n\n`options' to configure settings")
                .append("\n\nEn Passant: When a '*' is displayed, an en passant is possible. This occurs when a pawn takes its initial move two spaces, but could be intercepted by an opposing piece. If the opportunity is not taken, it is lost.")
                .append("\n\nCastling may be accomplished by moving the king to the rook's square, or using O-O to castle king-side and O-O-O to castle queen-side.")
                .append("\n");
        setStatus(helper.toString());
        //this.flushStatus();
    }

    public void dumpMoveLog() {
        System.out.println("\nMove history:\n\n" + board.getMoveHistory() + "\n");
    }


    public void saveGame() {

        boolean saved = false;
        do {
            String slot = IOUtils.promptForString("Name this save:");





            new File("saves").mkdir();

            if (slot.equalsIgnoreCase("list")) {
                System.out.println("You can't name the file that.");
                continue;
            }

            if (slot.equalsIgnoreCase("exit") || slot.equalsIgnoreCase("quit")) {
                return;
            }

            boolean overwrite = true;
            if (new File("saves" + File.separator + slot + ".chess").exists() && !FileUtils.readFileFully("saves" + File.separator + slot + ".chess").isEmpty()) {
                overwrite = IOUtils.promptForBoolean("It appears there is already a save by that name. Would you like to overwrite it? (Y/N)", "y.yes", "n.no");
            }

            if (overwrite) {
                FileUtils.write("saves" + File.separator + slot + ".chess", board.toFEN());
                if (FileUtils.readFileFully("saves" + File.separator + slot + ".chess").equals(board.toFEN())) {
                    System.out.println("Game saved!");
                }
                saved = true;
            } else {
                System.out.println("Game was not overwritten");
            }
        } while (!saved);

    }


    public void loadGame() {
        boolean loaded = false;
        do {
            String slot = IOUtils.promptForString("Which save should we load?");
            if (slot.equalsIgnoreCase("exit") || slot.equalsIgnoreCase("quit")) {
                return;
            }

            if (slot.equalsIgnoreCase("list")) {
                List<File> files = FileUtils.getFiles("saves");
                if (files.size() == 0) System.out.println("No saves.");
                for (int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    System.out.println((i + 1) + ". " + file.getName().replace(".chess", ""));
                }
            } else if (new File("saves" + File.separator + slot + ".chess").exists() && !FileUtils.readFileFully("saves" + File.separator + slot + ".chess").isEmpty()) {
                System.out.println("Loading game...");
                String boardFen = FileUtils.readFileFully("saves" + File.separator + slot + ".chess");
                Board savedBoard = new Board(boardFen);
                board = savedBoard;
                System.out.println(board);
                loaded = true;
            } else {
                System.out.println("That game does not exist.");
                loaded = false;
            }
        } while (!loaded);


    }


    private void editOptions() {

        MenuOption colors = new MenuOption("Colors: \t" + (getSettings().color != null ? getSettings().color : "auto"), () -> {
            Boolean input = ChessMasters.parseTristate(new ArrayList<>(Arrays.asList(IOUtils.promptForString("Enter a new value for 'Colors': "))), "color");
            getSettings().color = input;
            System.out.println("Setting updated to " + (input == null ? "auto" : input));
        });

        MenuOption unicode = new MenuOption("Unicode: \t" + (getSettings().unicode != null ? getSettings().unicode : "auto"), () -> {
            Boolean input = ChessMasters.parseTristate(new ArrayList<>(Arrays.asList(IOUtils.promptForString("Enter a new value for 'Unicode': "))), "unicode");
            getSettings().unicode = input;
            System.out.println("Setting updated to " + (input == null ? "auto" : input));
        });

        MenuOption trace = new MenuOption("Trace: \t" + (getSettings().traceMoves != null ? getSettings().traceMoves : "auto"), () -> {
            Boolean input = ChessMasters.parseTristate(new ArrayList<>(Arrays.asList(IOUtils.promptForString("Enter a new value for 'Trace': "))), "trace");
            getSettings().traceMoves = input;
            System.out.println("Setting updated to " + (input == null ? "auto" : input));
        });

        MenuOption flip = new MenuOption("Flip: \t" + getSettings().flip, () -> {
            boolean input;
            Boolean temp = ChessMasters.parseTristate(new ArrayList<>(Arrays.asList(IOUtils.promptForString("Enter a new value for 'Flip': "))), "flip");
            input = temp != null ? temp : getSettings().flip;
            getSettings().flip = input;
            if (temp != null)
                System.out.println("Setting updated to " + input);
            else
                System.out.println("Setting was not updated.");
        });

        MenuOption debug = new MenuOption("Debug: \t" + getSettings().debug, () -> {
            boolean input;
            Boolean temp = ChessMasters.parseTristate(new ArrayList<>(Arrays.asList(IOUtils.promptForString("Enter a new value for 'Debug': "))), "debug");
            input = temp != null ? temp : getSettings().debug;
            getSettings().debug = input;
            if (temp != null)
                System.out.println("Setting updated to " + input);
            else
                System.out.println("Setting was not updated.");
        });

        Menu menu = new Menu("What would you like to edit?", colors, unicode, trace, flip, debug)
                .setPerpetual(() -> {
                    colors.setOption("Colors: \t" + (getSettings().color != null ? getSettings().color : "auto"));
                    unicode.setOption("Unicode: \t" + (getSettings().unicode != null ? getSettings().unicode : "auto"));
                    trace.setOption("Trace: \t" + (getSettings().traceMoves != null ? getSettings().traceMoves : "auto"));
                    flip.setOption("Flip: \t" + getSettings().flip);
                    debug.setOption("Debug: \t" + getSettings().debug);

                    ChessMasters.loadSettings(getSettings());

                    Utils.clearConsole();
                });
        menu.open(true);

        Utils.clearConsole();
        System.out.println("\n\n" + board.toString(options.flip ? getColor() : PieceColor.WHITE, options.traceMoves));
    }

}
