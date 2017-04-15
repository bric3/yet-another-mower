package com.github.bric3.mower;

import java.util.Objects;

public class Mower {
    public final int id;
    private MowerPosition mowerStartPosition;

    public Mower(int id,
                 MowerPosition mowerStartPosition) {
        this.id = id;
        this.mowerStartPosition = mowerStartPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mower mower = (Mower) o;
        return id == mower.id &&
               Objects.equals(mowerStartPosition, mower.mowerStartPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mowerStartPosition);
    }

    @Override
    public String toString() {
        return "Mower{" +
               "id=" + id +
               '}';
    }
}
