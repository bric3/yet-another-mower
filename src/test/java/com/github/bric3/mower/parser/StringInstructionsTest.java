package com.github.bric3.mower.parser;

import java.text.ParseException;
import org.junit.Test;

import static com.github.bric3.mower.MowerInstruction.F;
import static com.github.bric3.mower.MowerInstruction.L;
import static com.github.bric3.mower.MowerInstruction.R;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StringInstructionsTest {

    @Test
    public void should_not_accept_null_instruction_string() {
        assertThatThrownBy(() -> new StringInstructions(73, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Line 73")
                .hasMessageContaining("instructions string cannot be 'null'");
    }

    @Test
    public void should_stream_valid_instructions() {
        assertThat(new StringInstructions(3, "LFRL").stream()).containsExactly(L, F, R, L);
    }

    @Test
    public void should_fail_to_stream_on_invalid_instructions() {
        assertThatThrownBy(() -> new StringInstructions(3, "LFz").stream().forEach(i -> {}))
                .hasCauseInstanceOf(ParseException.class);
    }
}
