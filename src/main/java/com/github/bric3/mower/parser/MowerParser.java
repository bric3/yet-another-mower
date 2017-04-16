package com.github.bric3.mower.parser;

import java.text.ParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.bric3.mower.Mower;
import com.github.bric3.mower.MowerPosition;
import com.github.bric3.mower.Orientation;

import static java.lang.String.format;
import static com.github.bric3.mower.parser.PositiveIntegerParser.parseInt;

class MowerParser {
    private static final Pattern MOWER_POSITION = Pattern.compile("(?<x>\\d+) (?<y>\\d+) (?<orientation>[" + Orientation.joinedValues() + "])");

    static Mower parseToMower(int createdAtLine,
                              String mowerStartingPosition) throws ParseException {


        return new Mower(createdAtLine,
                         parseToMowerPosition(createdAtLine, mowerStartingPosition));
    }

    private static MowerPosition parseToMowerPosition(int line, String position) throws ParseException {
        Matcher matcher = MOWER_POSITION.matcher(Objects.toString(position, ""));
        if (matcher.matches()) {
            return new MowerPosition(parseInt(line, matcher.group("x")),
                                     parseInt(line, matcher.group("y")),
                                     Orientation.valueOf(matcher.group("orientation")));
        }
        throw new ParseException(format("Line %d: ('%s') failed to parse Mower, expected two positive numbers followed by an orientation letter either N, E, W or S all separated by a space e.g. '11 2 N'",
                                        line,
                                        position),
                                 1);
    }
}
