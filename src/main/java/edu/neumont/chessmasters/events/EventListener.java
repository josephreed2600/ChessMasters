package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.annotations.EventHandler;

public class EventListener {

    @EventHandler
    public void move(PrePieceMoveEvent event) {
        System.out.println("Caught event! This is just a sample event.");
    }

    @EventHandler
    public void capture(PieceCaptureEvent event) {
        System.out.println("Piece is about to be captured!");
    }

}
