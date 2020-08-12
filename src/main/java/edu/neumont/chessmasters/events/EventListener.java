package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.annotations.EventHandler;
import edu.neumont.chessmasters.controllers.PlayerMove;
import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.pieces.King;
import edu.neumont.chessmasters.models.pieces.Piece;

public class EventListener {

    @EventHandler
    public void preMove(PrePieceMoveEvent event) {
        PlayerMove move = PlayerMove.inst();
        Board tempBoard = new Board(move.getBoard());
        King king = tempBoard.getKing(event.getPiece().getColor());
        if (tempBoard.isInCheck(king)) {

            tempBoard.setSquare(event.getFrom(), null);
            tempBoard.setSquare(event.getPassedLocation(), event.getPiece());
            if (tempBoard.isInCheck(king)) {
                System.out.println("\nYour king would still be in check with that move. Try moving another piece.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void move(PostPieceMoveEvent event) {
        boolean check = PlayerMove.inst().getBoard().pieceCreatesCheck(event.getPiece());
        if (check)
            System.out.println("\nCHECK!");
    }

    @EventHandler
    public void capture(PieceCaptureEvent event) {
        Piece target = event.getCaptured();
        Piece attacker = event.getAttacker();
        System.out.println("The " + target.getColor().toString().toLowerCase() + " " + target.getName().toLowerCase() +
                " was captured by the " + attacker.getColor().toString().toLowerCase() + " " + attacker.getName().toLowerCase());
    }

}
