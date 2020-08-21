package edu.neumont.chessmasters.controllers;

import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.pieces.Piece;
import edu.neumont.chessmasters.models.pieces.PieceColor;
import me.travja.utils.utils.IOUtils;

import java.io.EOFException;
import java.util.regex.Pattern;

public class PlayerMove {

    private        Board      board;
    private static PlayerMove inst;
    private        int        counter  = 0;
    private        String     status   = null;
    private        boolean    gameOver = false;
    public         String     positionOne;
    public         String     positionTwo;

    public PlayerMove() {
        this(new Board());
    }

    public PlayerMove(Board board) {
        inst = this;
        this.board = board;
    }

    public static PlayerMove inst() {
        return inst;
    }

    public Board getBoard() {
        return board;
    }

    public void run() throws EOFException {
        gameOver = false;
        boolean keepPlaying;
        do {
            keepPlaying = MenuPrompt();
        } while (keepPlaying);
    }

    public void setGameOver() {
        this.gameOver = true;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    //Returns a boolean dependent on if the player intends to quit the game or not.
    public boolean MenuPrompt() throws EOFException {
        board.clearPassant(counter % 2 == 0 ? PieceColor.WHITE : PieceColor.BLACK);
        System.out.println("\n\n" + board);
        boolean movePieceCheck;
        MoveResult result = null;
        do {
            if (status != null) {
                System.out.println(status);
                setStatus(null);
            }
            if (gameOver)
                return false;

            StringBuilder sb = new StringBuilder();
            if (counter == 0)
                sb.append("Welcome to Chess Masters");
            sb.append("\n\n");

            System.out.println(sb);
            String input = null;
            boolean isInt = false;


            do {

                positionOne = null;
                positionTwo = null;
                if(input != null && !input.equalsIgnoreCase("board"))
                    System.out.println("Unrecognized command. Type 'help' for help.");
                input = IOUtils.promptForString((counter % 2 == 0 ? "White" : "Black") + ", it's your turn:\nEnter a choice: ").toLowerCase();
                movePieceCheck = Pattern.matches("[A-Ha-h][1-8][\\s][A-Ha-h][1-8]", input);

                if (movePieceCheck) {
                    String[] text = input.split(Pattern.quote(" "));
                    positionOne = text[0];
                    positionTwo = text[1];

                    input = "1";

                } else if (input.equals("help")) {
                    input = "2";
                } else if(input.equals("board")){
                    System.out.println("\n\n" + board);
                    continue;
                }


                try {
                    Integer.parseInt(input);
                    isInt = true;
                } catch (NumberFormatException e) {
                    isInt = false;
                }
            } while (!isInt && !input.equalsIgnoreCase("quit"));


            boolean check = input.equalsIgnoreCase("quit");

            if (!check) {

                switch (Integer.parseInt(input)) {
                    case 1:
                        result = (positionOne == null || positionTwo == null) ? MoveResult.FAILED : PromptMove();
                        if (result == MoveResult.FAILED) {
                            if (status == null)
                                setStatus("That move could not be performed.");
                        } else {
                            counter++;
                        }


                        break;
                    case 2:
                        helpMenu();
                        break;
                    case 3:
                        return false;
                }
                return true;
            }
        } while (result == MoveResult.FAILED);
        return false;
    }

    public MoveResult PromptMove() {
        MoveResult result = MoveResult.FAILED;

        //Prompts user for first location
        boolean checkPiece = true;
        boolean checkSecond = true;
        //System.out.println("\nWhich piece do you want to move based on location {Ex: A1}");


//        do {
        //positionOne = IOUtils.promptForString("Enter a string: ");
        checkPiece = CheckMove(positionOne);

        //checks if position is true (valid location)
        if (checkPiece) {
            Piece p = board.getSquare(positionOne);

            if (p == null) {
                checkPiece = false;
                setStatus("no piece here");
                return MoveResult.FAILED;
            } else {
                PieceColor colorPiece = p.getColor();

                if (counter % 2 == 0) {
                    //Checking white piece
                    if (colorPiece != PieceColor.WHITE) {
                        checkPiece = false;
                        setStatus("wrong piece color");
                        return MoveResult.FAILED;
                    }

                } else {
                    //Checking black turn {Piece}
                    if (colorPiece != PieceColor.BLACK) {
                        checkPiece = false;
                        setStatus("wrong piece color");
                        return MoveResult.FAILED;
                    }

                }
            }
        }
//        } while (!checkPiece);


//        do {
        //System.out.println("Position You want to move piece Ex: A2");
        //positionTwo = IOUtils.promptForString("Enter a string  ");
        checkSecond = CheckMove(positionTwo);
        if (!checkSecond) {
            setStatus("You have chosen an incorrect location ");
            return MoveResult.FAILED;
        }
//        } while (!checkSecond);


        //System.out.println(positionOne);


        if (result != MoveResult.QUIT) {
            //Checks users input is a valid position.
            boolean c = board.movePiece(positionOne, positionTwo);
            result = c ? MoveResult.MOVED : MoveResult.FAILED;
        }

        return result;
    }

    private boolean CheckMove(String po1) {
        boolean checkerOne = Pattern.matches("[A-Ha-h][1-8]", po1);
        return checkerOne;


    }

    private boolean QuitGame(String s) {
        String quit = "quit";

        if (s.equals(quit)) {
            System.out.println("You have given up because you do not want to play");
            return true;
//            System.exit(0);
        }

        return false;
    }

    private void helpMenu() throws EOFException {
        StringBuilder helper = new StringBuilder(" --[ Helper commands ]-- ");

        helper.append("\n\nTo move, simply input your target piece (a2) and your destination (a4). Ex: 'a2 a4' moves the piece at a2 to a4")
                .append("\n\nTo quit/forfeit the game simply type 'quit' whenever\n")
                .append("\nEn Passant: When a '*' is displayed, an en passant is possible. This occurs when a pawn takes its initial move two spaces,\n")
                .append("but could be intercepted by an opposing piece. If the opportunity is not taken, it is lost.\n");
        //System.out.println(
        setStatus(helper.toString());
    }


    public enum MoveResult {
        QUIT,
        MOVED,
        FAILED
    }

}
