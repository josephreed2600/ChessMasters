package edu.neumont.chessmasters.models;

import edu.neumont.chessmasters.ChessMasters;
import edu.neumont.chessmasters.Utils;
import edu.neumont.chessmasters.models.Location;
import edu.neumont.chessmasters.models.pieces.PieceColor;
import edu.neumont.chessmasters.exceptions.IncompleteMoveException;

import java.util.regex.Pattern;

public class Move {

		public final Location from, to;

		public Move(Location from, Location to) {
			this.from = from;
			this.to = to;
		}

		public Move(String from, String to) {
			this(new Location(from), new Location(to));
		}

		public static Move fromSrcDest(String input) {
			if (ChessMasters.controller.getSettings().debug)
				System.err.println("[ debug ] Parsing source-destination syntax");
			String[] squares = input.split(" ");
			return new Move(squares[0], squares[1]);
		}

		public static Move fromFreeform(String input, Board board) throws IncompleteMoveException {
			// check for king-side castling
			if (Pattern.matches("[Oo]-?[Oo]", input)) {
				System.err.println("[ debug ] Parsing castling O-notation");
				if (Utils.Turns.getColor(board) == PieceColor.WHITE)
					return Move.fromFreeform("e1 h1", board);
				else
					return Move.fromFreeform("e8 h8", board);
			}
			// check for queen-side castling
			if (Pattern.matches("[Oo]-?[Oo]-?[Oo]", input)) {
				System.err.println("[ debug ] Parsing castling O-notation");
				if (Utils.Turns.getColor(board) == PieceColor.WHITE)
					return Move.fromFreeform("e1 a1", board);
				else
					return Move.fromFreeform("e8 a8", board);
			}
			if (Pattern.matches("[A-Ha-h][1-8] [A-Ha-h][1-8]", input)) {
				return fromSrcDest(input);
			}
			// someone please know a better way to do this
			if (Pattern.matches("[A-Ha-h][1-8][A-Ha-h][1-8]", input)) {
				return fromSrcDest(input.substring(0,2) + " " + input.substring(2,4));
			}
			if (Pattern.matches("[A-Ha-h][1-8]", input)) {
				throw new IncompleteMoveException("Missing destination square");
			}
			throw new UnsupportedOperationException("I don't know how to parse the move described by \"" + input + "\"");
		}

		public String toString() { return from + " " + to; }
}
