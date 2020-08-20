package edu.neumont.chessmasters;

import edu.neumont.chessmasters.controllers.PlayerMove;
import edu.neumont.chessmasters.events.EventListener;
import edu.neumont.chessmasters.events.EventRegistry;
import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.GameSettings;
import me.travja.utils.utils.IOUtils;
import me.travja.utils.utils.FileUtils;

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

		public static void executeWrappedJar() {
			//This just allows the jar to be double-clicked in windows.
			try {
				CodeSource codeSource = ChessMasters.class.getProtectionDomain().getCodeSource();
				String jarDir = new File(codeSource.getLocation().toURI().getPath()).getParentFile().getPath();
				Runtime.getRuntime().exec(new String[]{
					"cmd.exe", "/c start cmd.exe /k \"java -jar \"" + jarDir + "\\ChessMasters.jar\" --start\""});
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}

    public static void main(String[] args) {
			options = new GameSettings(args);
			options.needsWrapping = System.getProperty("os.name").toLowerCase().contains("windows");

			List<String> argv = new ArrayList<String>();
			Collections.addAll(argv, args);

			// no need to have an index, but doing it this way lets us alter the size of the list if
			// we wish to, e.g. in case of an option of the form "--option parameter"
			while (argv.size() > 0) {
				String option = argv.remove(0);
				System.out.println("Processing option: " + option);

				if (option != null)
				switch (option) {

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
						if (argv.size() < 1)
							System.err.println(
									"[ warn ]\tOption " + option + " expects one of {true|yes|on|1|enable}|{false|no|off|0|disable}|auto; received nothing. Ignoring");
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

					case "--file":
					case "-f":
						// handle files here
						if (argv.size() < 1)
							System.err.println("[ warn ]\tOption " + option + " expects a file path; received nothing. Ignoring");
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
					executeWrappedJar();
        } else {
            registerEvents();
            startGame();
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
