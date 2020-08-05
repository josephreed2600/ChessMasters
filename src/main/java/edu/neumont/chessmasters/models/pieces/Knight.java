package edu.neumont.chessmasters.models.pieces;

public class Knight extends Piece {
    public Knight(PieceColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String move) {
        int dx = Math.abs(getX() - getX(move));
        int dy = Math.abs(getY() - Integer.valueOf(move.substring(1)));
        return (dx == 1 && dy == 2) || (dy == 1 && dx == 2);
    }
}
