package com.github.bric3.mower.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import com.github.bric3.mower.Lawn;
import com.github.bric3.mower.Mower;
import com.github.bric3.mower.MowerInstructions;
import com.github.bric3.mower.MowerParser;

import static java.nio.charset.StandardCharsets.UTF_8;

public class InputStreamInstructionParser implements InstructionParser {
    private final BufferedReader instructionsReader;
    private boolean open = true;

    public InputStreamInstructionParser(Supplier<InputStream> inputStreamSupplier) {
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

    @Override
    public void parseMowersAndThen(BiConsumer<Mower, MowerInstructions> mowerConsumer) throws ParseException {
        canParse();
        int lineCount = 2;
        try {
            while (instructionsReader.ready()) {
                String mowerStartingPosition = instructionsReader.readLine();
                String mowerInstructions = instructionsReader.readLine();

                mowerConsumer.accept(MowerParser.parseToMower(lineCount,
                                                              mowerStartingPosition),
                                     new StringInstructions(lineCount + 1,
                                                            Objects.toString(mowerInstructions, "")));
                
                lineCount += 2;
            }
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }

    }

    private void canParse() {
        if (!open) {
            throw new IllegalStateException("parser closed");
        }
    }
}
