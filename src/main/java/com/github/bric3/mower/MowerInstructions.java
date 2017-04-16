package com.github.bric3.mower;

import java.util.stream.Stream;

public interface MowerInstructions {
    Stream<MowerInstruction> stream();
}
