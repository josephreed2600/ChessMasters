package edu.neumont.chessmasters;

import edu.neumont.chessmasters.controllers.PlayerMove;
import edu.neumont.chessmasters.events.EventListener;
import edu.neumont.chessmasters.events.EventRegistry;
import edu.neumont.chessmasters.models.Board;
import me.travja.utils.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;

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
        do {
            //Run setup here.
            Board board = new Board();
            new PlayerMove(board).run();
            playAgain = IOUtils.promptForBoolean("Play again? (y/n)", "y", "n");
        } while (playAgain);
        System.out.println("Goodbye");
    }

    private static void registerEvents() {
        EventRegistry.registerEvents(new EventListener());
    }
}
