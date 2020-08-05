package edu.neumont.chessmasters.models.pieces;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String move) {
        int dx = Math.abs(getX() - getX(move));
        int dy = Math.abs(getY() - Integer.valueOf(move.substring(1)));
        return dx == dy;
    }
}
