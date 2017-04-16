package com.github.bric3.mower.parser;

import java.text.ParseException;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import com.github.bric3.mower.MowerInstruction;
import com.github.bric3.mower.MowerInstructions;

import static java.lang.String.format;

class StringInstructions implements MowerInstructions {
    private final int line;
    private final String instructions;

    StringInstructions(int line, String instructions) {
        this.line = line;
        this.instructions = Objects.requireNonNull(instructions,
                                                   format("Line %d: instructions string cannot be 'null'", line));
    }

    @Override
    public Stream<MowerInstruction> stream() {
        return instructions.chars()
                           .mapToObj(new IntFunction<MowerInstruction>() {
                               int charPositionCounter = 1;
                               @Override
                               public MowerInstruction apply(int character) {
                                   try {
                                       return MowerInstruction.parseCommand(line, charPositionCounter++, (char) character);
                                   } catch (ParseException e) {
                                       throw new RuntimeException(e);
                                   }
                               }
                           });
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
