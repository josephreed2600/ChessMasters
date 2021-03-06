package edu.neumont.chessmasters.models.pieces;

import edu.neumont.chessmasters.models.Location;

public class Bishop extends Piece {
    public Bishop(PieceColor color) {
        super(color, "b");
    }

    @Override
    public boolean validateMove(String move) {
        move = move.toLowerCase();
        int dx = Math.abs(getLocation().getX() - Location.getX(move));
        int dy = Math.abs(getLocation().getY() - Location.getY(move));
        return dx == dy;
    }
}
