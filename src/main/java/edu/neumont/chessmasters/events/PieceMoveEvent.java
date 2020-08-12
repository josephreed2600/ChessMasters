package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.models.Location;
import edu.neumont.chessmasters.models.pieces.Piece;

public class PieceMoveEvent extends Event {

    private final Piece    piece;
    private final Location passedLocation;
    private       Location location;

    public PieceMoveEvent(Piece piece, Location location) {
        this.piece = piece;
        this.location = location;
        this.passedLocation = location;
    }

    public Piece getPiece() {
        return piece;
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

}
