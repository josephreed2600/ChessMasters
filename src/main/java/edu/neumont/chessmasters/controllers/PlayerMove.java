package edu.neumont.chessmasters.controllers;

import me.travja.utils.utils.IOUtils;


import java.util.regex.Pattern;

public class PlayerMove {


    public static void MenuPrompt() {

        StringBuilder sb = new StringBuilder("Welcome to Chess Masters");
        sb.append("\n\n").append("1)Make move \n").append("2)help").append("3)quit");
        System.out.println(sb);
        String input = IOUtils.promptForString("Enter a choice");


        boolean check = input.equals("quit");

        do {
            switch (Integer.parseInt(input)) {
                case 1:
                    PromptMove();
                    break;
                case 2:
                    helpMenu();
                    break;
            }

        } while (check);
        QuitGame(input);

    }

    public static void PromptMove() {

        //Prompts user for first location
        System.out.println("Which piece do you want to move based on location {Ex: A1}");
        String positionOne = IOUtils.promptForString("Enter a string: ");

        QuitGame(positionOne);

        //System.out.println(positionOne);

        System.out.println("Position You want to move piece Ex: A2");
        String positionTwo = IOUtils.promptForString("Enter a string  ");

        QuitGame(positionTwo);

        //Checks users input is a valid position.
        CheckMove(positionOne, positionTwo);
    }

    private static void CheckMove(String po1, String po2) {
        boolean checkerOne = Pattern.matches("[A-Ha-h][1-8]", po1);
        boolean checkerTwo = Pattern.matches("[A-Ha-h][1-8]", po2);


        if (checkerOne  && checkerTwo ) {
            System.out.println("both of your inputs are valid");
            //Sends both moves to next method to move piece
        } else {
            System.out.println("one of your inputs was invalid \n TRY AGAIN");
            PromptMove();
        }

    }

    private static void QuitGame(String s) {
        String quit = "quit";

        if (s.equals(quit)) {
            System.out.println("You have given up because you do not want to play");
            System.exit(0);
        }
    }

    private static void helpMenu() {
        StringBuilder helper = new StringBuilder("Helper commands");

        helper.append("\nWhen inputting a position only give two characters:\n Ex) A2").append("\nTo quit/forfeit the game simply type quit whenever\n");
        System.out.println(helper);

        MenuPrompt();
    }


}
