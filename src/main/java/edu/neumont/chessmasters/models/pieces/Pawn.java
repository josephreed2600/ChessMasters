package edu.neumont.chessmasters.models.pieces;

import edu.neumont.chessmasters.models.Location;

public class Pawn extends Piece {

    protected boolean isPassantable = false;

    public Pawn(PieceColor color) {
        super(color, "p");
    }

    public boolean shouldPromote() {
        return (color == PieceColor.WHITE && getLocation().getY() == 7) ||
                (color == PieceColor.BLACK && getLocation().getY() == 0);
    }

    public boolean isPassantable() {
        return isPassantable;
    }

    public void setPassantable(boolean passantable) {
        System.out.println(location + ": I'm passantable? " + passantable);
        isPassantable = passantable;
    }

    @Override
    public boolean validateMove(String move) {
        move = move.toLowerCase();
        boolean canMove = false;

        int y = getLocation().getY();
        int newY = Location.getY(move);
        if ((getColor() == PieceColor.WHITE && newY > y) ||
                (getColor() == PieceColor.BLACK && newY < y)) {
            int dx = Math.abs(getLocation().getX() - Location.getX(move));
            int dy = Math.abs(y - newY);
            canMove = (dx <= 1 && dy <= 1) || (numMoves == 0 && dx == 0 && dy <= 2);
        }

        return canMove;
    }

    @Override
    public boolean validateCapture(Location location) {
        boolean canMove = false;

        int y = getLocation().getY();
        int newY = location.getY();
        if ((getColor() == PieceColor.WHITE && newY > y) ||
                (getColor() == PieceColor.BLACK && newY < y)) {
            int dx = Math.abs(getLocation().getX() - location.getX());
            int dy = Math.abs(y - newY);
            canMove = dx == 1 && dy == 1;
        }

        return canMove;
    }
}
