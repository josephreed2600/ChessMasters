package edu.neumont.chessmasters.models.pieces;

public class Rook extends Piece {
    public Rook(PieceColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String move) {
        int dx = Math.abs(getX() - getX(move));
        int dy = Math.abs(getY() - Integer.valueOf(move.substring(1)));
        return (dx == 0 && dy > 0) || (dy == 0 && dx > 0);
    }
}
