package edu.neumont.chessmasters.models.pieces;

import edu.neumont.chessmasters.models.Location;

public class King extends Piece {
    public King(PieceColor color) {
        super(color, "k");
    }

    @Override
    public boolean validateMove(String move) {
        move = move.toLowerCase();
        int dx = Math.abs(getLocation().getX() - Location.getX(move));
        int dy = Math.abs(getLocation().getY() - Location.getY(move));
        return (dx <= 1 && dy <= 1) //We are moving one space
                || (numMoves == 0 && dy == 0 && (Location.getX(move) == 0 || Location.getX(move) == 7)); //Or we are attempting a castle
    }
}
