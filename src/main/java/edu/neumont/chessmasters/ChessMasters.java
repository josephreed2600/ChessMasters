package edu.neumont.chessmasters;

import edu.neumont.chessmasters.controllers.PlayerMove;
import edu.neumont.chessmasters.events.EventListener;
import edu.neumont.chessmasters.events.EventRegistry;
import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.GameSettings;
import me.travja.utils.utils.IOUtils;
import me.travja.utils.utils.FileUtils;
import org.fusesource.jansi.AnsiConsole;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ChessMasters {

	public static GameSettings options;
    public static PlayerMove controller;

		public static void executeWrappedJar(String[] args) {
			//This just allows the jar to be double-clicked in windows.
			try {
				CodeSource codeSource = ChessMasters.class.getProtectionDomain().getCodeSource();
				String jarDir = new File(codeSource.getLocation().toURI().getPath()).getParentFile().getPath();
				Runtime.getRuntime().exec(new String[]{
					"cmd.exe", "/c start cmd.exe /k \"java -jar \"" + jarDir + "\\ChessMasters.jar\" --start " + String.join(" ", args) + "\""});
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}

		public static final String help = 
			"Options:\n"+
				"\t-h  --help              \tDisplay this help and exit\n" +
				"\t-c  --color   <tristate>\tConfigure color support\n" +
				"\t-u  --unicode <tristate>\tonfigure unicode support\n" +
				"\t-d  --debug             \tEnable debug mode\n" +
				"\n" +
				"\t<tristate>\tOne of { yes | no | on | off | 1 | 0 | true | false | enable | disable | auto }\n";

    public static void main(String[] args) {
			options = new GameSettings(args);
			options.needsWrapping = System.getProperty("os.name").toLowerCase().contains("windows");

			List<String> argv = new ArrayList<String>();
			Collections.addAll(argv, args);

			// no need to have an index, but doing it this way lets us alter the size of the list if
			// we wish to, e.g. in case of an option of the form "--option parameter"
			while (argv.size() > 0) {
				String option = argv.remove(0);

				if (option != null)
				switch (option) {

					case "-h":
					case "--help":
						System.out.println(help);
						System.exit(0);

					case "--start":
						options.start = true;
						options.needsWrapping = false;
						break;

					case "--debug":
						System.out.println("[ debug ]\tDebug mode");
						options.debug = true;
						options.needsWrapping = false;
						break;

					case "-c":
					case "--color":
						if (argv.size() < 1) {
							System.err.println(
									"[ warn ]\tOption " + option + " expects one of {true|yes|on|1|enable}|{false|no|off|0|disable}|auto; received nothing. Ignoring");
							break;
						}
						String colorSetting = argv.remove(0);
						switch (colorSetting) {
							case "true":  case "yes": case "on":  case "1": case "enable":
								options.color = true;
								break;
							case "false": case "no":  case "off": case "0": case "disable":
								options.color = false;
								break;
							case "auto":
								options.color = null;
								break;
							default:
								System.err.println(
										"[ warn ]\tOption " + option + " expects one of {true|yes|on|1|enable}|{false|no|off|0|disable}|auto; received " + colorSetting
										+ ". Ignoring");
						}
						break;

					case "-u":
					case "--unicode":
						if (argv.size() < 1) {
							System.err.println(
									"[ warn ]\tOption " + option + " expects one of {true|yes|on|1|enable}|{false|no|off|0|disable}|auto; received nothing. Ignoring");
							break;
						}
						String unicodeSetting = argv.remove(0);
						switch (unicodeSetting) {
							case "true":  case "yes": case "on":  case "1": case "enable":
								options.unicode = true;
								break;
							case "false": case "no":  case "off": case "0": case "disable":
								options.unicode = false;
								break;
							case "auto":
								options.unicode = null;
								break;
							default:
								System.err.println(
										"[ warn ]\tOption " + option + " expects one of {true|yes|on|1|enable}|{false|no|off|0|disable}|auto; received " + unicodeSetting
										+ ". Ignoring");
						}
						break;

					case "--file":
					case "-f":
						// handle files here
						if (argv.size() < 1) {
							System.err.println("[ warn ]\tOption " + option + " expects a file path; received nothing. Ignoring");
							break;
						}
						options.filePath = argv.remove(0);
						options.fileContents = FileUtils.readFileFully(options.filePath);
						System.out.println("Found file contents:\n" + options.fileContents);
						break;

					default:
						// ignore unknown options
						// actually, right now, let's complain about them
						System.out.println("[ warn ] unknown option: " + option);
						break;
				}
			}

        if (options.needsWrapping) {
					executeWrappedJar(args);
        } else {
            registerEvents();
            startGame(options);
        }
    }

    public static void startGame(GameSettings options) {
        boolean playAgain;
        checkColorSupport();
        if (System.console() == null) {
            System.out.println(" ----[ IMPORTANT ]----\n" +
                    "This game session is not directly attached to a Console object. (Either stdin or stdout is being redirected.)\n" +
                    "If you enter a null string for any prompt, the application WILL terminate.\n" +
                    "Chances are, you know what you're doing, but if you accessed the application in a normal way, please let the developers know that you're receiving this error.\n");
        }

				if(options.color != null) Utils.USE_ANSI = options.color;
				if(options.unicode != null) Utils.USE_UNICODE = options.unicode;

        try {
            do {
                //Run setup here.
//                Board board = new Board("r2qk2r/8/8/8/8/8/8/R2QK2R w KQkq - 0 1");
                controller = new PlayerMove();
                controller.run();
                playAgain = IOUtils.promptForBoolean("Play again? (y/n)", "y", "n");
            } while (playAgain);
        } catch (EOFException e) {
            System.err.println("Input stream was terminated. Exiting program.");
        }
        System.out.println("Goodbye");
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
