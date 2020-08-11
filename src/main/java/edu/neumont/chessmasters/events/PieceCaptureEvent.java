package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.models.pieces.Piece;

public class PieceCaptureEvent extends Event {

    private final Piece attacker, captured;

    public PieceCaptureEvent(Piece attacker, Piece captured) {
        this.attacker = attacker;
        this.captured = captured;
    }

    public Piece getAttacker() {
        return attacker;
    }

    public Piece getCaptured() {
        return captured;
    }
}
