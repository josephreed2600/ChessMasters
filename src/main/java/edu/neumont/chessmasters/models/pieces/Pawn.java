package edu.neumont.chessmasters.models.pieces;

public class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(color);
    }

    @Override
    public boolean validateMove(String move) {
        boolean canMove = false;

        int y = getY();
        int newY = Integer.parseInt(move.substring(1));
        if ((getColor() == PieceColor.WHITE && newY > y) ||
                (getColor() == PieceColor.BLACK && newY < y)) {
            int dx = Math.abs(getX() - getX(move));
            int dy = Math.abs(y - newY);
            canMove = (dx <= 1 && dy <= 1) || (dx == 0 && dy <= 2);
        }

        return canMove;
    }

}
