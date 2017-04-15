package com.github.bric3.mower.parser;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.bric3.mower.Coordinates;
import com.github.bric3.mower.Lawn;

import static java.lang.String.format;

class LawnParser {
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
