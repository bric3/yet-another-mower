package com.github.bric3.mower;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.github.bric3.mower.parser.DefaultMowersInstructionParser;
import com.github.bric3.mower.parser.InstructionParser;

public class Mowers {
    private final InstructionParser instructionParser;
    private final Consumer<Lawn> onLawnInitialization;
    private boolean complete = false;
    private Exception failure;
    private ErrorReporter reporter = ErrorReporter.defaultReporter();

    public Mowers(InstructionParser instructionParser,
                  Consumer<Lawn> onLawnInitialization) {
        this.instructionParser = instructionParser;
        this.onLawnInitialization = onLawnInitialization;
    }

    public static MowersBuilder forInstructions(Supplier<InputStream> is) {
        return new MowersBuilder().instructions(is);
    }

    public boolean isComplete() {
        return complete;
    }

    public Optional<Exception> failure() {
        return Optional.ofNullable(failure);
    }

    private Mowers mowIt() {
        try(InstructionParser ip = instructionParser) {
            Lawn lawn = ip.parseLawn();
            onLawnInitialization.accept(lawn);
            complete = true;
        } catch (Exception ex) {
            reporter.reportException(ex);
            failure = ex;
            return this;
        }
        return this;
    }

    public static class MowersBuilder {
        private Supplier<InputStream> instructionIS;
        private Consumer<Lawn> lawnConsumer;

        public MowersBuilder instructions(Supplier<InputStream> is) {
            this.instructionIS = is;
            return this;
        }

        public MowersBuilder onLawnLine(Consumer<Lawn> lawnConsumer) {
            this.lawnConsumer = lawnConsumer;
            return this;
        }

        public Mowers mowIt() {
            return new Mowers(new DefaultMowersInstructionParser(instructionIS),
                              lawnConsumer)
                    .mowIt();
        }
    }
}
