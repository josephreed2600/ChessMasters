package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.annotations.EventHandler;

public class EventListener {

    @EventHandler
    public void move(PieceMoveEvent event) {
        System.out.println("Caught event! This is just a sample event.");
    }

}
