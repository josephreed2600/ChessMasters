package edu.neumont.chessmasters.models;

import edu.neumont.chessmasters.ChessMasters;
import edu.neumont.chessmasters.Utils;
import edu.neumont.chessmasters.events.EventRegistry;
import edu.neumont.chessmasters.events.PieceCaptureEvent;
import edu.neumont.chessmasters.events.PostPieceMoveEvent;
import edu.neumont.chessmasters.models.pieces.*;
import me.travja.utils.utils.IOUtils;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

public class Board {

	// y, x
	private Piece[][] squares;
	// Half-turns, starting at 0. To get turn number, (counter/2) + 1
	private int	   counter	   = 0;
	public  int	   movesSinceCap = 0;

	public Piece getSquare(String s) {
		return getSquare(new Location(s));
	}

	public Piece getSquare(Location l) {
		return squares[l.getY()][l.getX()];
	}

	public void setSquare(Location l, Piece p) {
		squares[l.getY()][l.getX()] = p;
		if (p != null) p.setLocation(l);
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int count) {
		this.counter = count;
	}

	public void incrCounter() {
		counter++;
	}

	public int getMovesSinceCap() {
		return movesSinceCap;
	}

	public void setMovesSinceCap(int count) {
		this.movesSinceCap = count;
	}

	public void incrMovesSinceCap() {
		movesSinceCap++;
	}

	private ArrayList<Move> moves;

	public ArrayList<Move> getMoves() { return moves; }
	public String getMoveHistory() {
		StringBuilder out = new StringBuilder();
		for (Move move : getMoves())
			out.append(move.toString() + "\n");
		return out.toString();
	}

	// Indicates whether to fire events. Ghost boards are used in determining checkmate,
	// so we don't want to issue alerts of captures made on them.
	public final boolean isGhostBoard;

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


	// copy ctors
	public Board(Board original) {
		this(original, true);
	}

	public Board(Board original, boolean isGhost) {
		this.squares = new Piece[8][8];
		this.moves = (ArrayList<Move>)original.getMoves().clone();
		this.isGhostBoard = isGhost;

		// copy pieces over
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				Location l = new Location(file, rank);
				Piece p = original.getSquare(l);
				if (p != null)
					this.setSquare(l, p.clone());
			}
		}
	}

	// create board with pawns
	public Board() {
		this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	}

	// create board from FEN
	public Board(String fen) {
		this.squares = new Piece[8][8];
		moves = new ArrayList<Move>();
		this.isGhostBoard = false;

		String[] components = fen.split(" ");
		String layout = components[0];
		PieceColor turn = components[1].equalsIgnoreCase("w") ? PieceColor.WHITE : PieceColor.BLACK;
		String castling = components[2];
		String passant = components[3];
		String halfClock = components[4];
		String counter = components[5];

		int index = 63; // iterate through the board
		for (Character p : layout.toCharArray()) {
			if (p == '/') continue;

			int rank = index / 8;
			int file = 7 - (index % 8);
			index--;

			try {
				index -= Integer.parseInt(p.toString()) - 1;
				continue;
			} catch (NumberFormatException nfe) {
				try {
					Piece piece = Piece.fromFEN(p.toString());
					PieceColor color = piece.getColor();
					setSquare(new Location(file, rank), piece);

					if (piece instanceof Rook || piece instanceof King) {
						if (color == PieceColor.WHITE) {
							if ((file == 0 && !castling.contains("Q")) || (file == 7 && !castling.contains("K")))
								piece.setNumMoves(1);
						} else if (color == PieceColor.BLACK) {
							if ((file == 0 && !castling.contains("q")) || (file == 7 && !castling.contains("k")))
								piece.setNumMoves(1);
						}
					} else if (piece instanceof Pawn) {
						if ((color == PieceColor.WHITE && rank != 1) || (color == PieceColor.BLACK && rank != 6))
							piece.setNumMoves(1); //No double moving!
					}
				} catch (UnsupportedOperationException uoe) {
					throw new IllegalArgumentException("Invalid character in FEN string: " + p);
				}
			}
		}

		if (!passant.equals("-")) {
			Location loc = new Location(passant);
			Piece owner = getSquare(loc.add(0, turn == PieceColor.WHITE ? -1 : 1));
			if (owner == null || owner.getColor() != turn.getOpposite() || !(owner instanceof Pawn))
				System.err.println("Could not add a passant target to " + passant + " as there is no corresponding pawn in front of it.");
			else
				setSquare(loc, new PassantTarget((Pawn) owner));
		}

		try {
			int half = Integer.parseInt(halfClock);
			setMovesSinceCap(half);
		} catch (NumberFormatException e) {
			System.err.println("Could not set the capture clock. '" + halfClock + "' is not a number. 0 will be used as default.");
		}

		try {
			int count = Integer.parseInt(counter);
			setCounter(((count-1) * 2 + (turn == PieceColor.WHITE ? 0 : 1))); //FEN counter strings start at 1, our counter starts at 0.
		} catch (NumberFormatException e) {
			System.err.println("Could not set the move counter. '" + counter + "' is not a number. 1 will be used as default.");
		}
	}

	public String toFEN() {
		StringBuilder ret = new StringBuilder();
		for (int rank = 7; rank >= 0; rank--) { //Build the layout
			int empty = 0;
			for (int file = 0; file < 8; file++) {
				Piece p = getSquare(new Location(file, rank));
				if (p == null || p instanceof PassantTarget) {
					empty++;
					continue;
				} else {
					if (empty > 0) {
						ret.append(empty);
						empty = 0;
					}
					ret.append(p.getNotation());
				}
			}
			if (empty > 0)
				ret.append(empty);

			if (rank != 0)
				ret.append("/");
		}

		ret.append(" ").append(counter % 2 == 0 ? "w" : "b"); //Whose turn is it?

		//Is casting available?
		boolean canCastleSet = false;
		ret.append(" ");
		for (PieceColor color : PieceColor.values()) {
			King king = getKing(color);
			if (king.getNumMoves() > 0)
				continue;
			Piece rank0 = getSquare(new Location(0, king.getLocation().getY()));
			Piece rank7 = getSquare(new Location(7, king.getLocation().getY()));
			if (rank7 instanceof Rook) {
				if (rank7.getNumMoves() == 0) {
					ret.append(color == PieceColor.WHITE ? "K" : "k");
					canCastleSet = true;
				}
			}
			if (rank0 instanceof Rook) {
				if (rank0.getNumMoves() == 0) {
					ret.append(color == PieceColor.WHITE ? "Q" : "q");
					canCastleSet = true;
				}
			}
		}
		if (!canCastleSet) ret.append("-");

		//Construct passant target string
		boolean passantSet = false;
		ret.append(" ");
		for (Piece piece : getAllPieces()) {
			if (piece instanceof PassantTarget) {
				ret.append(piece.getLocation());
				passantSet = true;
			}
		}
		if (!passantSet) ret.append("-");

		//Set the half clock (the moves since last cap)
		ret.append(" ").append(getMovesSinceCap());

		//Set the turn indicator, adding 1 because fen starts at 1 and we start at 0.
		ret.append(" ").append(getCounter() / 2 + 1);

		return ret.toString();
	}

	public void clearBoard() {
		this.squares = new Piece[8][8];
	}

	public void clearPassant(PieceColor color) {
		getAllPieces(color).stream().filter(piece -> piece instanceof PassantTarget).forEach(piece -> setSquare(piece.getLocation(), null));
	}

	public boolean placePiece(Piece p, Location l) {
		if (getSquare(l) != null) return false;
		setSquare(l, p);
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
		// check whether we're moving through a piece
		if (!pathIsEmpty(p.getLocation(), dest))
			return false;

		// check whether we're capturing
		Piece victim = getSquare(dest);
		if (victim != null) {
			if (!p.validateCapture(dest) && !isGhostBoard) {
				if (victim instanceof Rook && p instanceof King && p.getColor() == victim.getColor())
					ChessMasters.controller.setStatus("That is an invalid castle.");
				else
					ChessMasters.controller.setStatus("You can't capture that piece.");
				return false;
			}
			// if we are, ensure that we're capturing an opponent
			if (victim.getColor() == p.getColor())
				return (p instanceof King && victim instanceof Rook && p.getNumMoves() == 0 && victim.getNumMoves() == 0);
			// if we're trying to capture a king, something has gone horribly wrong---we
			// shouldn't have been able to reach this configuration in the first place
			if (victim instanceof King)
				throw new UnsupportedOperationException
					("An attempt (" + p.getLocation() + " -> " + dest + ") was made to capture a king, indicating that the game was in an illegal state:\n" + this.toString());
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

	// Returns whether the given color is in checkmate.
	public boolean isInCheckmate(PieceColor color) {
		return isInCheckmate(getKing(color));
	}

	public boolean isInCheckmate(King king) {
		if (!isInCheck(king)) return false;
		for (int rank_from = 0; rank_from < 8; rank_from++) {
			for (int file_from = 0; file_from < 8; file_from++) {

				Location from = new Location(file_from, rank_from);
				Piece p = getSquare(from);

				if (p == null || p.getColor() != king.getColor()) continue;

				for (int rank_to = 0; rank_to < 8; rank_to++) {
					for (int file_to = 0; file_to < 8; file_to++) {

						Location to = new Location(file_to, rank_to);
						if (from.equals(to)) continue;

						Board b = new Board(this, true);
						try {
							if (b.movePiece(from, to) && !b.isInCheck(king.getColor()))
								// we found a move that's valid and removes us from check
								return false; // so we're not in checkmate
						} catch (UnsupportedOperationException uoe) {
							continue;
						}
					}
				}
			}
		}
		return true;
	}

	public boolean checkStalemate(PieceColor color) {
		return getAllPieces(color).stream().noneMatch(this::canPieceMove);
	}

	public boolean canPieceMove(Piece p) {
		if (p == null) return false;
		Location from = p.getLocation();

		for (int rank_to = 0; rank_to < 8; rank_to++) {
			for (int file_to = 0; file_to < 8; file_to++) {

				Location to = new Location(file_to, rank_to);
				if (from.equals(to)) continue;

				Board b = new Board(this, true);
				try {
					if (b.movePiece(from, to) && !b.isInCheck(p.getColor()))
						// we found a move that's valid and removes us from check
						return true; // so we're not in checkmate
				} catch (UnsupportedOperationException uoe) {
					continue;
				}
			}
		}
		return false;
	}

	public boolean contains(ArrayList<Piece> arr, Class<? extends Piece> type) {
		return arr.stream().anyMatch(type::isInstance);
	}

	public boolean isDeadPosition() {
		ArrayList<Piece> all = getAllPieces();
		if (all.size() > 4) return false;

		//Possible combinations are
		//K v k
		//K v k n
		//K v k b
		//K B v k b

		if (all.size() == 2 // K v k
				|| (contains(all, Knight.class) && all.size() == 3)) return true; // K v k n OR K N vs k
		else if (contains(all, Bishop.class)) {
			if (all.size() == 3) return true; // K v k b OR K B v k
			else if (all.size() == 4) {
				ArrayList<Piece> white = getAllPieces(PieceColor.WHITE);
				ArrayList<Piece> black = getAllPieces(PieceColor.BLACK);
				if (white.size() == 3 || black.size() == 3) return false;

				Bishop wB = white.stream().filter(p -> p instanceof Bishop).findFirst().map(p -> (Bishop) p).orElse(null);
				Bishop bb = black.stream().filter(p -> p instanceof Bishop).findFirst().map(p -> (Bishop) p).orElse(null);
				if (wB == null || bb == null)
					return false; //In this case, we have king and something vs king and bishop. In this case, checkmate is still potentially possible.

				PieceColor wBColor = wB.getLocation().getX() % 2 == 0 ? PieceColor.BLACK : PieceColor.WHITE;
				if (wB.getLocation().getY() % 2 == 1) wBColor = wBColor.getOpposite();

				PieceColor bbColor = bb.getLocation().getX() % 2 == 0 ? PieceColor.BLACK : PieceColor.WHITE;
				if (bb.getLocation().getY() % 2 == 1) bbColor = bbColor.getOpposite();

				return wBColor == bbColor;
			}
		}
		return false;
	}

	public boolean movePiece(String from, String to) {
		return movePiece(new Location(from), new Location(to));
	}

	public boolean movePiece(Move move) {
		return movePiece(move.from, move.to);
	}

	public boolean movePiece(Location from, Location to) {
		Piece p = getSquare(from);
		Piece target = getSquare(to);
		if (p == null) {
			ChessMasters.controller.setStatus("The source square (" + from.toString() + ") is empty");
			return false;
		}
		if (!validateMove(p, to)) return false;
		if (isCastle(new Move(from, to))) {
			boolean castled = castle((King) p, (Rook) target);

			if (castled) {
				ChessMasters.controller.setStatus(p.getColor().name() + " performed a " + (target.getLocation().getX() < p.getLocation().getX() ? "king-side" : "queen-side") + " castle.");
			}

			if (!this.isGhostBoard) {
				PostPieceMoveEvent post = new PostPieceMoveEvent(p, target, this);
				EventRegistry.callEvents(post);
			}

			return castled;
//			ChessMasters.controller.setStatus("Castling has not been fully implemented! Hold tight!");
//			return false;
		} else {
			Piece victim = getSquare(to);
			PieceCaptureEvent capture = new PieceCaptureEvent(p, victim, this); //Fire our capture event when a piece is captured.
			if (!p.move(to, this.isGhostBoard)) return false;
			if (p instanceof Pawn) checkPassant((Pawn) p, from);
			setSquare(to, p);
			setSquare(from, null);

			boolean passant = false;
			if (victim != null) {
				passant = p instanceof Pawn && victim instanceof PassantTarget;
				if (passant && from.getX() != p.getLocation().getX()) {//The location has been updated to the 'to' location
					setSquare(((PassantTarget) victim).getOwner().getLocation(), null);
				}
			}

			setSquare(to, p);
			setSquare(from, null);

			if (!isGhostBoard && p instanceof Pawn && ((Pawn) p).shouldPromote()) { // Promote pawn to queen.
				String choiceInput = "";
				boolean validInput = false;
				do {
					try {
						choiceInput = IOUtils.promptForString("What would you like to promote your piece to (Queen, Knight, Rook, or Bishop)?: ").toLowerCase();
					} catch (EOFException e) {
						e.printStackTrace();
					}

					choiceInput.toLowerCase();
					switch(choiceInput) {
						case "queen" :
							p = new Queen(p.getColor());
							setSquare(to, p);
							validInput = true;
							break;
						case "knight" :
							p = new Knight(p.getColor());
							setSquare(to, p);
							validInput = true;
							break;
						case "rook" :
							p = new Rook(p.getColor());
							setSquare(to, p);
							validInput = true;
							break;
						case "bishop" :
							p = new Bishop(p.getColor());
							setSquare(to, p);
							validInput = true;
							break;
						default:
							System.out.println("Invalid piece.");
							break;
					}
				} while(!validInput);
			}

			if (!this.isGhostBoard) {
				PostPieceMoveEvent post = new PostPieceMoveEvent(p, this);
				EventRegistry.callEvents(post);
				if (victim != null) {
					capture.setPassant(passant);
					EventRegistry.callEvents(capture);
				}
			}
		}

		// if we made it this far, the move is ok
		moves.add(new Move(from, to));
		return true;
	}

	private boolean isCastle(Move move) {
		Piece piece = getSquare(move.from);
		Piece target = getSquare(move.to);
		if (piece == null || target == null)
			return false;

		if (piece instanceof King) {
			if (target != null && target instanceof Rook
					&& target.getColor() == piece.getColor()) {
				return piece.getNumMoves() == 0 && target.getNumMoves() == 0;
			}
		}
		return false;
	}

	public boolean castle(King king, Rook rook) {
		if (king.getLocation().getY() != rook.getLocation().getY()) return false;
		int dx = rook.getLocation().getX() - king.getLocation().getX();
		if (dx < 0)
			dx += 2;
		else
			dx -= 1;

		Location kingInit = king.getLocation();
		Location rookInit = rook.getLocation();

		Location kingDest = new Location(king.getLocation().getX() + dx, king.getLocation().getY());
		Location rookDest = new Location(kingDest.getX() + (dx < 0 ? 1 : -1), kingDest.getY());

		if (!king.move(kingDest) || !rook.move(rookDest)) {
			king.setLocation(kingInit);
			rook.setLocation(rookInit);
			return false;
		}
		setSquare(kingDest, king);
		setSquare(rookDest, rook);
		setSquare(kingInit, null);
		setSquare(rookInit, null);
		return true;
	}

	/**
	 * Simply toggles a flag in the pawn if this is a move that causes the piece to be passantable.
	 *
	 * @param pawn The pawn to check
	 * @param dest The destination location
	 * @return Whether or not the pawn is now passantable.
	 */
	public boolean checkPassant(Pawn pawn, Location dest) {
		Location location = pawn.getLocation();
		int numMoves = pawn.getNumMoves();
		if (numMoves == 1 && Math.abs(location.getY() - dest.getY()) == 2) { //This is our first move (already executed) and we're moving 2 spaces
			int dy = location.getY() - dest.getY();
			boolean passantTarget = false;

			Location intercept = new Location(location.getX(), dest.getY() + (dy / 2));
			for (Piece piece : getAllPieces(pawn.getColor().getOpposite())) {
				if (!(piece instanceof Pawn))
					continue;

				if (piece.validateCapture(intercept)) {
					passantTarget = true;
				}
			}
			pawn.setPassantable(passantTarget);

			if (passantTarget) {
				this.setSquare(intercept, new PassantTarget(pawn));
				ChessMasters.controller.setStatus("Note: The '*' indicates that an en passant is possible. For more details, type 'help'");
			}
		} else if (pawn.isPassantable()) {
			pawn.setPassantable(false);
		}

		return pawn.isPassantable();
	}

	public boolean isInCheck(PieceColor color) {
		return isInCheck(getKing(color));
	}

	public boolean isInCheck(King king) {
		if (king == null) return false;
		for (Piece piece : getAllPieces(king.getColor().getOpposite())) {
			if (pieceCreatesCheck(piece, king)) {
				return true;
			}
		}

		return false;
	}

	public boolean pieceCreatesCheck(Piece piece) {
		King king = getKing(piece.getColor().getOpposite());
		return pieceCreatesCheck(piece, king);
	}

	public boolean pieceCreatesCheck(Piece piece, King king) {
		if (!piece.validateCapture(king.getLocation()))
			return false;
		try {
			validateMove(piece, king.getLocation());
		} catch (UnsupportedOperationException e) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return this.toString(PieceColor.WHITE, false);
	}

	public String toString(PieceColor side, boolean highlightLast) {
		// Build the top edge
		String top = Utils.buildRow(
				"   " + Utils.Drawing.Corners.topLeft(),
				Utils.Drawing.Edges.horizontal() + Utils.Drawing.Edges.horizontal() + Utils.Drawing.Edges.horizontal(),
				Utils.Drawing.Joints.horizontalDown(),
				Utils.Drawing.Corners.topRight(),
				8
				) + "\n";

		// Build the row separator
		String rowSep = Utils.buildRow(
				"   " + Utils.Drawing.Joints.verticalRight(),
				Utils.Drawing.Edges.horizontal() + Utils.Drawing.Edges.horizontal() + Utils.Drawing.Edges.horizontal(),
				Utils.Drawing.Joints.cross(),
				Utils.Drawing.Joints.verticalLeft(),
				8
				) + "\n";

		// Build the bottom edge
		String bottom = Utils.buildRow(
				"   " + Utils.Drawing.Corners.bottomLeft(),
				Utils.Drawing.Edges.horizontal() + Utils.Drawing.Edges.horizontal() + Utils.Drawing.Edges.horizontal(),
				Utils.Drawing.Joints.horizontalUp(),
				Utils.Drawing.Corners.bottomRight(),
				8
				) + "\n";

		// Assemble the board
		StringBuilder sb_out = new StringBuilder();

		String prefix = top;

		boolean squareColor = true; // white, starting at a8

		// Build each row
		for (int row = 7; row >= 0; row--) {
			Piece[] rank = squares[side == PieceColor.WHITE ? row : 7 - row];
			sb_out.append(prefix);

			sb_out.append(' ').append(side == PieceColor.WHITE ? row + 1 : 8 - row).append(' ');
			for (int col = 0; col < 8; col++) {
				Piece piece = rank[side == PieceColor.WHITE ? col : 7 - col];
				sb_out.append(Utils.Drawing.Edges.vertical());

				// highlight last move if applicable
				if (highlightLast && getMoves().size() > 0) {
					Location thisSquare = (side == PieceColor.WHITE ? new Location(col, row) : new Location(7-col, 7-row));
					Move lastMove = getMoves().get(getMoves().size()-1);
					if (thisSquare.equals(lastMove.from)) {
						sb_out.append(Utils.Styles.sourceSquare());
					} else if (thisSquare.equals(lastMove.to)) {
						sb_out.append(Utils.Styles.destSquare());
					} else {
						sb_out.append(squareColor ? Utils.Styles.lightSquare() : Utils.Styles.darkSquare());
					}
				} else {
					sb_out.append(squareColor ? Utils.Styles.lightSquare() : Utils.Styles.darkSquare());
				}

				sb_out
					.append(' ')
					.append(piece == null ? '-' : piece)
					.append(' ')
					.append(Utils.Styles.reset())
					;
				squareColor = !squareColor;
			}
			squareColor = !squareColor;

			sb_out
				.append(Utils.Drawing.Edges.vertical())
				.append('\n')
				;

			prefix = rowSep;
		}

		sb_out.append(bottom);
		if (side == PieceColor.WHITE)
			sb_out.append("     a   b   c   d   e   f   g   h  ");
		else
			sb_out.append("     h   g   f   e   d   c   b   a  ");

		return sb_out.toString();
	}
}
