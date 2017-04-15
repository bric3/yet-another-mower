package com.github.bric3.mower;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.text.ParseException;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

public class DefaultMowersInstructionParser implements InstructionParser {
    private final BufferedReader instructionsReader;

    public DefaultMowersInstructionParser(Supplier<InputStream> inputStreamSupplier) {
        instructionsReader = new BufferedReader(new InputStreamReader(inputStreamSupplier.get(), UTF_8));
    }

    @Override
    public void close() throws IOException {
        instructionsReader.close();
    }

    @Override
    public Lawn parseLawn() throws ParseException {
        try {
            String lawnLine = instructionsReader.readLine();
            return LawnParser.parseToLawn(lawnLine);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    private static class LawnParser {
        private static final Pattern LAWN_UPPER_RIGHT_COORDINATES = Pattern.compile("(?<x>\\d+) (?<y>\\d+)");

        static Lawn parseToLawn(String lawnLine) throws ParseException {
            Matcher matcher = LAWN_UPPER_RIGHT_COORDINATES.matcher(lawnLine);
            if (matcher.matches()) {
                return new Lawn(new Coordinates(Integer.valueOf(matcher.group("x")),
                                                Integer.valueOf(matcher.group("y"))));
            }
            throw new ParseException(format("Line 0 ('%s') failed to parse, expected to positive number separated by a space e.g. '11 2'",
                                            lawnLine),
                                     0);
        }
    }
}
