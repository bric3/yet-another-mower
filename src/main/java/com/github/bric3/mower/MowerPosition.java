package com.github.bric3.mower;

import java.util.Objects;

public class MowerPosition {
    public final Coordinates coordinates;
    public final Orientation orientation;

    public MowerPosition(int x, int y, Orientation orientation) {
        this(new Coordinates(x, y), orientation);
    }

    public MowerPosition(Coordinates coordinates, Orientation orientation) {
        this.coordinates = Objects.requireNonNull(coordinates);
        this.orientation = Objects.requireNonNull(orientation);
    }

    public static MowerPosition mowerPosition(int x, int y, Orientation orientation) {
        return new MowerPosition(x, y, orientation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MowerPosition that = (MowerPosition) o;
        return Objects.equals(coordinates, that.coordinates) &&
               orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, orientation);
    }

    @Override
    public String toString() {
        return "MowerPosition{" +
               "coordinates=" + coordinates +
               ", orientation=" + orientation +
               '}';
    }
}
