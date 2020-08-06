package edu.neumont.chessmasters.models;

public class Location {

    private int x = 0, y = 0;

    public Location(String coordinates) {
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
        if(x > 7 || x < 0)
            throw new IndexOutOfBoundsException("Cannot set piece's location beyond 7 or less than 0");
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if(y > 7 || y < 0)
            throw new IndexOutOfBoundsException("Cannot set piece's location beyond 7 or less than 0");
        this.y = y;
    }

    public static int getY(String location) {
        return Integer.valueOf(location.substring(1)) - 1;
    }

    public static int getX(String location) {
        char c = location.charAt(0);
        return Integer.valueOf(c) - Integer.valueOf('a');
    }

}
