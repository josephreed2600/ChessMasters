package Controllers;


import me.travja.utils.utils.IOUtils;

import java.util.regex.*;

public class PlayerMove {

    public static void PromptChoice() {

        //Prompts user for first location
        System.out.println("Which piece do you want to move based on location {Ex: A1}");
        String positionOne = IOUtils.promptForString("Enter a string: ");


        //System.out.println(positionOne);

        System.out.println("Position You want to move piece Ex: A2");
        String positionTwo = IOUtils.promptForString("Enter a string  ");

        //Checks users input is a valid position.
        CheckMove(positionOne, positionTwo);
    }

    public static void CheckMove(String po1, String po2) {
        boolean checkerOne = Pattern.matches("[A-H][1-8]", po1);
        boolean checkerTwo = Pattern.matches("[A-H][1-8]", po2);


        if (checkerOne == true) {
            System.out.println("You are correct");

            if (checkerTwo == true) {
                System.out.println("both of your inputs are valid");
                //Sends both moves to next method to move piece
            }
        } else {
            System.out.println("one of your inputs was invalid \n TRY AGAIN");
            PromptChoice();
        }

    }


}
