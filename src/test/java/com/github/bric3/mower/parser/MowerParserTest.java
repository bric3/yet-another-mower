package com.github.bric3.mower.parser;

import java.text.ParseException;
import com.github.bric3.mower.Mower;
import com.github.bric3.mower.MowerPosition;
import org.junit.Test;

import static com.github.bric3.mower.parser.MowerParser.parseToMower;
import static com.github.bric3.mower.Orientation.N;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MowerParserTest {

    @Test
    public void should_parse_mower_with_two_lines() throws ParseException {
        assertThat(parseToMower(73,
                                "1 2 N"))
                .isEqualTo(new Mower(73,
                                     MowerPosition.mowerPosition(1, 2, N)));

    }

    @Test
    public void should_fail_to_parse_bad_mowers() {
        assertThatThrownBy(() -> parseToMower(73, "1 -2 N"))
                .isInstanceOf(ParseException.class)
                .hasMessageContaining("Line 73")
                .hasMessageContaining("1 -2 N");
        assertThatThrownBy(() -> parseToMower(73, "1 -2 N"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> parseToMower(73, "a 2 N"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> parseToMower(73, "1"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> parseToMower(73, "-1 2 N"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> parseToMower(73, "1 2 Q"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> parseToMower(73, "1 25723570457405704 E"))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> parseToMower(73, ""))
                .isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> parseToMower(73, null))
                .isInstanceOf(ParseException.class);
    }
}
