package edu.neumont.chessmasters.models.pieces;

import edu.neumont.chessmasters.models.Location;

public class Knight extends Piece {
    public Knight(PieceColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String move) {
        int dx = Math.abs(getLocation().getX() - Location.getX(move));
        int dy = Math.abs(getLocation().getY() - Location.getY(move));
        return (dx == 1 && dy == 2) || (dy == 1 && dx == 2);
    }
}
