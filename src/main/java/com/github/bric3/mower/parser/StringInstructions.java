package com.github.bric3.mower.parser;

import java.util.Objects;
import com.github.bric3.mower.MowerInstructions;

class StringInstructions implements MowerInstructions {
    private final int line;
    private final String instructions;

    StringInstructions(int line, String instructions) {
        this.line = line;
        this.instructions = instructions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringInstructions that = (StringInstructions) o;
        return line == that.line &&
               Objects.equals(instructions, that.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, instructions);
    }

    @Override
    public String toString() {
        return "StringInstructions{" +
               "line=" + line +
               ", instructions='" + instructions + '\'' +
               '}';
    }
}
