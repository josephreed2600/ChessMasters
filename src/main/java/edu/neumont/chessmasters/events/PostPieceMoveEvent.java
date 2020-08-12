package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.models.Location;
import edu.neumont.chessmasters.models.pieces.Piece;

public class PostPieceMoveEvent extends Event {

    private final Piece    piece;
    private final Location location;

    public PostPieceMoveEvent(Piece piece) {
        this.piece = piece;
        this.location = piece.getLocation();
    }

    public Piece getPiece() {
        return piece;
    }

    public Location getLocation() {
        return location;
    }

}
