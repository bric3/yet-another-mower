package com.github.bric3.mower;

import java.text.ParseException;

public interface InstructionParser extends AutoCloseable {
    Lawn parseLawn() throws ParseException;
}
