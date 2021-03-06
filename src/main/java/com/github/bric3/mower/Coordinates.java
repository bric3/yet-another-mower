package com.github.bric3.mower;

import java.util.Objects;

public class Coordinates {
    public final int x;
    public final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinates coordinates(int x, int y) {
        return new Coordinates(x, y);
    }

    public Coordinates xMinusOne() {
        return new Coordinates(x - 1, y);
    }

    public Coordinates yPlusOne() {
        return new Coordinates(x, y + 1);
    }

    public Coordinates yMinusOne() {
        return new Coordinates(x, y - 1);
    }

    public Coordinates xPlusOne() {
        return new Coordinates(x + 1, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x &&
               y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
               "x=" + x +
               ", y=" + y +
               '}';
    }
}
