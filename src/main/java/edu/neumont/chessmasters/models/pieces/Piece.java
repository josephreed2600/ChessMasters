package edu.neumont.chessmasters.models.pieces;

import edu.neumont.chessmasters.ChessMasters;
import edu.neumont.chessmasters.Utils;
import edu.neumont.chessmasters.controllers.PlayerMove;
import edu.neumont.chessmasters.events.EventRegistry;
import edu.neumont.chessmasters.events.PrePieceMoveEvent;
import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.Location;

public abstract class Piece {

    protected final PieceColor color;
    protected final String     notation;

    protected int      numMoves = 0;
    protected Location location;

    public Piece(PieceColor color, String notation) {
        this.color = color;
        this.notation = notation;
    }

    public Piece(PieceColor color) {
        this(color, "?");
    }

    public static Piece fromFEN(String type) {
        switch (type) {
            case "P":
                return new Pawn(PieceColor.WHITE);
            case "p":
                return new Pawn(PieceColor.BLACK);

            case "R":
                return new Rook(PieceColor.WHITE);
            case "r":
                return new Rook(PieceColor.BLACK);

            case "N":
                return new Knight(PieceColor.WHITE);
            case "n":
                return new Knight(PieceColor.BLACK);

            case "B":
                return new Bishop(PieceColor.WHITE);
            case "b":
                return new Bishop(PieceColor.BLACK);

            case "Q":
                return new Queen(PieceColor.WHITE);
            case "q":
                return new Queen(PieceColor.BLACK);

            case "K":
                return new King(PieceColor.WHITE);
            case "k":
                return new King(PieceColor.BLACK);

            default:
                throw new UnsupportedOperationException("Unrecognized piece type: " + type);
        }
    }

    public String getName() {
        return this.getClass().getSimpleName();
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocation(String location) {
        setLocation(new Location(location));
    }

    /**
     * Attempts to move the piece to the given location. Returns false if the move is illegal.<br/>
     * If the piece's location has not yet been set, this will behave the same as {@link #setLocation(String)}
     * <br/>
     * <br/>
     * Please note, this does not check if another piece exists at that location already.
     *
     * @param location The coordinate location of the position to move to (ie 'a2')
     * @return boolean - Whether or not the move has been made.
     */
    public boolean move(String location) {
        return move(location, false);
    }

    public boolean move(String location, boolean quiet) {
        //Validate the passed in location
        if (this.getLocation() != null && !validateMove(location)) {
            if (!quiet)
                ChessMasters.controller.setStatus("This piece can't move in this way.");
            return false;
        }

        PrePieceMoveEvent event = new PrePieceMoveEvent(this, new Location(location), !quiet ? PlayerMove.inst().getBoard() : new Board(PlayerMove.inst().getBoard()));
        if (this instanceof King) { //Set our castle event
            event.setCastle(numMoves == 0 && Location.getY(location) == getLocation().getY()
                    && (Location.getX(location) == 2 || Location.getX(location) == 6));
        }
        if (!quiet) {
            //We have to make sure to CALL our events
            EventRegistry.callEvents(event);
        }

        if (event.isCancelled())
            return false;

        location = event.getLocation().toString();

        //Validate the potential new location
        if (this.getLocation() != null && !validateMove(location))
            return false;

        this.location = new Location(location);
        if (this instanceof King)
            ((King) this).setCastling(false);
        numMoves++;
        return true;
    }

    public boolean move(Location location) {
        return move(location, false);
    }

    public boolean move(Location location, boolean quiet) {
        return move(location.toString(), quiet);
    }

    public Piece clone() {
        Piece piece = Piece.fromFEN(getNotation());

        piece.setLocation(this.getLocation());
        piece.setNumMoves(this.getNumMoves());

        return piece;
    }

    public boolean validateMove(Location location) {
        return validateMove(location.toString());
    }

    public abstract boolean validateMove(String move);

    /**
     * Validates that a capture is legal.
     * This is really most applicable to Pawns as they can't capture where they indeed might be able to move.
     *
     * @param location
     * @return
     */
    public boolean validateCapture(Location location) {
        return validateMove(location);
    }

    public String getNotation() {
        return getColor() == PieceColor.WHITE
                ? this.notation.toUpperCase()
                : this.notation.toLowerCase();
    }

    @Override
    public String toString() {
        return getColor() == PieceColor.WHITE
                ? Utils.Styles.lightPiece + this.notation.toUpperCase() + Utils.Styles.afterPiece
                : Utils.Styles.darkPiece + this.notation.toLowerCase() + Utils.Styles.afterPiece
                ;
    }

}
