package edu.neumont.chessmasters.controllers;

import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.pieces.Piece;
import edu.neumont.chessmasters.models.pieces.PieceColor;
import me.travja.utils.utils.IOUtils;

import java.util.regex.Pattern;

public class PlayerMove {

    private        Board      board;
    private static PlayerMove inst;
    public         int        counter = 0;

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

    public void run() {
        boolean keepPlaying;
        do {
            keepPlaying = MenuPrompt();
        } while (keepPlaying);
    }


    //Returns a boolean dependent on if the player intends to quit the game or not.
    public boolean MenuPrompt() {
        System.out.println(board);
        StringBuilder sb = new StringBuilder("Welcome to Chess Masters");
        sb.append("\n\n").append("1)Make move \n").append("2)help").append("\n3)quit");
        System.out.println(sb);
        String input;
        boolean isInt;

        do {
            input = IOUtils.promptForString("Enter a choice");
            try {
                Integer.parseInt(input);
                isInt = true;
            } catch (NumberFormatException e) {
                isInt = false;
            }
        } while (!isInt && !input.equalsIgnoreCase("quit"));


        boolean check = input.equalsIgnoreCase("quit");

        if (!check) {
            MoveResult result;
            switch (Integer.parseInt(input)) {
                case 1:
                    while ((result = PromptMove()) == MoveResult.FAILED) {
                        System.out.println("Invalid move try again");
                    }

                    if (result == MoveResult.QUIT)
                        return false;
                    counter++;
                    System.out.println(board);
                    break;
                case 2:
                    helpMenu();
                    break;
                case 3:
                    return false;
            }
            return true;
        }

        return false;
    }

    public MoveResult PromptMove() {
        MoveResult result = MoveResult.FAILED;

        //Prompts user for first location
        boolean checkPiece = true;
        boolean checkSecond = true;
        System.out.println("\nWhich piece do you want to move based on location {Ex: A1}");
        String positionOne = null;
        String positionTwo = null;


        do {
            positionOne = IOUtils.promptForString("Enter a string: ");
            if (QuitGame(positionOne))
                return MoveResult.QUIT;

            checkPiece = CheckMove(positionOne);

            //checks if position is true (valid location)
            if (checkPiece) {
                Piece p = board.getSquare(positionOne);

                if (p == null) {
                    checkPiece = false;
                    System.out.println("no piece here");
                } else {
                    PieceColor colorPiece = p.getColor();

                    if (counter % 2 == 0) {
                        //Checking white piece
                        if (colorPiece != PieceColor.WHITE) {
                            checkPiece = false;
                            System.out.println("wrong piece color");
                        }

                    } else {
                        //Checking black turn {Piece}
                        if (colorPiece != PieceColor.BLACK) {
                            checkPiece = false;
                            System.out.println("wrong piece color");
                        }

                    }
                }
            }
        } while (!checkPiece);


        do {
            System.out.println("Position You want to move piece Ex: A2");
            positionTwo = IOUtils.promptForString("Enter a string  ");
            if (QuitGame(positionTwo))
                return MoveResult.QUIT;

            checkSecond = CheckMove(positionTwo);
            if (!checkSecond) {
                System.out.println("You have chosen an incorrect location ");
            }
        } while (!checkSecond);


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

    private void helpMenu() {
        StringBuilder helper = new StringBuilder("Helper commands");

        helper.append("\n\nWhen inputting a position only give two characters:\n Ex) A2").append("\nTo quit/forfeit the game simply type quit whenever\n");
        System.out.println(helper);

        MenuPrompt();
    }


    public enum MoveResult {
        QUIT,
        MOVED,
        FAILED
    }

}
