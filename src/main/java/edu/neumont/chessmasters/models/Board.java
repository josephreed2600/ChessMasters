package edu.neumont.chessmasters.models;

import edu.neumont.chessmasters.Utils;
import edu.neumont.chessmasters.models.Location;
import edu.neumont.chessmasters.models.pieces.*;

public class Board {

	// y, x
	private Piece[][] squares;
	//public Piece[][] getSquares() { return squares; }
	//public void setSquares(Piece[][] squares) { this.squares = squares; }

	public Board() {
		this.squares = new Piece[8][8];
		for (int file = 0; file < 8; file++) {
			placePiece(new Pawn(PieceColor.WHITE), new Location(file, 1));
			placePiece(new Pawn(PieceColor.BLACK), new Location(file, 6));
		}

		placePiece(new   Rook(PieceColor.WHITE), new Location("a1"));
		placePiece(new Knight(PieceColor.WHITE), new Location("b1"));
		placePiece(new Bishop(PieceColor.WHITE), new Location("c1"));
		placePiece(new  Queen(PieceColor.WHITE), new Location("d1"));
		placePiece(new   King(PieceColor.WHITE), new Location("e1"));
		placePiece(new Bishop(PieceColor.WHITE), new Location("f1"));
		placePiece(new Knight(PieceColor.WHITE), new Location("g1"));
		placePiece(new   Rook(PieceColor.WHITE), new Location("h1"));

		placePiece(new   Rook(PieceColor.BLACK), new Location("a8"));
		placePiece(new Knight(PieceColor.BLACK), new Location("b8"));
		placePiece(new Bishop(PieceColor.BLACK), new Location("c8"));
		placePiece(new  Queen(PieceColor.BLACK), new Location("d8"));
		placePiece(new   King(PieceColor.BLACK), new Location("e8"));
		placePiece(new Bishop(PieceColor.BLACK), new Location("f8"));
		placePiece(new Knight(PieceColor.BLACK), new Location("g8"));
		placePiece(new   Rook(PieceColor.BLACK), new Location("h8"));
	}

	public boolean placePiece(Piece p, Location l) {
		if (squares[l.getY()][l.getX()] != null) return false;
		squares[l.getY()][l.getX()] = p;
		return true;
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
		for (int row = 7; row >= 0; row--) {
			Piece[] rank = squares[row];
			sb_out.append(prefix);

			sb_out.append(' ').append(rankIndex--).append(' ');
			for (Piece piece : rank) {
				sb_out
					.append(Utils.Drawing.Edges.vertical)
					.append(squareColor ? Utils.Styles.lightSquare : Utils.Styles.darkSquare)
					.append(' ')
					.append(piece == null ? '-' : piece)
					.append(' ')
					.append(Utils.Styles.reset)
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
