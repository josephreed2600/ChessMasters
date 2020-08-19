package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.ChessMasters;
import edu.neumont.chessmasters.annotations.EventHandler;
import edu.neumont.chessmasters.controllers.PlayerMove;
import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.Location;
import edu.neumont.chessmasters.models.pieces.King;
import edu.neumont.chessmasters.models.pieces.Piece;
import edu.neumont.chessmasters.models.pieces.PieceColor;

public class EventListener {

    @EventHandler
    public void preMove(PrePieceMoveEvent event) {
        Board tempBoard = new Board(event.getBoard());
        King king = tempBoard.getKing(event.getPiece().getColor());
        boolean initialCheck = tempBoard.isInCheck(king);
        if (event.isCastle()) {
            if (initialCheck) {
                event.setCancelled(true);
                ChessMasters.controller.setStatus("You can't castle to get out of check.");
                return;
            } else {
                boolean queenside = event.getPassedLocation().getX() == 0;
                int destX = event.getPassedLocation().getX() + (queenside ? 1 : -1);

                while (king.getLocation().getX() != destX) {
                    tempBoard.setSquare(new Location(queenside ? king.getLocation().getX() - 1 : king.getLocation().getX() + 1, king.getLocation().getY()), king);
                    if (tempBoard.isInCheck(king)) {
                        event.setCancelled(true);
                        ChessMasters.controller.setStatus("You king would pass over hostile territory if you performed that move.");
                        break;
                    }
                }
            }
        } else {
//        if (!event.getBoard().isGhostBoard && initialCheck) {
            if (king.getLocation().equals(event.getPiece().getLocation())) {
                king.setLocation(event.getPassedLocation());
            }

            tempBoard.setSquare(event.getFrom(), null);
            tempBoard.setSquare(event.getPassedLocation(), event.getPiece().clone());
//        }

            if (tempBoard.isInCheck(king)) {
                if (initialCheck)
                    ChessMasters.controller.setStatus("\nYour king would still be in check with that move. Try moving another piece.");
                else
                    ChessMasters.controller.setStatus("\nThat move would put your king in danger! Try moving another piece.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void move(PostPieceMoveEvent event) {
        boolean check = PlayerMove.inst().getBoard().isInCheck(event.getPiece().getColor().getOpposite());//PlayerMove.inst().getBoard().pieceCreatesCheck(event.getPiece());
        if (check) {
            ChessMasters.controller.setStatus("\nCHECK");
        }
        boolean checkmate = PlayerMove.inst().getBoard().isInCheckmate(event.getPiece().getColor().getOpposite());
        if (check) {
            if (checkmate) {
                ChessMasters.controller.setStatus("\nCHECKMATE! " + (event.getPiece().getColor() == PieceColor.WHITE ? "1-0" : "0-1"));
                ChessMasters.controller.setGameOver();
            }
        }
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
