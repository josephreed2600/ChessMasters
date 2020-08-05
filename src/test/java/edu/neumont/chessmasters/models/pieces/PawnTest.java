package edu.neumont.chessmasters.models.pieces;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class PawnTest {

    @Test
    void testPawnMovement() {
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
        assertFalse (wpawn.validateMove("c4"));
        System.out.println("Correct. Invalid move.\n");

        System.out.println("Testing white pawn moving from b2 to b5");
        assertFalse (wpawn.validateMove("b5"));
        System.out.println("Correct. Invalid move.\n");

        System.out.println("Testing white pawn moving from b2 to b1");
        assertFalse (wpawn.validateMove("b1"));
        System.out.println("Correct. Invalid move.\n");


        Pawn bpawn = new Pawn(PieceColor.BLACK);
        bpawn.setLocation("b7");
        System.out.println("Testing white pawn moving from b7 to b6");
        assert (bpawn.validateMove("b6"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing white pawn moving from b7 to b5");
        assert (bpawn.validateMove("b5"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing white pawn moving from b7 to c6");
        assert (bpawn.validateMove("c6"));
        System.out.println("Correct. Valid move.\n");

        System.out.println("Testing white pawn moving from b7 to c5");
        assertFalse (bpawn.validateMove("c5"));
        System.out.println("Correct. Invalid move.\n");

        System.out.println("Testing white pawn moving from b7 to b4");
        assertFalse (bpawn.validateMove("b4"));
        System.out.println("Correct. Invalid move.\n");

        System.out.println("Testing white pawn moving from b7 to b8");
        assertFalse (bpawn.validateMove("b8"));
        System.out.println("Correct. Invalid move.\n");
    }

}