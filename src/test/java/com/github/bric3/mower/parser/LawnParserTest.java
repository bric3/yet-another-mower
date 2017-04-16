package com.github.bric3.mower.parser;

import java.text.ParseException;
import com.github.bric3.mower.Coordinates;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LawnParserTest {

    @Test
    public void should_fail_to_parse_bad_lawn_lines() {
        assertThatThrownBy(() -> LawnParser.parseToLawn("whatever"))
                .isInstanceOf(ParseException.class)
                .hasMessageContaining("Line 1")
                .hasMessageContaining("whatever");
        assertThatThrownBy(() -> LawnParser.parseToLawn("\n"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> LawnParser.parseToLawn("a a"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> LawnParser.parseToLawn("4 a"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> LawnParser.parseToLawn("4 -3"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> LawnParser.parseToLawn("4 3L"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> LawnParser.parseToLawn("4L 3L"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> LawnParser.parseToLawn("3643747236472365972356 3"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> LawnParser.parseToLawn("4L"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> LawnParser.parseToLawn(""))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> LawnParser.parseToLawn(null))
                .isInstanceOf(ParseException.class);
    }

    @Test
    public void should_parse_valid_lawn_line() throws ParseException {
        assertThat(LawnParser.parseToLawn("4 3").upperRightCoordinate)
                .isEqualTo(Coordinates.coordinates(4, 3));
    }
}
