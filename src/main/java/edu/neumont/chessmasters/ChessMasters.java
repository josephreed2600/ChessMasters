package edu.neumont.chessmasters;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import edu.neumont.chessmasters.models.*;
import edu.neumont.chessmasters.controllers.PlayerMove;

public class ChessMasters {

    public static boolean debug = false;

    private static boolean arrayContains(String[] arr, String test) {
        for (String s : arr) {
            if (s.equalsIgnoreCase(test))
                return true;
        }

        return false;
    }

    public static void main(String[] args) {

        //This just allows the jar to be double-clicked in windows.
        if (!System.getProperty("os.name").toLowerCase().contains("windows") || (args.length >= 1 && (arrayContains(args, "-start") || arrayContains(args, "-debug")))) {
            debug = arrayContains(args, "-debug");
            PlayerMove.PromptChoice();
//        System.out.println("You input '" + input + "'");
//        String input = IOUtils.promptForString("Enter a string: ");
            //Basic IOUtils use
        } else {
            try {
                CodeSource codeSource = ChessMasters.class.getProtectionDomain().getCodeSource();
                String jarDir = new File(codeSource.getLocation().toURI().getPath()).getParentFile().getPath();
                Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c start cmd.exe /k \"java -jar \"" + jarDir + "\\ChessMasters-1.0-SNAPSHOT.jar\" -start\""});
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }


}
