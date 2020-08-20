package edu.neumont.chessmasters;

public class Utils {
	public static final boolean USE_UNICODE = false;
	public static boolean USE_ANSI = !System.getProperty("os.name").startsWith("Windows");

	public static class Drawing {
		public static class Corners {
			public static final String topLeft        = USE_UNICODE ? "\u250c" : "+";
			public static final String topRight       = USE_UNICODE ? "\u2510" : "+";
			public static final String bottomLeft     = USE_UNICODE ? "\u2514" : "+";
			public static final String bottomRight    = USE_UNICODE ? "\u2518" : "+";
		}
		public static class Edges {
			public static final String horizontal     = USE_UNICODE ? "\u2500" : "-";
			public static final String vertical       = USE_UNICODE ? "\u2502" : "|";
		}
		public static class Joints {
			public static final String verticalLeft   = USE_UNICODE ? "\u2524" : "+";
			public static final String verticalRight  = USE_UNICODE ? "\u251c" : "+";
			public static final String horizontalUp   = USE_UNICODE ? "\u2534" : "+";
			public static final String horizontalDown = USE_UNICODE ? "\u252c" : "+";
			public static final String cross          = USE_UNICODE ? "\u253c" : "+";
		}
	}

	public static class Styles {

		public static final String reset         = USE_ANSI ? "\u001b[0m"   : "";
		public static final String bold          = USE_ANSI ? "\u001b[1m"   : "";
		public static final String normal        = USE_ANSI ? "\u001b[22m"  : ""; // unbold

		public static final String lightSquare   = Background.darkgray;
		public static final String darkSquare    = Background.black;
		public static final String lightPiece    = bold + Foreground.white;
		public static final String darkPiece     = bold + Foreground.lightgray;
		public static final String afterPiece    = normal;

		public static class Foreground {
			public static final String black       = USE_ANSI ? "\u001b[30m"  : "";
			public static final String red         = USE_ANSI ? "\u001b[31m"  : "";
			public static final String green       = USE_ANSI ? "\u001b[32m"  : "";
			public static final String yellow      = USE_ANSI ? "\u001b[33m"  : "";
			public static final String blue        = USE_ANSI ? "\u001b[34m"  : "";
			public static final String magenta     = USE_ANSI ? "\u001b[35m"  : "";
			public static final String cyan        = USE_ANSI ? "\u001b[36m"  : "";
			public static final String white       = USE_ANSI ? "\u001b[97m"  : "";
			public static final String lightgray   = USE_ANSI ? "\u001b[37m"  : "";
			public static final String darkgray    = USE_ANSI ? "\u001b[90m"  : "";
		}
		public static class Background {
			public static final String black       = USE_ANSI ? "\u001b[40m"  : "";
			public static final String red         = USE_ANSI ? "\u001b[41m"  : "";
			public static final String green       = USE_ANSI ? "\u001b[42m"  : "";
			public static final String yellow      = USE_ANSI ? "\u001b[43m"  : "";
			public static final String blue        = USE_ANSI ? "\u001b[44m"  : "";
			public static final String magenta     = USE_ANSI ? "\u001b[45m"  : "";
			public static final String cyan        = USE_ANSI ? "\u001b[46m"  : "";
			public static final String white       = USE_ANSI ? "\u001b[107m" : "";
			public static final String lightgray   = USE_ANSI ? "\u001b[47m"  : "";
			public static final String darkgray    = USE_ANSI ? "\u001b[100m" : "";
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
