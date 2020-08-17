package edu.neumont.chessmasters.models;

import edu.neumont.chessmasters.models.Location;

public class Move {

		public final Location from, to;

		public Move(Location from, Location to) {
			this.from = from;
			this.to = to;
		}

		public Move(String from, String to) {
			this(new Location(from), new Location(to));
		}

}
