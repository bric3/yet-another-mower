package com.github.bric3.mower.parser;

import java.text.ParseException;
import com.github.bric3.mower.Lawn;

public interface InstructionParser extends AutoCloseable {
    Lawn parseLawn() throws ParseException;
}
