package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.Location;
import edu.neumont.chessmasters.models.pieces.Piece;

public class PrePieceMoveEvent extends Event {

    private final Piece    piece;
    private final Board    board;
    private final Location passedLocation;
    private       Location location;
    private       boolean  isCastle  = false; //Derived based on the piece passed in and the potential piece at the location
    private       boolean  cancelled = false;
    private       boolean  quiet     = false;

    public PrePieceMoveEvent(Piece piece, Location location, Board board, boolean quiet) {
        this(piece, location, board);
        this.quiet = quiet;
    }

    public PrePieceMoveEvent(Piece piece, Location location, Board board) {
        this.piece = piece;
        this.board = board;
        this.location = location;
        this.passedLocation = location;

//        if (piece instanceof King) {
//            Piece target = board.getSquare(location);
//            if (target != null && target instanceof Rook
//                    && target.getColor() == piece.getColor()) {
//                isCastle = piece.getNumMoves() == 0 && target.getNumMoves() == 0;
//            }
//        }
    }

    public Piece getPiece() {
        return piece;
    }

    public Board getBoard() {
        return board;
    }

    public Location getFrom() {
        return piece.getLocation();
    }

    public Location getLocation() {
        return location;
    }

    public Location getPassedLocation() {
        return passedLocation;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isCastle() {
        return isCastle;
    }

    public void setCastle(boolean castle) {
        isCastle = castle;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    /**
     * You can cancel the event and it won't go through.
     */
    public void cancel() {
        cancelled = true;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
