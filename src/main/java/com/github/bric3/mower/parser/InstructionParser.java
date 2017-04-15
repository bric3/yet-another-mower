package com.github.bric3.mower.parser;

import java.text.ParseException;
import java.util.function.BiConsumer;
import com.github.bric3.mower.Lawn;
import com.github.bric3.mower.Mower;
import com.github.bric3.mower.MowerInstructions;

public interface InstructionParser extends AutoCloseable {
    Lawn parseLawn() throws ParseException;

    void parseMowers(BiConsumer<Mower, MowerInstructions> mowerConsumer) throws ParseException;
}
