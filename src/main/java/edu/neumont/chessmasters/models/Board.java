package edu.neumont.chessmasters.models;

import edu.neumont.chessmasters.Utils;
import edu.neumont.chessmasters.events.EventRegistry;
import edu.neumont.chessmasters.events.PieceCaptureEvent;
import edu.neumont.chessmasters.events.PostPieceMoveEvent;
import edu.neumont.chessmasters.models.pieces.*;

import java.util.ArrayList;

public class Board {

	// y, x
	private Piece[][] squares;

	private Piece getSquare(String s) {
		return getSquare(new Location(s));
	}

	public Piece getSquare(Location l) {
		return squares[l.getY()][l.getX()];
	}

	public void setSquare(Location l, Piece p) {
		squares[l.getY()][l.getX()] = p;
	}

	public King getKing(PieceColor color) {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Piece piece = getSquare(new Location(x, y));
				if (piece != null && piece instanceof King && piece.getColor() == color)
					return (King) piece;
			}
		}

		return null;
	}

	public ArrayList<Piece> getAllPieces() {
		ArrayList<Piece> pieces = new ArrayList<>();
		pieces.addAll(getAllPieces(PieceColor.BLACK));
		pieces.addAll(getAllPieces(PieceColor.WHITE));
		return pieces;
	}

	public ArrayList<Piece> getAllPieces(PieceColor color) {
		ArrayList<Piece> pieces = new ArrayList<>();
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Piece piece = getSquare(new Location(x, y));
				if (piece != null && piece.getColor() == color)
					pieces.add(piece);
			}
		}
		return pieces;
	}
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

	public void clearBoard() {
		this.squares = new Piece[8][8];
	}

	public boolean placePiece(Piece p, Location l) {
		if (getSquare(l) != null) return false;
		setSquare(l, p);
		p.setLocation(l);
		return true;
	}

	// Checks whether the exclusive range of squares is empty
	// (all squares between a and b, not including a and b)
	public boolean pathIsEmpty(Location a, Location b) {
		Location[] path = Location.getExclusiveRange(a, b);
		for (Location l : path)
			if (getSquare(l) != null)
				return false;
		return true;
	}

	public boolean validateMove(Piece p, Location dest) {
		// TODO implement checks for:
		//  - moving through pieces

		// check whether we're moving through a piece
		if (!pathIsEmpty(p.getLocation(), dest))
			return false;

		// check whether we're capturing
		Piece victim = getSquare(dest);
		if (victim != null) {
			// if we are, ensure that we're capturing an opponent
			if (victim.getColor() == p.getColor())
				return false;
			// if we're trying to capture a king, something has gone horribly wrong---we
			// shouldn't have been able to reach this configuration in the first place
			if (victim instanceof King)
				throw new UnsupportedOperationException
					("An attempt was made to capture a king, indicating that the game was in an illegal state");
		}

		if (victim != null &&
				( (p instanceof Pawn && p.getLocation().getX() != dest.getX())
						|| !(p instanceof Pawn) ) ) {
			PieceCaptureEvent event = new PieceCaptureEvent(p, victim); //Fire our capture event when a piece is captured.
			EventRegistry.callEvents(event);
		}

		if (p instanceof Pawn) {
			// in a valid move, either we're capturing or we're going straight forward
			return (victim != null) ^ (p.getLocation().getX() == dest.getX());
		} else if (p instanceof Rook
				|| p instanceof Knight
				|| p instanceof Bishop
				|| p instanceof Queen
				|| p instanceof King) {
			return true;
		} else {
			throw new UnsupportedOperationException("I don't know how to validate this move for this piece");
		}
	}

	public boolean movePiece(String from, String to) {
		return movePiece(new Location(from), new Location(to));
	}

	public boolean movePiece(Location from, Location to) {
		Piece p = getSquare(from);
		if (p == null) return false;
		if (!validateMove(p, to)) return false;
		if (!p.move(to)) return false;
		setSquare(to, p);
		setSquare(from, null);

		if (p instanceof Pawn && ((Pawn) p).shouldPromote()) { //Promote pawn to queen.
			p = new Queen(p.getColor());
			setSquare(to, p);
		}

		PostPieceMoveEvent post = new PostPieceMoveEvent(p);
		EventRegistry.callEvents(post);
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
