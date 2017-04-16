package com.github.bric3.mower.acceptance;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Stack;
import com.github.bric3.mower.Mower;
import com.github.bric3.mower.Mowers;
import org.junit.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static com.github.bric3.mower.Coordinates.coordinates;
import static com.github.bric3.mower.MowerPosition.mowerPosition;
import static com.github.bric3.mower.Orientation.E;
import static com.github.bric3.mower.Orientation.N;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MowerExerciseFileTest {
    
    private String EXPECTED_OUTPUT = "1 3 N\n" +
                                     "5 1 E\n";


    @Test
    public void should_read_first_line_as_lawn_size() {
        // Given
        ByteArrayInputStream is = new ByteArrayInputStream("5 5\n1 2 N\nLFLFLFLFF\n".getBytes(UTF_8));

        // When
        Mowers mowers = Mowers.forInstructions(() -> is)
                              .onLawnSetup(lawn -> assertThat(lawn.upperRightCoordinate).isEqualTo(coordinates(5, 5)))
                              .mowIt();

        // Then
        assertThat(mowers.isComplete()).isTrue();
        assertThat(mowers.failure()).isEmpty();
    }

    @Test
    public void should_fail_when_first_line_cannot_be_parsed_as_lawn() {
        // Given
        ByteArrayInputStream is = new ByteArrayInputStream("-5 5\n...".getBytes(Charset.defaultCharset()));

        // When
        Mowers mowers = Mowers.forInstructions(() -> is)
                              .onLawnSetup(lawn -> fail("should not be invoked"))
                              .mowIt();

        // Then
        assertThat(mowers.isComplete()).isTrue();
        assertThat(mowers.failure().get()).isInstanceOf(ParseException.class)
                                          .hasMessageContaining("Line 1")
                                          .hasMessageContaining("failed to parse")
                                          .hasMessageContaining("");
    }

    @Test
    public void should_read_first_mower_line_as_expected() {
        // Given
        ByteArrayInputStream is = new ByteArrayInputStream("5 5\n1 2 N\nLFLFLFLFF\n".getBytes(UTF_8));

        // When
        Mowers mowers = Mowers.forInstructions(() -> is)
                              .onNewMower((mower, instructions) -> {
                                  assertThat(mower).isEqualTo(new Mower(2, mowerPosition(1, 2  , N)));
                              })
                              .mowIt();

        // Then
        assertThat(mowers.isComplete()).isTrue();
        assertThat(mowers.failure()).isEmpty();
    }

    @Test
    public void should_read_multiple_mowers_line_as_expected() {
        // Given
        ByteArrayInputStream is = new ByteArrayInputStream("5 5\n1 2 N\nLFLFLFLFF\n3 3 E\nFFRFFRFRRF\n".getBytes(UTF_8));
        Stack<Mower> mowerStack = new Stack<>();

        // When
        Mowers mowers = Mowers.forInstructions(() -> is)
                              .onNewMower((mower, instructions) -> mowerStack.push(mower))
                              .mowIt();

        // Then
        assertThat(mowerStack).containsExactly(new Mower(2, mowerPosition(1, 2  , N)),
                                               new Mower(4, mowerPosition(3, 3  , E)));
        assertThat(mowers.isComplete()).isTrue();
        assertThat(mowers.failure()).isEmpty();
    }

    @Test
    public void should_fail_when_mower_is_missing_instructions() {
        // Given
        ByteArrayInputStream is = new ByteArrayInputStream("5 5\n1 2 N".getBytes(UTF_8));

        // When
        Mowers mowers = Mowers.forInstructions(() -> is)
                              .onNewMower((mower, instructions) -> fail("should not be invoked"))
                              .mowIt();

        // Then
        assertThat(mowers.isComplete()).isTrue();
        assertThat(mowers.failure().get()).isInstanceOf(NullPointerException.class)
                                          .hasMessageContaining("Line 3")
                                          .hasMessageContaining("instructions");
    }

    @Test
    public void should_fail_when_mower_setup_cannot_be_parsed_as_mower() {
        // Given
        ByteArrayInputStream is = new ByteArrayInputStream("5 5\n1 2 nein\nLF".getBytes(UTF_8));

        // When
        Mowers mowers = Mowers.forInstructions(() -> is)
                              .onNewMower((mower, instructions) -> fail("should not be invoked"))
                              .mowIt();

        // Then
        assertThat(mowers.isComplete()).isTrue();
        assertThat(mowers.failure().get()).isInstanceOf(ParseException.class)
                                          .hasMessageContaining("Line 2")
                                          .hasMessageContaining("failed to parse")
                                          .hasMessageContaining("");
    }
}


