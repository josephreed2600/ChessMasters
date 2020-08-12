package edu.neumont.chessmasters.models;

import edu.neumont.chessmasters.models.pieces.Knight;
import edu.neumont.chessmasters.models.pieces.Pawn;
import edu.neumont.chessmasters.models.pieces.PieceColor;
import edu.neumont.chessmasters.models.pieces.Queen;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class BoardTest {

    @Test
    void pathIsEmpty() {
        System.out.println("\n --[ Ensuring pathIsEmpty works correctly ]-- ");
        Board board = new Board();
        board.clearBoard();

        board.placePiece(new Pawn(PieceColor.WHITE), new Location("a4"));
        board.placePiece(new Queen(PieceColor.WHITE), new Location("a1"));

        assertFalse(board.pathIsEmpty(new Location("a1"), new Location("a6")));
        System.out.println("Cannot move queen from a1 to a6 because we have placed a pawn in the way.");

        assert (board.pathIsEmpty(new Location("a1"), new Location("f1")));
        System.out.println("We can move the queen to f1 however. That path is clear.");

        assert (board.pathIsEmpty(new Location("a1"), new Location("f6")));
        System.out.println("We can also move to f6 (diagonal) because there is no piece in the way.");

        board.placePiece(new Pawn(PieceColor.WHITE), new Location("b2"));
        assertFalse(board.pathIsEmpty(new Location("a1"), new Location("f6")));
        System.out.println("After placing a pawn in the way, we can no longer move the queen diagonally.");

        board.placePiece(new Pawn(PieceColor.WHITE), new Location("c1"));
        assertFalse(board.pathIsEmpty(new Location("a1"), new Location("f1")));
        System.out.println("After placing a pawn in the way, we can no longer move the queen to f1.");

        System.out.println("\n --[ Checking Knight can move wherever he pleases ]--");

        board.placePiece(new Knight(PieceColor.WHITE), new Location("b1"));
        board.placePiece(new Pawn(PieceColor.WHITE), new Location("a2"));
        board.placePiece(new Pawn(PieceColor.WHITE), new Location("c2"));
        assert (board.pathIsEmpty(new Location("b1"), new Location("a3")));
        assert (board.pathIsEmpty(new Location("b1"), new Location("c3")));
        assert (board.pathIsEmpty(new Location("b1"), new Location("d2")));
        System.out.println("Tests passed!");
    }
}