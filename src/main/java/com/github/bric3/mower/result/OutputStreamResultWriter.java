package com.github.bric3.mower.result;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.function.Supplier;
import com.github.bric3.mower.MowerPosition;

public class OutputStreamResultWriter implements ResultWriter {

    private final PrintWriter printWriter;
    private boolean open = true;

    public OutputStreamResultWriter(Supplier<OutputStream> outputStreamSupplier, Charset charset) {
        printWriter = new PrintWriter(new OutputStreamWriter(
                new BufferedOutputStream(outputStreamSupplier.get()),
                charset)
        );
    }

    @Override
    public void writeMowerFinalPosition(MowerPosition finalPosition) {
        canWrite();
        printWriter.printf("%d %d %s%n",
                           finalPosition.coordinates.x,
                           finalPosition.coordinates.y,
                           finalPosition.orientation);
    }

    private void canWrite() {
        if (!open) {
            throw new IllegalStateException("writer closed");
        }
    }

    @Override
    public void close() throws Exception {
        try {
            printWriter.flush();
            printWriter.close();
        } finally {
            open = false;
        }
    }
}
