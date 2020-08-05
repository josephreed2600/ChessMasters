package edu.neumont.chessmasters.models.pieces;

public abstract class Piece {

    protected final PieceColor color;

    protected int numMoves = 0;
    protected String location;

    public Piece(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public void setNumMoves(int numMoves) {
        this.numMoves = numMoves;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getY() {
        return Integer.parseInt(location.substring(1));
    }

    public int getX() {
        char c = location.charAt(0);
        return Integer.valueOf(c) - Integer.valueOf('a') + 1;
    }

    public static int getX(String location) {
        char c = location.charAt(0);
        return Integer.valueOf(c) - Integer.valueOf('a') + 1;
    }

    public abstract boolean validateMove(String move);

}
