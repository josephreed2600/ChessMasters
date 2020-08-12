package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.models.Location;
import edu.neumont.chessmasters.models.pieces.Piece;

public class PrePieceMoveEvent extends Event {

    private final Piece    piece;
    private final Location passedLocation;
    private       Location location;
    private       boolean  cancelled = false;

    public PrePieceMoveEvent(Piece piece, Location location) {
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
