package edu.neumont.chessmasters.models.pieces;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

class PieceMovementTests {

    @Test
    void testPawnMovement() {
        System.out.println("\nTesting Pawn movement");
        Pawn wpawn = new Pawn(PieceColor.WHITE);
        wpawn.setLocation("b2");
        System.out.println("Testing white pawn moving from b2 to b4");
        assert (wpawn.move("b4"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing white pawn moving from b4 to b5");
        assert (wpawn.move("b5"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing white pawn moving from b5 to c6");
        assert (wpawn.move("c6"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Assert we can't move 2 forward now that we've already made a move");
        assertFalse (wpawn.move("c8"));
        System.out.println("Correct. Invalid move.\n");

        System.out.println("Testing white pawn moving from c6 to d8");
        assertFalse(wpawn.move("d8"));
        System.out.println("Correct. Invalid move.\n");

        wpawn.setLocation("b2");
        System.out.println("Testing white pawn moving from b2 to b5");
        assertFalse(wpawn.move("b5"));
        System.out.println("Correct. Invalid move.\n");

        System.out.println("Testing white pawn moving from b2 to b1");
        assertFalse(wpawn.move("b1"));
        System.out.println("Correct. Invalid move.\n");


        Pawn bpawn = new Pawn(PieceColor.BLACK);
        bpawn.setLocation("b7");
        System.out.println("Testing black pawn moving from b7 to b5");
        assert (bpawn.move("b5"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing black pawn moving from b5 to b4");
        assert (bpawn.move("b4"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing black pawn moving from b4 to c3");
        assert (bpawn.move("c3"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing black pawn moving from c3 to d1");
        assertFalse(bpawn.move("d1"));
        System.out.println("Correct. Invalid move.\n");

        bpawn.setLocation("b7");
        System.out.println("Testing black pawn moving from b7 to b4");
        assertFalse(bpawn.move("b4"));
        System.out.println("Correct. Invalid move.\n");

        System.out.println("Testing black pawn moving from b7 to b8");
        assertFalse(bpawn.move("b8"));
        System.out.println("Correct. Invalid move.\n");
    }

    @Test
    void kingTest() {
        King king = new King(PieceColor.WHITE);
        System.out.println("\nTesting that king can move in each of the 8 directions.");
        king.setLocation("d4");
        System.out.println("From d4 to d5");
        assert (king.move("d5"));
        System.out.println("From d5 to e6");
        assert (king.move("e6"));
        System.out.println("From e6 to f6");
        assert (king.move("f6"));
        System.out.println("From f6 to g5");
        assert (king.move("g5"));
        System.out.println("From g5 to g4");
        assert (king.move("g4"));
        System.out.println("From g4 to f3");
        assert (king.move("f3"));
        System.out.println("From f3 to e3");
        assert (king.move("e3"));
        System.out.println("From e3 to d2");
        assert (king.move("d2"));
        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");

        king.setLocation("d4");
        System.out.println("From d4 to d6");
        assertFalse(king.move("d6"));
        System.out.println("From d4 to f6");
        assertFalse(king.move("f6"));
        System.out.println("From d4 to d1");
        assertFalse(king.move("d1"));

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

//    @Test
//    void queenTest() {
//        Queen queen = new Queen(PieceColor.WHITE);
//        System.out.println("\nTesting that queen can move in each of the 8 directions.");
//        queen.setLocation("d4");
//        System.out.println("From d4 to d5");
//        assert (queen.move("d5"));
//        System.out.println("From d4 to e5");
//        assert (queen.move("e5"));
//        System.out.println("From d4 to e4");
//        assert (queen.move("e4"));
//        System.out.println("From d4 to e3");
//        assert (queen.move("e3"));
//        System.out.println("From d4 to d3");
//        assert (queen.move("d3"));
//        System.out.println("From d4 to c3");
//        assert (queen.move("c3"));
//        System.out.println("From d4 to c4");
//        assert (queen.move("c4"));
//        System.out.println("From d4 to c5");
//        assert (queen.move("c5"));
//
//        System.out.println("From d4 to d7");
//        assert (queen.move("d7"));
//        System.out.println("From d4 to f6");
//        assert (queen.move("f6"));
//        System.out.println("From d4 to f4");
//        assert (queen.move("f4"));
//        System.out.println("From d4 to f2");
//        assert (queen.move("f2"));
//        System.out.println("From d4 to d2");
//        assert (queen.move("d2"));
//        System.out.println("From d4 to b2");
//        assert (queen.move("b2"));
//        System.out.println("From d4 to b4");
//        assert (queen.move("b4"));
//        System.out.println("From d4 to b6");
//        assert (queen.move("b6"));
//        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");
//
//        System.out.println("From d4 to c6");
//        assertFalse(queen.move("c6"));
//        System.out.println("From d4 to e6");
//        assertFalse(queen.move("e6"));
//        System.out.println("From d4 to g8");
//        assertFalse(queen.move("g8"));
//        System.out.println("All passed. Queen moves properly.");
//    }
//
//    @Test
//    void bishopTest() {
//        Bishop bishop = new Bishop(PieceColor.WHITE);
//        System.out.println("\nTesting that bishop can move in each of the 4 diagonal directions.");
//        bishop.setLocation("d4");
//        System.out.println("From d4 to e5");
//        assert (bishop.move("e5"));
//        System.out.println("From d4 to e3");
//        assert (bishop.move("e3"));
//        System.out.println("From d4 to c3");
//        assert (bishop.move("c3"));
//        System.out.println("From d4 to c5");
//        assert (bishop.move("c5"));
//
//        System.out.println("From d4 to f6");
//        assert (bishop.move("f6"));
//        System.out.println("From d4 to f2");
//        assert (bishop.move("f2"));
//        System.out.println("From d4 to b2");
//        assert (bishop.move("b2"));
//        System.out.println("From d4 to b6");
//        assert (bishop.move("b6"));
//        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");
//
//        System.out.println("From d4 to c6");
//        assertFalse(bishop.move("c6"));
//        System.out.println("From d4 to e6");
//        assertFalse(bishop.move("e6"));
//        System.out.println("From d4 to g8");
//        assertFalse(bishop.move("g8"));
//        System.out.println("From d4 to d5");
//        assertFalse(bishop.move("d5"));
//        System.out.println("From d4 to f4");
//        assertFalse(bishop.move("f4"));
//        System.out.println("All passed. Bishop moves properly.");
//    }
//
//    @Test
//    void rookTest() {
//        Rook rook = new Rook(PieceColor.WHITE);
//        System.out.println("\nTesting that rook can move in each of the 4 cardinal directions.");
//        rook.setLocation("d4");
//        System.out.println("From d4 to c4");
//        assert (rook.move("c4"));
//        System.out.println("Fromm d4 to e4");
//        assert (rook.move("e4"));
//        System.out.println("From d4 to d5");
//        assert (rook.move("d5"));
//        System.out.println("From d4 to d3");
//        assert (rook.move("d3"));
//        System.out.println("From d4 to d8");
//        assert (rook.move("d8"));
//        System.out.println("From d4 to f4");
//        assert (rook.move("f4"));
//        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");
//
//        System.out.println("From d4 to e5");
//        assertFalse(rook.move("e5"));
//        System.out.println("From d4 to e3");
//        assertFalse(rook.move("e3"));
//        System.out.println("From d4 to c3");
//        assertFalse(rook.move("c3"));
//        System.out.println("From d4 to c5");
//        assertFalse(rook.move("c5"));
//        System.out.println("All passed. Rook moves properly.");
//    }
//
//    @Test
//    void knightTest() {
//        Knight knight = new Knight(PieceColor.WHITE);
//        System.out.println("\nTesting that knight can move in each of the 8 possible ways.");
//        knight.setLocation("d4");
//        System.out.println("From d4 to b5");
//        assert (knight.move("b5"));
//        System.out.println("From d4 to c6");
//        assert (knight.move("c6"));
//        System.out.println("From d4 to e6");
//        assert (knight.move("e6"));
//        System.out.println("From d4 to f5");
//        assert (knight.move("f5"));
//        System.out.println("From d4 to f3");
//        assert (knight.move("f3"));
//        System.out.println("From d4 to e2");
//        assert (knight.move("e2"));
//        System.out.println("From d4 to c2");
//        assert (knight.move("c2"));
//        System.out.println("From d4 to b3");
//        assert (knight.move("b3"));
//        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");
//
//        System.out.println("From d4 to e5");
//        assertFalse(knight.move("e5"));
//        System.out.println("From d4 to e3");
//        assertFalse(knight.move("e3"));
//        System.out.println("From d4 to c3");
//        assertFalse(knight.move("c3"));
//        System.out.println("From d4 to c5");
//        assertFalse(knight.move("c5"));
//        System.out.println("From d4 to c7");
//        assertFalse(knight.move("c7"));
//        System.out.println("From d4 to a8");
//        assertFalse(knight.move("a8"));
//        System.out.println("From d4 to a7");
//        assertFalse(knight.move("a7"));
//        System.out.println("All passed. Knight moves properly.");
//    }

}