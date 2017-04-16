package com.github.bric3.mower;

import java.util.Objects;

import static com.github.bric3.mower.Coordinates.coordinates;

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

    public static Lawn lawn(int upperXCoordinate,
                            int upperYCoordinate) {
        return new Lawn(coordinates(upperXCoordinate, upperYCoordinate));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lawn lawn = (Lawn) o;
        return Objects.equals(upperRightCoordinate, lawn.upperRightCoordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upperRightCoordinate);
    }

    @Override
    public String toString() {
        return "Lawn{" +
               "upperRightCoordinate=" + upperRightCoordinate +
               '}';
    }
}
