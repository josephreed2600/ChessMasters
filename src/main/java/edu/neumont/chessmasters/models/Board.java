package edu.neumont.chessmasters.models;

import edu.neumont.chessmasters.Utils;

public class Board {
	private class Piece {}
	// ok, a little weird, but a4 can be represented by File.A and Rank(4)
	// jk let's not
	//private static enum File { A = 0, B = 1, C = 2, D = 3, E = 4, F = 5, G = 6, H = 7 };
	private static int File(String file) {
		switch (file.toUpperCase()) {
			case "A": return 0;
			case "B": return 1;
			case "C": return 2;
			case "D": return 3;
			case "E": return 4;
			case "F": return 5;
			case "G": return 6;
			case "H": return 7;
			default: throw new IllegalArgumentException("File must be between A and H, inclusive");
		}
	}
	private static int File(Character file) {
		return File(Character.toString(file));
	}
	private static int Rank(int rank) {
		if (rank < 1 || rank > 8)
			throw new IllegalArgumentException("Rank must be between 1 and 8, inclusive");
		return rank - 1;
	}
	private static int Rank(String rank) {
		return Rank(Integer.parseInt(rank));
	}
	private static int Rank(Character rank) {
		return Rank(Character.toString(rank));
	}

	private Piece[][] squares;
	//public Piece[][] getSquares() { return squares; }
	//public void setSquares(Piece[][] squares) { this.squares = squares; }

	public Board() {
		this.squares = new Piece[8][8];
	}

	public boolean placePiece(Piece p, int rank, int file) {
		return false;
	}

	@Override
	public String toString() {

		// Build the top edge
		String top = Utils.buildRow(
				"   " + Utils.Drawing.Corners.topLeft,
				Utils.Drawing.Edges.horizontal + Utils.Drawing.Edges.horizontal + Utils.Drawing.Edges.horizontal,
				Utils.Drawing.Joints.horizontalDown,
				Utils.Drawing.Corners.topRight,
				8
				) + "\n";

		// Build the row separator
		String rowSep = Utils.buildRow(
				"   " + Utils.Drawing.Joints.verticalRight,
				Utils.Drawing.Edges.horizontal + Utils.Drawing.Edges.horizontal + Utils.Drawing.Edges.horizontal,
				Utils.Drawing.Joints.cross,
				Utils.Drawing.Joints.verticalLeft,
				8
				) + "\n";

		// Build the bottom edge
		String bottom = Utils.buildRow(
				"   " + Utils.Drawing.Corners.bottomLeft,
				Utils.Drawing.Edges.horizontal + Utils.Drawing.Edges.horizontal + Utils.Drawing.Edges.horizontal,
				Utils.Drawing.Joints.horizontalUp,
				Utils.Drawing.Corners.bottomRight,
				8
				) + "\n";

		// Assemble the board
		StringBuilder sb_out = new StringBuilder();

		String prefix = top;
		int rankIndex = 8;

		boolean squareColor = true; // white, starting at a8

		// Build each row
		for (Piece[] rank : squares) {
			sb_out.append(prefix);

			sb_out.append(' ').append(rankIndex--).append(' ');
			for (Piece piece : rank) {
				sb_out
					.append(Utils.Drawing.Edges.vertical)
					.append(squareColor ? Utils.Colors.Background.darkgray : Utils.Colors.Background.black)
					.append(' ')
					.append(piece == null ? '-' : piece)
					.append(' ')
					.append(Utils.Colors.reset)
					;
				squareColor = !squareColor;
			}
			squareColor = !squareColor;

			sb_out
				.append(Utils.Drawing.Edges.vertical)
				.append('\n')
				;

			prefix = rowSep;
		}

		sb_out.append(bottom);
		sb_out.append("     a   b   c   d   e   f   g   h  ");

		return sb_out.toString();
	}
}
