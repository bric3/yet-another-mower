package com.github.bric3.mower;

import java.util.Arrays;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;

public enum Orientation {
    N(Coordinates::yPlusOne) {
        @Override
        public Orientation left() {
            return W;
        }

        @Override
        public Orientation right() {
            return E;
        }
    },
    E(Coordinates::xPlusOne) {
        @Override
        public Orientation left() {
            return N;
        }

        @Override
        public Orientation right() {
            return S;
        }
    },
    W(Coordinates::xMinusOne) {
        @Override
        public Orientation left() {
            return S;
        }

        @Override
        public Orientation right() {
            return N;
        }
    },
    S(Coordinates::yMinusOne) {
        @Override
        public Orientation left() {
            return E;
        }

        @Override
        public Orientation right() {
            return W;
        }
    };

    public final Function<Coordinates, Coordinates> forwardFunction;

    Orientation(Function<Coordinates, Coordinates> forwardFunction) {
        this.forwardFunction = forwardFunction;
    }

    static String joinedValues() {
        return Arrays.stream(values())
                     .map(Enum::toString)
                     .collect(joining());
    }

    public abstract Orientation left();
    public abstract Orientation right();
}
