package edu.neumont.chessmasters;

import edu.neumont.chessmasters.controllers.PlayerMove;
import edu.neumont.chessmasters.events.EventListener;
import edu.neumont.chessmasters.events.EventRegistry;
import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.GameSettings;
import me.travja.utils.utils.IOUtils;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChessMasters {

    private static double wScore = 0;
    private static double bScore = 0;

    public static void increaseWScore(double increase) {
        wScore = wScore + increase;
    }

    public static void increaseBScore(double increase) {
        bScore = bScore + increase;
    }

    public static String getScoreboard() {
        return "Overall Score: White - " + wScore + " Black - " + bScore;
    }

    public static PlayerMove controller;

    public static void executeWrappedJar(String[] args) {
        //This just allows the jar to be double-clicked in windows.
        try {
            CodeSource codeSource = ChessMasters.class.getProtectionDomain().getCodeSource();
            String path = codeSource.getLocation().toURI().getPath();
            String jarDir = new File(path).getParentFile().getPath();
            String fileName = new File(path).getName();
            Runtime.getRuntime().exec(new String[]{
                    "cmd.exe",
                    "/c start cmd.exe /k \"java -jar \"" + jarDir + File.separator + fileName + "\" --start " + String.join(" ", args) + "\""});
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static final String help =
            "Options:\n" +
                    "\t-h  --help              \tDisplay this help and exit\n" +
                    "\t-c  --color   <tristate>\tConfigure color support\n" +
                    "\t-u  --unicode <tristate>\tConfigure unicode support\n" +
                    "\t-t  --trace   <tristate>\tConfigure tracing the last move made (requires color)\n" +
                    "\t-f  --flip    <tristate>\tConfigure flipping the board to face the player\n" +
                    "\t-d  --debug             \tEnable debug mode\n" +
                    "\n" +
                    "\t<tristate>\tOne of { yes | no | on | off | 1 | 0 | true | false | enable | disable | auto }\n";

    public static Boolean parseTristate(List<String> argv, String option) {
        if (argv.size() < 1) {
            System.err.println(
                    "[ warn ]\tOption " + option + " expects one of {true|yes|on|1|enable}|{false|no|off|0|disable}|auto; received nothing. Ignoring");
            return null;
        }
        String input = argv.remove(0);
        switch (input) {
            case "true":
            case "yes":
            case "on":
            case "1":
            case "enable":
                return true;
            case "false":
            case "no":
            case "off":
            case "0":
            case "disable":
                return false;
            case "auto":
                return null;
            default:
                System.err.println(
                        "[ warn ] Option " + option + " expects one of {true|yes|on|1|enable}|{false|no|off|0|disable}|auto; received " + input
                                + ". Ignoring");
                return null;
        }
    }

    public static GameSettings parseArgs(String[] args) {
        GameSettings options = new GameSettings(args);
        options.needsWrapping = System.getProperty("os.name").toLowerCase().contains("windows");

        // convert array into a more friendly format
        List<String> argv = new ArrayList<String>();
        Collections.addAll(argv, args);

        // no need to have an index, but doing it this way lets us alter the size of the list if
        // we wish to, e.g. in case of an option of the form "--option parameter"
        while (argv.size() > 0) {
            String option = argv.remove(0);

            if (option != null && option.trim().length() > 0)
                switch (option.trim()) {

                    case "-h":
                    case "--help":
                        System.out.println(help);
                        System.exit(0);

                    case "--start":
                        options.start = true;
                        options.needsWrapping = false;
                        break;

                    case "--debug":
                        System.err.println("[ debug ]\tDebug mode");
                        options.debug = true;
                        options.needsWrapping = false;
                        break;

                    case "-c":
                    case "--color":
                        options.color = parseTristate(argv, option);
                        break;

                    case "-u":
                    case "--unicode":
                        options.unicode = parseTristate(argv, option);
                        break;

                    case "-t":
                    case "--trace":
                        options.traceMoves = parseTristate(argv, option);
                        break;

                    case "-f":
                    case "--flip":
                        Boolean temp = parseTristate(argv, option);
                        if (temp != null) options.flip = temp;
                        break;

                    default:
                        // ignore unknown options
                        // actually, right now, let's complain about them
												if (options.debug)
													System.err.println("[ warn ] unknown option: " + option);
                        break;
                }
        }
        return options;
    }

    public static void main(String[] args) {
        checkColorSupport();
        GameSettings options = parseArgs(args);

        if (options.needsWrapping) {
            executeWrappedJar(args);
        } else {
            registerEvents();
            startGame(options);
        }
    }

    public static void loadSettings(GameSettings options) {
        if (options.color != null) Utils.USE_ANSI = options.color;
        if (options.unicode != null) Utils.USE_UNICODE = options.unicode;
        if (options.traceMoves == null)
            options.traceMoves = Utils.USE_ANSI; // by default, trace moves if color is allowed
    }

    public static void startGame(GameSettings options) {
        boolean playAgain;

        // detect pipey things going on
        if (System.console() == null) {
            System.err.println(" ----[ IMPORTANT ]----\n" +
                    "This game session is not directly attached to a Console object. (Either stdin or stdout is being redirected.)\n" +
                    "If you enter a null string for any prompt, the application WILL terminate.\n" +
                    "Chances are, you know what you're doing, but if you accessed the application in a normal way, please let the developers know that you're receiving this error.\n");
        }

        loadSettings(options);

        do {
            //Run setup here.
//                Board board = new Board("r2qk2r/8/8/8/8/8/8/R2QK2R w KQkq - 0 1");
            controller = PlayerMove.inst(new Board());
            controller.run(options);
            if (IOUtils.promptForBoolean("Dump move log? (y/n)", "y", "n"))
                controller.dumpMoveLog();
            playAgain = IOUtils.promptForBoolean("Play again? (y/n)", "y", "n");
        } while (playAgain);
    }

    public static void checkColorSupport() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            AnsiConsole.systemInstall();
            Utils.USE_ANSI = true;
        }
    }

    private static void registerEvents() {
        EventRegistry.registerEvents(new EventListener());
    }
}
