package edu.neumont.chessmasters.models.pieces;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class PieceTest {

    @Test
    void testPawnMovement() {
        System.out.println("\nTesting Pawn movement");
        Pawn wpawn = new Pawn(PieceColor.WHITE);
        wpawn.setLocation("b2");
        System.out.println("Testing white pawn moving from b2 to b3");
        assert (wpawn.validateMove("b3"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing white pawn moving from b2 to b4");
        assert (wpawn.validateMove("b4"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing white pawn moving from b2 to c3");
        assert (wpawn.validateMove("c3"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing white pawn moving from b2 to c4");
        assertFalse(wpawn.validateMove("c4"));
        System.out.println("Correct. Invalid move.\n");

        System.out.println("Testing white pawn moving from b2 to b5");
        assertFalse(wpawn.validateMove("b5"));
        System.out.println("Correct. Invalid move.\n");

        System.out.println("Testing white pawn moving from b2 to b1");
        assertFalse(wpawn.validateMove("b1"));
        System.out.println("Correct. Invalid move.\n");


        Pawn bpawn = new Pawn(PieceColor.BLACK);
        bpawn.setLocation("b7");
        System.out.println("Testing black pawn moving from b7 to b6");
        assert (bpawn.validateMove("b6"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing black pawn moving from b7 to b5");
        assert (bpawn.validateMove("b5"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing black pawn moving from b7 to c6");
        assert (bpawn.validateMove("c6"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing black pawn moving from b7 to c5");
        assertFalse(bpawn.validateMove("c5"));
        System.out.println("Correct. Invalid move.\n");

        System.out.println("Testing black pawn moving from b7 to b4");
        assertFalse(bpawn.validateMove("b4"));
        System.out.println("Correct. Invalid move.\n");

        System.out.println("Testing black pawn moving from b7 to b8");
        assertFalse(bpawn.validateMove("b8"));
        System.out.println("Correct. Invalid move.\n");
    }

    @Test
    void kingTest() {
        King king = new King(PieceColor.WHITE);
        System.out.println("\nTesting that king can move in each of the 8 directions.");
        king.setLocation("d4");
        System.out.println("From d4 to d5");
        assert (king.validateMove("d5"));
        System.out.println("From d4 to e5");
        assert (king.validateMove("e5"));
        System.out.println("From d4 to e4");
        assert (king.validateMove("e4"));
        System.out.println("From d4 to e3");
        assert (king.validateMove("e3"));
        System.out.println("From d4 to d3");
        assert (king.validateMove("d3"));
        System.out.println("From d4 to c3");
        assert (king.validateMove("c3"));
        System.out.println("From d4 to c4");
        assert (king.validateMove("c4"));
        System.out.println("From d4 to c5");
        assert (king.validateMove("c5"));
        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");

        System.out.println("From d4 to d6");
        assertFalse(king.validateMove("d6"));
        System.out.println("From d4 to f6");
        assertFalse(king.validateMove("f6"));
        System.out.println("From d4 to d1");
        assertFalse(king.validateMove("d1"));
        System.out.println("All passed. King moves properly.");
    }

    @Test
    void queenTest() {
        Queen queen = new Queen(PieceColor.WHITE);
        System.out.println("\nTesting that queen can move in each of the 8 directions.");
        queen.setLocation("d4");
        System.out.println("From d4 to d5");
        assert (queen.validateMove("d5"));
        System.out.println("From d4 to e5");
        assert (queen.validateMove("e5"));
        System.out.println("From d4 to e4");
        assert (queen.validateMove("e4"));
        System.out.println("From d4 to e3");
        assert (queen.validateMove("e3"));
        System.out.println("From d4 to d3");
        assert (queen.validateMove("d3"));
        System.out.println("From d4 to c3");
        assert (queen.validateMove("c3"));
        System.out.println("From d4 to c4");
        assert (queen.validateMove("c4"));
        System.out.println("From d4 to c5");
        assert (queen.validateMove("c5"));

        System.out.println("From d4 to d7");
        assert (queen.validateMove("d7"));
        System.out.println("From d4 to f6");
        assert (queen.validateMove("f6"));
        System.out.println("From d4 to f4");
        assert (queen.validateMove("f4"));
        System.out.println("From d4 to f2");
        assert (queen.validateMove("f2"));
        System.out.println("From d4 to d2");
        assert (queen.validateMove("d2"));
        System.out.println("From d4 to b2");
        assert (queen.validateMove("b2"));
        System.out.println("From d4 to b4");
        assert (queen.validateMove("b4"));
        System.out.println("From d4 to b6");
        assert (queen.validateMove("b6"));
        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");

        System.out.println("From d4 to c6");
        assertFalse(queen.validateMove("c6"));
        System.out.println("From d4 to e6");
        assertFalse(queen.validateMove("e6"));
        System.out.println("From d4 to g8");
        assertFalse(queen.validateMove("g8"));
        System.out.println("All passed. Queen moves properly.");
    }

    @Test
    void bishopTest() {
        Bishop bishop = new Bishop(PieceColor.WHITE);
        System.out.println("\nTesting that bishop can move in each of the 4 diagonal directions.");
        bishop.setLocation("d4");
        System.out.println("From d4 to e5");
        assert (bishop.validateMove("e5"));
        System.out.println("From d4 to e3");
        assert (bishop.validateMove("e3"));
        System.out.println("From d4 to c3");
        assert (bishop.validateMove("c3"));
        System.out.println("From d4 to c5");
        assert (bishop.validateMove("c5"));

        System.out.println("From d4 to f6");
        assert (bishop.validateMove("f6"));
        System.out.println("From d4 to f2");
        assert (bishop.validateMove("f2"));
        System.out.println("From d4 to b2");
        assert (bishop.validateMove("b2"));
        System.out.println("From d4 to b6");
        assert (bishop.validateMove("b6"));
        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");

        System.out.println("From d4 to c6");
        assertFalse(bishop.validateMove("c6"));
        System.out.println("From d4 to e6");
        assertFalse(bishop.validateMove("e6"));
        System.out.println("From d4 to g8");
        assertFalse(bishop.validateMove("g8"));
        System.out.println("From d4 to d5");
        assertFalse(bishop.validateMove("d5"));
        System.out.println("From d4 to f4");
        assertFalse(bishop.validateMove("f4"));
        System.out.println("All passed. Bishop moves properly.");
    }

    @Test
    void rookTest() {
        Rook rook = new Rook(PieceColor.WHITE);
        System.out.println("\nTesting that rook can move in each of the 4 cardinal directions.");
        rook.setLocation("d4");
        System.out.println("From d4 to c4");
        assert (rook.validateMove("c4"));
        System.out.println("Fromm d4 to e4");
        assert (rook.validateMove("e4"));
        System.out.println("From d4 to d5");
        assert (rook.validateMove("d5"));
        System.out.println("From d4 to d3");
        assert (rook.validateMove("d3"));
        System.out.println("From d4 to d8");
        assert (rook.validateMove("d8"));
        System.out.println("From d4 to f4");
        assert (rook.validateMove("f4"));
        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");

        System.out.println("From d4 to e5");
        assertFalse(rook.validateMove("e5"));
        System.out.println("From d4 to e3");
        assertFalse(rook.validateMove("e3"));
        System.out.println("From d4 to c3");
        assertFalse(rook.validateMove("c3"));
        System.out.println("From d4 to c5");
        assertFalse(rook.validateMove("c5"));
        System.out.println("All passed. Rook moves properly.");
    }

    @Test
    void knightTest() {
        Knight knight = new Knight(PieceColor.WHITE);
        System.out.println("\nTesting that knight can move in each of the 8 possible ways.");
        knight.setLocation("d4");
        System.out.println("From d4 to b5");
        assert (knight.validateMove("b5"));
        System.out.println("From d4 to c6");
        assert (knight.validateMove("c6"));
        System.out.println("From d4 to e6");
        assert (knight.validateMove("e6"));
        System.out.println("From d4 to f5");
        assert (knight.validateMove("f5"));
        System.out.println("From d4 to f3");
        assert (knight.validateMove("f3"));
        System.out.println("From d4 to e2");
        assert (knight.validateMove("e2"));
        System.out.println("From d4 to c2");
        assert (knight.validateMove("c2"));
        System.out.println("From d4 to b3");
        assert (knight.validateMove("b3"));
        System.out.println("All valid movement tests passed. Testing a few invalid moves now.\n");

        System.out.println("From d4 to e5");
        assertFalse(knight.validateMove("e5"));
        System.out.println("From d4 to e3");
        assertFalse(knight.validateMove("e3"));
        System.out.println("From d4 to c3");
        assertFalse(knight.validateMove("c3"));
        System.out.println("From d4 to c5");
        assertFalse(knight.validateMove("c5"));
        System.out.println("From d4 to c7");
        assertFalse(knight.validateMove("c7"));
        System.out.println("From d4 to a8");
        assertFalse(knight.validateMove("a8"));
        System.out.println("From d4 to a7");
        assertFalse(knight.validateMove("a7"));
        System.out.println("All passed. Knight moves properly.");
    }

}