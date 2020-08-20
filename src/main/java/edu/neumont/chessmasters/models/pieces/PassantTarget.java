package edu.neumont.chessmasters.models.pieces;

public class PassantTarget extends Piece {

    private final Pawn owner;

    public PassantTarget(Pawn owner) {
        super(owner.getColor(), "*");
        this.owner = owner;
    }

    public Pawn getOwner() {
        return owner;
    }

    @Override
    public boolean validateMove(String move) {
        return false;
    }
}
