package edu.neumont.chessmasters.models.pieces;

public enum PieceColor {
    WHITE,
    BLACK;

    public PieceColor getOpposite() {
        return ordinal() == 0 ? BLACK : WHITE;
    }
}
