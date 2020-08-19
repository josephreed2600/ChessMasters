package edu.neumont.chessmasters.models.pieces;

import edu.neumont.chessmasters.models.Location;

public class King extends Piece {
    private boolean isCastling = false;

    public King(PieceColor color) {
        super(color, "k");
    }

    public boolean isCastling() {
        return isCastling;
    }

    public void setCastling(boolean castling) {
        isCastling = castling;
    }

    @Override
    public boolean validateCapture(Location move) {
        int dx = Math.abs(getLocation().getX() - move.getX());
        int dy = Math.abs(getLocation().getY() - move.getY());
        boolean castle = (numMoves == 0 && dy == 0
                && (move.getX() == 0 || move.getX() == 7));
        setCastling(castle);
        return (dx <= 1 && dy <= 1) //We are moving one space
                || castle;
    }

    @Override
    public boolean validateMove(String move) {
        move = move.toLowerCase();
        int dx = Math.abs(getLocation().getX() - Location.getX(move));
        int dy = Math.abs(getLocation().getY() - Location.getY(move));
        boolean castle = isCastling && numMoves == 0 && dy == 0
                && (Location.getX(move) == 2 || Location.getX(move) == 6);
        boolean ret = (dx <= 1 && dy <= 1) //We are moving one space
                || castle; //Or we are attempting a castle
        return ret;
    }
}
