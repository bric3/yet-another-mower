package com.github.bric3.mower.parser;

import java.io.ByteArrayInputStream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InputStreamInstructionParserTest {

    @Test
    public void cannot_be_used_after_close() throws Exception {
        InputStreamInstructionParser instructionParser = new InputStreamInstructionParser(() -> new ByteArrayInputStream(new byte[0]));

        instructionParser.close();

        assertThatThrownBy(instructionParser::parseLawn).isInstanceOf(IllegalStateException.class);
    }
}
