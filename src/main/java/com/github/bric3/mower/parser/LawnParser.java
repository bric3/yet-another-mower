package com.github.bric3.mower.parser;

import java.text.ParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.bric3.mower.Coordinates;
import com.github.bric3.mower.Lawn;

import static java.lang.String.format;
import static com.github.bric3.mower.parser.PositiveIntegerParser.parseInt;

class LawnParser {
    private static final Pattern LAWN_UPPER_RIGHT_COORDINATES = Pattern.compile("(?<x>\\d+) (?<y>\\d+)");

    static Lawn parseToLawn(String lawnLine) throws ParseException {
        Matcher matcher = LAWN_UPPER_RIGHT_COORDINATES.matcher(Objects.toString(lawnLine, ""));
        if (matcher.matches()) {
            return new Lawn(new Coordinates(parseInt(1, matcher.group("x")),
                                            parseInt(1, matcher.group("y"))));
        }
        throw new ParseException(format("Line 1: ('%s') failed to parse Lawn`, expected two positive integers separated by a space e.g. '11 2'",
                                        lawnLine),
                                 1);
    }

}
