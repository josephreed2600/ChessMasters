package edu.neumont.chessmasters.models.pieces;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

class PieceMovementTests {

    private boolean test(Piece piece, String position) {
        System.out.println("Testing " + piece.getColor().toString() + " " + piece.getClass().getSimpleName() + " moving from " + piece.getLocation().toString() + " to " + position);
        return piece.move(position);
    }

    @Test
    void testPawnMovement() {
        System.out.println("\nTesting Pawn movement");
        Pawn wpawn = new Pawn(PieceColor.WHITE);
        wpawn.setLocation("b2");
        assert (test(wpawn, "b4"));
        System.out.println("Correct. Valid move.\n");

        assert (test(wpawn, "b5"));
        System.out.println("Correct. Valid move.\n");

        assert (test(wpawn, "c6"));
        System.out.println("Correct. Valid move.\n");

        assertFalse(test(wpawn, "c8"));
        System.out.println("Correct. Invalid move.\n");

        assertFalse(test(wpawn, "d8"));
        System.out.println("Correct. Invalid move.\n");

        wpawn.setLocation("b2");
        assertFalse(test(wpawn, "b5"));
        System.out.println("Correct. Invalid move.\n");

        assertFalse(test(wpawn, "b1"));
        System.out.println("Correct. Invalid move.\n");


        Pawn bpawn = new Pawn(PieceColor.BLACK);
        bpawn.setLocation("b7");
        assert (test(bpawn, "b5"));
        System.out.println("Correct. Valid move.\n");

        assert (test(bpawn, "b4"));
        System.out.println("Correct. Valid move.\n");

        assert (test(bpawn, "c3"));
        System.out.println("Correct. Valid move.\n");

        assertFalse(test(bpawn, "d1"));
        System.out.println("Correct. Invalid move.\n");

        bpawn.setLocation("b7");
        assertFalse(test(bpawn, "b4"));
        System.out.println("Correct. Invalid move.\n");

        assertFalse(test(bpawn, "b8"));
        System.out.println("Correct. Invalid move.\n");
    }

    @Test
    void pawnPromotion() {
        System.out.println("\nTesting pawn promotion");
        Pawn wPawn = new Pawn(PieceColor.WHITE);
        wPawn.setLocation("A8");
        assert(wPawn.shouldPromote());
        System.out.println("White pawn at A8 should be promoted.");
        wPawn.setLocation("A7");
        assertFalse(wPawn.shouldPromote());
        System.out.println("White pawn at A7 should correctly not be promoted.");
        wPawn.setLocation("A1");
        assertFalse(wPawn.shouldPromote());
        System.out.println("White pawn at A7 should correctly not be promoted.");

        Pawn bPawn = new Pawn(PieceColor.BLACK);
        bPawn.setLocation("A1");
        assert(bPawn.shouldPromote());
        System.out.println("Black pawn at A1 should be promoted.");
        bPawn.setLocation("A2");
        assertFalse(bPawn.shouldPromote());
        System.out.println("Black pawn at A2 should correctly not be promoted.");
        bPawn.setLocation("A8");
        assertFalse(bPawn.shouldPromote());
        System.out.println("Black pawn at A8 should correctly not be promoted.");

        System.out.println("TEST PASSED!");
    }

    @Test
    void kingTest() {
        King king = new King(PieceColor.WHITE);
        System.out.println("\nTesting that king can move in each of the 8 directions.");
        king.setLocation("d4");
        System.out.println("From d4 to d5");
        assert (test(king, "d5"));
        System.out.println("From d5 to e6");
        assert (test(king, "e6"));
        System.out.println("From e6 to f6");
        assert (test(king, "f6"));
        System.out.println("From f6 to g5");
        assert (test(king, "g5"));
        System.out.println("From g5 to g4");
        assert (test(king, "g4"));
        System.out.println("From g4 to f3");
        assert (test(king, "f3"));
        System.out.println("From f3 to e3");
        assert (test(king, "e3"));
        System.out.println("From e3 to d2");
        assert (test(king, "d2"));
        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");

        king.setLocation("d4");
        System.out.println("From d4 to d6");
        assertFalse(test(king, "d6"));
        System.out.println("From d4 to f6");
        assertFalse(test(king, "f6"));
        System.out.println("From d4 to d1");
        assertFalse(test(king, "d1"));

        System.out.println("Setting king to g8 and attempting to move off the board.");
        king.setLocation("h8");
        try {
            king.move("i8");
            fail("Moving off the board was allowed! (i8)");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Successfully failed");
        }

        try {
            king.move("h9");
            fail("Moving off the board was allowed! (h9)");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Successfully failed");
        }

        try {
            king.move("i9");
            fail("Moving off the board was allowed! (i9)");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Successfully failed");
        }

        System.out.println("All passed. King moves properly.");
    }

    @Test
    void queenTest() {
        Queen queen = new Queen(PieceColor.WHITE);
        System.out.println("\nTesting that queen can move in each of the 8 directions.");
        queen.setLocation("d4");
        System.out.println("From d4 to d5");
        assert (test(queen, "d5"));
        queen.setLocation("d4");
        System.out.println("From d4 to e5");
        assert (test(queen, "e5"));
        queen.setLocation("d4");
        System.out.println("From d4 to e4");
        assert (test(queen, "e4"));
        queen.setLocation("d4");
        System.out.println("From d4 to e3");
        assert (test(queen, "e3"));
        queen.setLocation("d4");
        System.out.println("From d4 to d3");
        assert (test(queen, "d3"));
        queen.setLocation("d4");
        System.out.println("From d4 to c3");
        assert (test(queen, "c3"));
        queen.setLocation("d4");
        System.out.println("From d4 to c4");
        assert (test(queen, "c4"));
        queen.setLocation("d4");
        System.out.println("From d4 to c5");
        assert (test(queen, "c5"));

        queen.setLocation("d4");
        System.out.println("From d4 to d7");
        assert (test(queen, "d7"));
        queen.setLocation("d4");
        System.out.println("From d4 to f6");
        assert (test(queen, "f6"));
        queen.setLocation("d4");
        System.out.println("From d4 to f4");
        assert (test(queen, "f4"));
        queen.setLocation("d4");
        System.out.println("From d4 to f2");
        assert (test(queen, "f2"));
        queen.setLocation("d4");
        System.out.println("From d4 to d2");
        assert (test(queen, "d2"));
        queen.setLocation("d4");
        System.out.println("From d4 to b2");
        assert (test(queen, "b2"));
        queen.setLocation("d4");
        System.out.println("From d4 to b4");
        assert (test(queen, "b4"));
        queen.setLocation("d4");
        System.out.println("From d4 to b6");
        assert (test(queen, "b6"));
        queen.setLocation("d4");
        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");

        System.out.println("From d4 to c6");
        assertFalse(test(queen, "c6"));
        System.out.println("From d4 to e6");
        assertFalse(test(queen, "e6"));
        System.out.println("From d4 to g8");
        assertFalse(test(queen, "g8"));
        System.out.println("All passed. Queen moves properly.");
    }

    @Test
    void bishopTest() {
        Bishop bishop = new Bishop(PieceColor.WHITE);
        System.out.println("\nTesting that bishop can move in each of the 4 diagonal directions.");
        bishop.setLocation("d4");
        assert (test(bishop, "e5"));
        bishop.setLocation("d4");
        assert (test(bishop, "e3"));
        bishop.setLocation("d4");
        assert (test(bishop, "c3"));
        bishop.setLocation("d4");
        assert (test(bishop, "c5"));

        bishop.setLocation("d4");
        assert (test(bishop, "f6"));
        bishop.setLocation("d4");
        assert (test(bishop, "f2"));
        bishop.setLocation("d4");
        assert (test(bishop, "b2"));
        bishop.setLocation("d4");
        assert (test(bishop, "b6"));
        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");

        bishop.setLocation("d4");
        assertFalse(test(bishop, "c6"));
        assertFalse(test(bishop, "e6"));
        assertFalse(test(bishop, "g8"));
        assertFalse(test(bishop, "d5"));
        assertFalse(test(bishop, "f4"));
        System.out.println("All passed. Bishop moves properly.");
    }

    @Test
    void rookTest() {
        Rook rook = new Rook(PieceColor.WHITE);
        System.out.println("\nTesting that rook can move in each of the 4 cardinal directions.");
        rook.setLocation("d4");
        assert (test(rook, "c4"));
        rook.setLocation("d4");
        assert (test(rook, "e4"));
        rook.setLocation("d4");
        assert (test(rook, "d5"));
        rook.setLocation("d4");
        assert (test(rook, "d3"));
        rook.setLocation("d4");
        assert (test(rook, "d8"));
        rook.setLocation("d4");
        assert (test(rook, "f4"));
        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");

        rook.setLocation("d4");
        assertFalse(test(rook, "e5"));
        assertFalse(test(rook, "e3"));
        assertFalse(test(rook, "c3"));
        assertFalse(test(rook, "c5"));
        System.out.println("All passed. Rook moves properly.");
    }

    @Test
    void knightTest() {
        Knight knight = new Knight(PieceColor.WHITE);
        System.out.println("\nTesting that knight can move in each of the 8 possible ways.");
        knight.setLocation("d4");
        assert (test(knight, "b5"));
        knight.setLocation("d4");
        assert (test(knight, "c6"));
        knight.setLocation("d4");
        assert (test(knight, "e6"));
        knight.setLocation("d4");
        assert (test(knight, "f5"));
        knight.setLocation("d4");
        assert (test(knight, "f3"));
        knight.setLocation("d4");
        assert (test(knight, "e2"));
        knight.setLocation("d4");
        assert (test(knight, "c2"));
        knight.setLocation("d4");
        assert (test(knight, "b3"));
        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");

        knight.setLocation("d4");
        assertFalse(test(knight, "e5"));
        assertFalse(test(knight, "e3"));
        assertFalse(test(knight, "c3"));
        assertFalse(test(knight, "c5"));
        assertFalse(test(knight, "c7"));
        assertFalse(test(knight, "a8"));
        assertFalse(test(knight, "a7"));
        System.out.println("All passed. Knight moves properly.");
    }

}