package com.github.bric3.mower.result;

import java.io.ByteArrayOutputStream;
import org.junit.Test;

import static com.github.bric3.mower.MowerPosition.mowerPosition;
import static com.github.bric3.mower.Orientation.W;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OutputStreamResultWriterTest {

    @Test
    public void can_write_position_as_line_with_a_new_line() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamResultWriter rw = new OutputStreamResultWriter(() -> outputStream);

        rw.writeMowerFinalPosition(mowerPosition(73, 73, W));
        rw.close();

        assertThat(outputStream.toString("UTF-8")).isEqualTo("73 73 W\n");
    }


    @Test
    public void cannot_write_once_closed() throws Exception {
        OutputStreamResultWriter rw = new OutputStreamResultWriter(ByteArrayOutputStream::new);

        rw.close();

        assertThatThrownBy(() -> rw.writeMowerFinalPosition(mowerPosition(73, 73, W)))
                .isInstanceOf(IllegalStateException.class);

    }
}
