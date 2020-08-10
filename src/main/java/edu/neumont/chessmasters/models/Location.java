package edu.neumont.chessmasters.models;

public class Location {

    private int x = 0, y = 0;

    public Location(String coordinates) {
        coordinates = coordinates.toLowerCase();
        setX(getX(coordinates));
        setY(getY(coordinates));
    }

    public Location(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (x > 7 || x < 0)
            throw new IndexOutOfBoundsException("Cannot set piece's location beyond 7 or less than 0");
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (y > 7 || y < 0)
            throw new IndexOutOfBoundsException("Cannot set piece's location beyond 7 or less than 0");
        this.y = y;
    }

    public static int getY(String location) {
        location = location.toLowerCase();
        return Integer.valueOf(location.substring(1)) - 1;
    }

    public static int getX(String location) {
        location = location.toLowerCase();
        char c = location.charAt(0);
        return Integer.valueOf(c) - Integer.valueOf('a');
    }

		public static Location[] getExclusiveRange(Location a, Location b) {
			int ax = a.getX(), ay = a.getY(),
					bx = b.getX(), by = b.getY(),
					Dx = bx - ax , Dy = by - ay ;
			// if locations are adjacent or overlap
			if (-1 <= Dx && Dx <= 1 && -1 <= Dy && Dy <= 1)
				return new Location[0];
			// if we're not moving along a rank or a file, and we're not moving along a diagonal,
			// then we're doing something weird like a knight. return an empty set
			if (!(Dx == 0 ^ Dy == 0) && Math.abs(Dx) != Math.abs(Dy))
				return new Location[0];

			// vector components for iterating along the line
			int dx = (Dx == 0 ? 0 : Dx / Math.abs(Dx))
				, dy = (Dy == 0 ? 0 : Dy / Math.abs(Dy));

			Location[] out = new Location[Math.max(Math.abs(Dx), Math.abs(Dy)) - 1];

			int index = 0;
			while (ax+dx != bx || ay+dy != by) {
				ax += dx;
				ay += dy;
				out[index++] = new Location(ax, ay);
			}
			return out;
		}

		@Override
		public String toString() {
			int rank = 1 + getY();
			char file = (char)('a' + getX());
			return Character.toString(file) + Integer.toString(rank);
		}

		public boolean equals(Location other) {
			return this.getX() == other.getX() && this.getY() == other.getY();
		}

}
