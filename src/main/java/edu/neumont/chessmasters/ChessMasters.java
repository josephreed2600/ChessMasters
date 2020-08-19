package edu.neumont.chessmasters;

import edu.neumont.chessmasters.controllers.PlayerMove;
import edu.neumont.chessmasters.events.EventListener;
import edu.neumont.chessmasters.events.EventRegistry;
import edu.neumont.chessmasters.models.Board;
import me.travja.utils.utils.IOUtils;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;

public class ChessMasters {

    public static boolean    debug = false;
    public static PlayerMove controller;

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
            registerEvents();
            startGame();
        } else {
            try {
                CodeSource codeSource = ChessMasters.class.getProtectionDomain().getCodeSource();
                String jarDir = new File(codeSource.getLocation().toURI().getPath()).getParentFile().getPath();
                Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c start cmd.exe /k \"java -jar \"" + jarDir + "\\ChessMasters.jar\" -start\""});
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startGame() {
        boolean playAgain;
        if(System.console() == null) {
            System.out.println(" ----[ IMPORTANT ]----\n" +
                    "This game session is not directly attached to a Console object. (Either stdin or stdout is being redirected.)\n" +
                    "If you enter a null string for any prompt, the application WILL terminate.\n" +
                    "Chances are, you know what you're doing, but if you accessed the application in a normal way, please let the developers know that you're receiving this error.\n");
        }

        try {
            do {
                //Run setup here.
                Board board = new Board();
                controller = new PlayerMove(board);
                controller.run();
                playAgain = IOUtils.promptForBoolean("Play again? (y/n)", "y", "n");
            } while (playAgain);
        } catch (EOFException e) {
            System.err.println("Input stream was terminated. Exiting program.");
        }
        System.out.println("Goodbye");
    }

    private static void registerEvents() {
        EventRegistry.registerEvents(new EventListener());
    }
}
