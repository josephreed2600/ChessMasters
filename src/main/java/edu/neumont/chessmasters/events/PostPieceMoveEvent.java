package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.Location;
import edu.neumont.chessmasters.models.pieces.Piece;

public class PostPieceMoveEvent extends Event {

    private final Piece    piece, secondary;
    private final Location location;
    private final Board    board;

    public PostPieceMoveEvent(Piece piece, Board board) {
        this.piece = piece;
        this.secondary = null;
        this.location = piece.getLocation();
        this.board = board;
    }

    public PostPieceMoveEvent(Piece piece, Piece secondary, Board board) {
        this.piece = piece;
        this.secondary = secondary;
        this.location = piece.getLocation();
        this.board = board;
    }

    public Piece getPiece() {
        return piece;
    }

    /**
     * Gets the secondary piece involved in the event, if any. This is really only applicable to Castling.
     * @return The secondary piece involved in the event.
     */
    public Piece getSecondary() {
        return secondary;
    }

    public Location getLocation() {
        return location;
    }

    public Board getBoard() {
        return board;
    }

}
