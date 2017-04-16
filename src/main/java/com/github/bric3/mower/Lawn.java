package com.github.bric3.mower;

public class Lawn {
    public final Coordinates upperRightCoordinate;

    public Lawn(Coordinates upperRightCoordinate) {
        this.upperRightCoordinate = upperRightCoordinate;
    }


    public boolean allows(Coordinates coordinates) {
        boolean withinXAxis = 0 <= coordinates.x && coordinates.x <= upperRightCoordinate.x;
        boolean withinYAxis = 0 <= coordinates.y && coordinates.y <= upperRightCoordinate.y;
        return withinXAxis && withinYAxis;
    }

    public String sizeAsString() {
        return upperRightCoordinate.x + "x" + upperRightCoordinate.y;
    }
}
