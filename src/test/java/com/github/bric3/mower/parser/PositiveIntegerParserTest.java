package com.github.bric3.mower.parser;

import java.text.ParseException;
import org.junit.Test;

import static com.github.bric3.mower.parser.PositiveIntegerParser.parseInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PositiveIntegerParserTest {
    @Test
    public void should_parse_int() throws ParseException {
        assertThat(parseInt(2, "0")).isEqualTo(0);
        assertThat(parseInt(2, "3")).isEqualTo(3);
        assertThat(parseInt(2, "10")).isEqualTo(10);
        assertThat(parseInt(2, "2147483647")).isEqualTo(2147483647);
    }

    @Test
    public void should_not_parse_negative_int() throws ParseException {
        assertThatThrownBy(() -> parseInt(2, "-10"))
                .isInstanceOf(ParseException.class)
                .hasMessageContaining("Line 2")
                .hasMessageContaining("positive integer")
                .hasMessageContaining("[0-2147483647]");
    }

    @Test
    public void should_not_parse_bigger_that_integer_max_value() throws ParseException {
        assertThatThrownBy(() -> parseInt(2, "2147483648"))
                .isInstanceOf(ParseException.class)
                .hasMessageContaining("Line 2")
                .hasMessageContaining("positive integer")
                .hasMessageContaining("[0-2147483647]");
        assertThatThrownBy(() -> parseInt(2, "2835792357023750"))
                .isInstanceOf(ParseException.class)
                .hasMessageContaining("Line 2")
                .hasMessageContaining("positive integer")
                .hasMessageContaining("[0-2147483647]");
    }
}
