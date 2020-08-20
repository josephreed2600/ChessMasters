package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.pieces.Piece;

public class PieceCaptureEvent extends Event {

    private final Piece attacker, captured;
    private final Board   board;
    private       boolean isPassant = false;

    public PieceCaptureEvent(Piece attacker, Piece captured, Board board) {
        this.attacker = attacker;
        this.captured = captured;
        this.board = board;
    }

    public boolean isPassant() {
        return isPassant;
    }

    public void setPassant(boolean passant) {
        isPassant = passant;
    }

    public Piece getAttacker() {
        return attacker;
    }

    public Piece getCaptured() {
        return captured;
    }

    public Board getBoard() {
        return board;
    }
}
