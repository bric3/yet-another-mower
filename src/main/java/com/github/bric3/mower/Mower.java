package com.github.bric3.mower;

import java.util.Objects;
import java.util.Stack;

import static java.lang.String.format;

public class Mower {
    public final int mowerLine;
    private MowerPosition mowerFinalPosition;
    private MowerPosition mowerStartPosition;

    public Mower(int mowerLine,
                 MowerPosition mowerStartPosition) {
        this.mowerLine = mowerLine;
        this.mowerStartPosition = mowerStartPosition;
        this.mowerFinalPosition = mowerStartPosition;
    }

    public void mow(Lawn lawn, MowerInstructions mowerInstructions) {
        if (!lawn.allows(mowerStartPosition.coordinates)) {
            throw new IllegalArgumentException(format("Line %d mower cannot be placed on lawn of size (%s), start position is off (%s)",
                                                      mowerLine,
                                                      lawn.sizeAsString(),
                                                      mowerStartPosition.coordinates));
        }

        mowerFinalPosition = mowerInstructions.stream()
                                              .collect(() -> {
                                                           Stack<MowerPosition> positions = new Stack<>();
                                                           positions.push(mowerStartPosition);
                                                           return positions;
                                                       },
                                                       (previousPosition, command) -> command.apply(previousPosition, lawn),
                                                       (p1, p2) -> {
                                                           throw new UnsupportedOperationException();
                                                       })
                                              .pop();
        System.out.println("final position of " + mowerLine + ":" + mowerFinalPosition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mower mower = (Mower) o;
        return mowerLine == mower.mowerLine &&
               Objects.equals(mowerStartPosition, mower.mowerStartPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mowerLine, mowerStartPosition);
    }

    @Override
    public String toString() {
        return "Mower{" +
               "mowerLine=" + mowerLine +
               '}';
    }
}
