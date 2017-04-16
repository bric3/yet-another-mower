package com.github.bric3.mower.parser;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicBoolean;
import com.github.bric3.mower.Mower;
import org.junit.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static com.github.bric3.mower.Lawn.lawn;
import static com.github.bric3.mower.MowerInstruction.F;
import static com.github.bric3.mower.MowerInstruction.L;
import static com.github.bric3.mower.MowerInstruction.R;
import static com.github.bric3.mower.MowerPosition.mowerPosition;
import static com.github.bric3.mower.Orientation.N;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

public class InputStreamInstructionParserTest {

    @Test
    public void cannot_be_used_after_close() throws Exception {
        InputStreamInstructionParser instructionParser = parser(new byte[0]);

        instructionParser.close();

        assertThatThrownBy(instructionParser::parseLawn).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void should_parse_mowers() throws ParseException {
        InputStreamInstructionParser instructionParser = parser("1 1 N\nLFR".getBytes(UTF_8));

        AtomicBoolean invoked = new AtomicBoolean(false);
        instructionParser.parseMowers((mower, instruction) -> {
            invoked.set(true);
            assertThat(mower).isEqualTo(new Mower(2, mowerPosition(1, 1, N)));
            assertThat(instruction.stream()).containsExactly(L, F, R);
        });

        assertThat(invoked.get()).isTrue();
    }

    @Test
    public void should_fail_to_parse_incomplete_mower_line() throws ParseException {
        InputStreamInstructionParser instructionParser = parser("1 X N\nLFR".getBytes(UTF_8));

        assertThatThrownBy(() -> instructionParser.parseMowers((mower, instruction) -> fail("should not be invoked"))).isInstanceOf(ParseException.class);
    }

    @Test
    public void should_treat_no_mower_instruction_line_as_empty_instruction_line() throws ParseException {
        InputStreamInstructionParser instructionParser = parser("1 1 N".getBytes(UTF_8));

        AtomicBoolean invoked = new AtomicBoolean(false);
        instructionParser.parseMowers((mower, instruction) -> {
            invoked.set(true);
            assertThat(mower).isEqualTo(new Mower(2, mowerPosition(1, 1, N)));
            assertThat(instruction.stream()).isEmpty();
        });

        assertThat(invoked.get()).isTrue();
    }

    @Test
    public void should_allow_empty_mower_instruction_line() throws ParseException {
        InputStreamInstructionParser instructionParser = parser("1 1 N\n".getBytes(UTF_8));

        AtomicBoolean invoked = new AtomicBoolean(false);
        instructionParser.parseMowers((mower, instruction) -> {
            invoked.set(true);
            assertThat(mower).isEqualTo(new Mower(2, mowerPosition(1, 1, N)));
            assertThat(instruction.stream()).isEmpty();
        });

        assertThat(invoked.get()).isTrue();
    }

    @Test
    public void should_parse_lawn() throws ParseException {
        InputStreamInstructionParser instructionParser = parser("1 1\n".getBytes(UTF_8));

        assertThat(instructionParser.parseLawn()).isEqualTo(lawn(1, 1));
    }

    private InputStreamInstructionParser parser(byte[] bytes) {
        return new InputStreamInstructionParser(() -> new ByteArrayInputStream(bytes));
    }


}
