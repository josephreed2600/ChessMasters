package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.ChessMasters;
import edu.neumont.chessmasters.annotations.EventHandler;
import edu.neumont.chessmasters.controllers.PlayerMove;
import edu.neumont.chessmasters.models.Board;
import edu.neumont.chessmasters.models.Location;
import edu.neumont.chessmasters.models.pieces.*;

public class EventListener {

    @EventHandler
    public void preMove(PrePieceMoveEvent event) {
        Board tempBoard = new Board(event.getBoard());
        King king = tempBoard.getKing(event.getPiece().getColor());
        boolean initialCheck = tempBoard.isInCheck(king);
        if (event.isCastle()) {
            if (initialCheck) {
                event.setCancelled(true);
                if (!event.isQuiet())
                    ChessMasters.controller.setStatus("You can't castle to get out of check.");
                return;
            } else {
                runCastleCheck(event, tempBoard, king);
            }
        } else {
//        if (!event.getBoard().isGhostBoard && initialCheck) {
            if (king != null && king.getLocation().equals(event.getPiece().getLocation())) {
                king.setLocation(event.getPassedLocation());
            }

            Piece target = tempBoard.getSquare(event.getPassedLocation());
            tempBoard.setSquare(event.getFrom(), null);
            tempBoard.setSquare(event.getPassedLocation(), event.getPiece().clone());

            if (target instanceof PassantTarget && event.getPiece() instanceof Pawn)
                tempBoard.setSquare(((PassantTarget) target).getOwner().getLocation(), null);
//        }

            if (tempBoard.isInCheck(king)) {
                if (initialCheck) {
                    if (!event.isQuiet())
                        ChessMasters.controller.setStatus("\nYour king would still be in check with that move. Try a different move.");
                } else if (!event.isQuiet())
                    ChessMasters.controller.setStatus("\nThat move would put your king in danger! Try a different move.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void move(PostPieceMoveEvent event) {
        String scoreBoard = ChessMasters.getScoreboard();
        
        if (event.getBoard().isGhostBoard)
            return;
        boolean check = PlayerMove.inst().getBoard().isInCheck(event.getPiece().getColor().getOpposite());//PlayerMove.inst().getBoard().pieceCreatesCheck(event.getPiece());
        String status = ChessMasters.controller.getStatus() == null ? "" : ChessMasters.controller.getStatus() + "\n";
        if (check) {
            ChessMasters.controller.setStatus(status + "CHECK");
        }
        boolean checkmate = PlayerMove.inst().getBoard().isInCheckmate(event.getPiece().getColor().getOpposite());
        if (check) {
            if (checkmate) {
                if(event.getPiece().getColor() == PieceColor.WHITE) {
                    ChessMasters.increaseWScore(1);
                } else if(event.getPiece().getColor() == PieceColor.BLACK) {
                    ChessMasters.increaseBScore(1);
                }       
                scoreBoard = ChessMasters.getScoreboard();
                ChessMasters.controller.setStatus(status + "CHECKMATE! " + scoreBoard);
                ChessMasters.controller.setGameOver();
            }
        }

        if (event.getPiece() instanceof Pawn)
            event.getBoard().setMovesSinceCap(0);
        else
            event.getBoard().incrMovesSinceCap();
    }

    @EventHandler
    public void capture(PieceCaptureEvent event) {
        Piece target = event.getCaptured();
        Piece attacker = event.getAttacker();
        String extra = "";
//        System.out.println(
        if (event.isPassant()) {
            target = ((PassantTarget) target).getOwner();
            extra = " by performing an En Passant!";
        }

        String status = ChessMasters.controller.getStatus() == null ? "" : "\n" + ChessMasters.controller.getStatus();
        if (!(target instanceof PassantTarget))
            ChessMasters.controller.setStatus("The " + target.getColor().toString().toLowerCase() + " " + target.getName().toLowerCase() +
                    " was captured by the " + attacker.getColor().toString().toLowerCase() + " " + attacker.getName().toLowerCase() + extra + status);
        event.getBoard().setMovesSinceCap(0);
    }

    private void runCastleCheck(PrePieceMoveEvent event, Board tempBoard, King king) {
        boolean queenside = event.getPassedLocation().getX() < king.getLocation().getX();
        int destX = event.getPassedLocation().getX();

        while (king.getLocation().getX() != destX) {
            tempBoard.setSquare(king.getLocation(), null);
            tempBoard.setSquare(new Location(queenside ? king.getLocation().getX() - 1 : king.getLocation().getX() + 1, king.getLocation().getY()), king);
            if (tempBoard.isInCheck(king)) {
                event.setCancelled(true);
                if (king.getLocation().getX() == destX)
                    ChessMasters.controller.setStatus("That move would put your king in check");
                else
                    ChessMasters.controller.setStatus("You king would pass over hostile territory if you performed that move.");
                break;
            }
        }
    }

}
