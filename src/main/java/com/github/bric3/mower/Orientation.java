package com.github.bric3.mower;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Orientation {

    N,
    E,
    W,
    S;

    static String joinedValues() {
        return Arrays.stream(values()).map(Enum::toString).collect(Collectors.joining());
    }
}
