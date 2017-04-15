package com.github.bric3.mower.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.text.ParseException;
import java.util.function.Supplier;
import com.github.bric3.mower.Lawn;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DefaultMowersInstructionParser implements InstructionParser {
    private final BufferedReader instructionsReader;
    private boolean open = true;

    public DefaultMowersInstructionParser(Supplier<InputStream> inputStreamSupplier) {
        instructionsReader = new BufferedReader(new InputStreamReader(inputStreamSupplier.get(), UTF_8));
    }

    @Override
    public void close() throws IOException {
        try {
            instructionsReader.close();
        } finally {
            open = false;
        }
    }

    @Override
    public Lawn parseLawn() throws ParseException {
        canParse();
        try {
            String lawnLine = instructionsReader.readLine();
            return LawnParser.parseToLawn(lawnLine);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private void canParse() {
        if (!open) {
            throw new IllegalStateException("parser and  closed");
        }
    }
}
