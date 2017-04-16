package com.github.bric3.mower.parser;

import java.text.ParseException;

import static java.lang.String.format;

class PositiveIntegerParser {
    static int parseInt(int line, String numberString) throws ParseException {
        // avoiding NumberFormatException
        if (numberString.length() <= 10) {
            long parsedLong = Long.parseLong(numberString);
            if(0 <=  parsedLong && parsedLong <= Integer.MAX_VALUE) {
                return (int) parsedLong;
            }
        }
        throw new ParseException(format("Line %d: ('%s') failed to parse integer`, expected positive integer [0-2147483647]",
                                        line,
                                        numberString),
                                 1);
    }
}
