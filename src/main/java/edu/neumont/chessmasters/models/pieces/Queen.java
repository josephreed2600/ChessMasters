package edu.neumont.chessmasters.models.pieces;

import edu.neumont.chessmasters.models.Location;

public class Queen extends Piece {
    public Queen(PieceColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String move) {
        move = move.toLowerCase();
        int dx = Math.abs(getLocation().getX() - Location.getX(move));
        int dy = Math.abs(getLocation().getY() - Location.getY(move));
        return dx == 0 || dy == 0 || dx == dy;
    }
}
