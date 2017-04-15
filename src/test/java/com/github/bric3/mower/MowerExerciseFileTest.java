package com.github.bric3.mower;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static com.github.bric3.mower.Coordinates.coordinates;
import static org.assertj.core.api.Assertions.assertThat;

public class MowerExerciseFileTest {

    private String EXERCISE_INPUT_FILE = "5 5\n" +
                                         "1 2 N\n" +
                                         "LFLFLFLFF\n" +
                                         "3 3 E\n" +
                                         "FFRFFRFRRF\n";

    private String EXPECTED_OUTPUT = "1 3 N\n" +
                                    "5 1 E\n";


    @Test
    public void should_read_first_line_as_lawn_size() {
        // Given
        ByteArrayInputStream is = new ByteArrayInputStream(EXERCISE_INPUT_FILE.getBytes(UTF_8));

        // When
        Mowers mowers = Mowers.forInstructions(() -> is)
                              .onLawnLine(lawn -> assertThat(lawn.upperRightCoordinate).isEqualTo(coordinates(5, 5)))
                              .mowIt();

        // Then
        assertThat(mowers.isComplete()).isTrue();
        assertThat(mowers.failure()).isEmpty();
    }

    @Test
    public void should_fail_when_first_line_cannot_be_arsed_as_lawn() {
        // Given
        ByteArrayInputStream is = new ByteArrayInputStream("-5 5\n...".getBytes(Charset.defaultCharset()));

        // When
        Mowers mowers = Mowers.forInstructions(() -> is)
                              .onLawnLine(lawn -> Assertions.fail("should not be executed"))
                              .mowIt();

        // Then
        assertThat(mowers.isComplete()).isFalse();
        assertThat(mowers.failure().get()).isInstanceOf(ParseException.class)
                                          .hasMessageContaining("failed to parse")
                                          .hasMessageContaining("");
    }
}


