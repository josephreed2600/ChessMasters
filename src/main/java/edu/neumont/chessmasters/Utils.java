package edu.neumont.chessmasters;

import static org.fusesource.jansi.Ansi.ansi;

public class Utils {
	public static boolean USE_UNICODE = !System.getProperty("os.name").startsWith("Windows");
	public static boolean USE_ANSI = !System.getProperty("os.name").startsWith("Windows");

    public static void clearConsole() {
		if (USE_ANSI)
			System.out.println(ansi().eraseScreen());
    }

    public static class Drawing {
		public static class Corners {
			public static String topLeft        () { return  USE_UNICODE ? "\u250c" : "+"; }
			public static String topRight       () { return  USE_UNICODE ? "\u2510" : "+"; }
			public static String bottomLeft     () { return  USE_UNICODE ? "\u2514" : "+"; }
			public static String bottomRight    () { return  USE_UNICODE ? "\u2518" : "+"; }
		}
		public static class Edges {
			public static String horizontal     () { return  USE_UNICODE ? "\u2500" : "-"; }
			public static String vertical       () { return  USE_UNICODE ? "\u2502" : "|"; }
		}
		public static class Joints {
			public static String verticalLeft   () { return  USE_UNICODE ? "\u2524" : "+"; }
			public static String verticalRight  () { return  USE_UNICODE ? "\u251c" : "+"; }
			public static String horizontalUp   () { return  USE_UNICODE ? "\u2534" : "+"; }
			public static String horizontalDown () { return  USE_UNICODE ? "\u252c" : "+"; }
			public static String cross          () { return  USE_UNICODE ? "\u253c" : "+"; }
		}
	}

	public static class Styles {

		public static String reset         () { return  USE_ANSI ? "\u001b[0m"   : ""; }
		public static String bold          () { return  USE_ANSI ? "\u001b[1m"   : ""; }
		public static String normal        () { return  USE_ANSI ? "\u001b[22m"  : ""; } // unbold

		public static String lightSquare   () { return  Background.darkgray(); }
		public static String darkSquare    () { return  Background.black(); }
		public static String sourceSquare  () { return  Background.red(); }
		public static String destSquare    () { return  Background.green(); }
		public static String lightPiece    () { return  bold() + Foreground.white(); }
		public static String darkPiece     () { return  bold() + Foreground.lightgray(); }
		public static String afterPiece    () { return  normal(); }

		public static class Foreground {
			public static String black       () { return  USE_ANSI ? "\u001b[30m"  : ""; }
			public static String red         () { return  USE_ANSI ? "\u001b[31m"  : ""; }
			public static String green       () { return  USE_ANSI ? "\u001b[32m"  : ""; }
			public static String yellow      () { return  USE_ANSI ? "\u001b[33m"  : ""; }
			public static String blue        () { return  USE_ANSI ? "\u001b[34m"  : ""; }
			public static String magenta     () { return  USE_ANSI ? "\u001b[35m"  : ""; }
			public static String cyan        () { return  USE_ANSI ? "\u001b[36m"  : ""; }
			public static String white       () { return  USE_ANSI ? "\u001b[97m"  : ""; }
			public static String lightgray   () { return  USE_ANSI ? "\u001b[37m"  : ""; }
			public static String darkgray    () { return  USE_ANSI ? "\u001b[90m"  : ""; }
		}
		public static class Background {
			public static String black       () { return  USE_ANSI ? "\u001b[40m"  : ""; }
			public static String red         () { return  USE_ANSI ? "\u001b[41m"  : ""; }
			public static String green       () { return  USE_ANSI ? "\u001b[42m"  : ""; }
			public static String yellow      () { return  USE_ANSI ? "\u001b[43m"  : ""; }
			public static String blue        () { return  USE_ANSI ? "\u001b[44m"  : ""; }
			public static String magenta     () { return  USE_ANSI ? "\u001b[45m"  : ""; }
			public static String cyan        () { return  USE_ANSI ? "\u001b[46m"  : ""; }
			public static String white       () { return  USE_ANSI ? "\u001b[107m" : ""; }
			public static String lightgray   () { return  USE_ANSI ? "\u001b[47m"  : ""; }
			public static String darkgray    () { return  USE_ANSI ? "\u001b[100m" : ""; }
		}
	}

	// count indcates how many times outer will be repeated
	// left outer inner outer inner outer inner outer right
	public static String buildRow(String left, String outer, String inner, String right, int count) {
		String prefix = left;
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < count; i++) {
			out.append(prefix).append(outer);
			prefix = inner;
		}
		out.append(right);
		return out.toString();
	}
}
