package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.ChessMasters;
import edu.neumont.chessmasters.annotations.EventHandler;
import edu.neumont.chessmasters.controllers.PlayerMove;
import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.pieces.King;
import edu.neumont.chessmasters.models.pieces.Piece;
import edu.neumont.chessmasters.models.pieces.PieceColor;

public class EventListener {

    @EventHandler
    public void preMove(PrePieceMoveEvent event) {
        PlayerMove move = PlayerMove.inst();
        Board tempBoard = new Board(move.getBoard());
        King king = tempBoard.getKing(event.getPiece().getColor());
        if (!move.getBoard().isGhostBoard && tempBoard.isInCheck(king)) {

            if (king.getLocation().equals(event.getPiece().getLocation())) {
                king.setLocation(event.getPassedLocation());
            }
            tempBoard.setSquare(event.getFrom(), null);
            tempBoard.setSquare(event.getPassedLocation(), event.getPiece());
            if (tempBoard.isInCheck(king)) {
                ChessMasters.controller.setStatus("\nYour king would still be in check with that move. Try moving another piece.");
//                System.out.println("\nYour king would still be in check with that move. Try moving another piece.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void move(PostPieceMoveEvent event) {
        boolean check = PlayerMove.inst().getBoard().pieceCreatesCheck(event.getPiece());
        if (check)
            ChessMasters.controller.setStatus("\nCHECK");
//            System.out.println("\nCHECK!");
        boolean checkmate = PlayerMove.inst().getBoard().isInCheckmate(event.getPiece().getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE);
        if (checkmate) {
            ChessMasters.controller.setStatus("\nCHECKMATE! " + (event.getPiece().getColor() == PieceColor.WHITE ? "1-0" : "0-1"));
            ChessMasters.controller.setGameOver();
        }
//            System.out.println("\nCHECKMATE! " + (event.getPiece().getColor() == PieceColor.WHITE ? "1-0" : "0-1"));
    }

    @EventHandler
    public void capture(PieceCaptureEvent event) {
        Piece target = event.getCaptured();
        Piece attacker = event.getAttacker();
//        System.out.println(
        ChessMasters.controller.setStatus("The " + target.getColor().toString().toLowerCase() + " " + target.getName().toLowerCase() +
                " was captured by the " + attacker.getColor().toString().toLowerCase() + " " + attacker.getName().toLowerCase());
    }

}
