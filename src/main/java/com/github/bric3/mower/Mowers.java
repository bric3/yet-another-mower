package com.github.bric3.mower;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.github.bric3.mower.parser.InputStreamInstructionParser;
import com.github.bric3.mower.parser.InstructionParser;
import com.github.bric3.mower.result.OutputStreamResultWriter;
import com.github.bric3.mower.result.ResultWriter;

/**
 * This is the mowers engine.
 *
 * It can be passed an instruction parser that will parse
 * mowers instructions.
 *
 * Can be created via the builder API {@link Mowers#forInstructions(Supplier)}.
 */
public class Mowers {
    private final InstructionParser instructionParser;
    private final ResultWriter resultWriter;
    private final Consumer<Lawn> onLawnInitialization;
    private final BiConsumer<Mower, MowerInstructions> onNewMower;
    private final ErrorReporter reporter = ErrorReporter.defaultReporter();
    private boolean complete = false;
    private Exception failure;

    Mowers(InstructionParser instructionParser,
           ResultWriter resultWriter,
           Consumer<Lawn> onLawnSetup,
           BiConsumer<Mower, MowerInstructions> onNewMower) {
        this.instructionParser = Objects.requireNonNull(instructionParser);
        this.resultWriter = resultWriter;
        this.onLawnInitialization = Objects.requireNonNull(onLawnSetup);
        this.onNewMower = Objects.requireNonNull(onNewMower);
    }

    /**
     * Fluent builder API to give instructions and customize {@link Mowers}
     * @param is InputStream of the instructions supplier
     * @return the builder
     */
    public static MowersBuilder forInstructions(Supplier<InputStream> is) {
        return new MowersBuilder().instructions(is);
    }

    /**
     * Indicates whether or not the mowers run has been completed or not.
     * Note this may not indicate if there was an error or not during the run,
     * check {@link #failure} for that.
     * @return true if complete, false if not
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Return the exception after invoking {@link #start()}
     * @return an Optional that will be either empty or contain an exception.
     */
    public Optional<Exception> failure() {
        return Optional.ofNullable(failure);
    }

    /**
     * Starts the mowers
     * @return this
     */
    Mowers start() {
        if(complete) {
            throw new IllegalStateException("Already completed, recreate a new one!");
        }
        try(InstructionParser ip = instructionParser;
            ResultWriter rw = resultWriter) {
            // Initialize Lawn // TODO enforce parse methods order / refactor
            Lawn lawn = ip.parseLawn();
            onLawnInitialization.accept(lawn);

            // Ongoing mower instruction parser
            ip.parseMowersAndThen((mower, mowerInstructions) -> {
                onNewMower.accept(mower, mowerInstructions);
                mower.mow(lawn, mowerInstructions);
                rw.writeMowerFinalPosition(mower.getFinalPosition());
            });

            
        } catch (Exception ex) {
            reporter.reportException(ex);
            failure = ex;
            return this;
        } finally {
            complete = true;
        }
        return this;
    }

    /**
     * The Mowers builder
     */
    public static class MowersBuilder {
        private Supplier<InputStream> instructionInputStreamSupplier;
        private Supplier<OutputStream> outputStreamSupplier = () -> new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // /dev/null
            }
        };
        private Consumer<Lawn> lawnConsumer = (lawn) -> {};
        private BiConsumer<Mower, MowerInstructions> mowerConsumer = (m, inst) -> {};

        /**
         * Defines where to find the instructions InputStream
         * @param is the instruction InputStream supplier
         * @return this
         */
        public MowersBuilder instructions(Supplier<InputStream> is) {
            this.instructionInputStreamSupplier = is;
            return this;
        }

        /**
         * Callback on new Lawn, once parsed.
         * @param onLawnSetup the callback
         * @return this
         */
        public MowersBuilder onLawnSetup(Consumer<Lawn> onLawnSetup) {
            this.lawnConsumer = onLawnSetup;
            return this;
        }

        /**
         * Callback on new Mower and instruction set, once parsed.
         * @param onNewMower the callback
         * @return this
         */
        public MowersBuilder onNewMower(BiConsumer<Mower, MowerInstructions> onNewMower) {
            this.mowerConsumer = onNewMower;
            return this;
        }

        /**
         * Defines where the result will be written to
         * @param outputStreamSupplier
         * @return this
         */
        public MowersBuilder output(Supplier<OutputStream> outputStreamSupplier) {
            this.outputStreamSupplier = outputStreamSupplier;
            return this;
        }

        /**
         * Create {@link Mowers} and starts it via {@link Mowers#start()}
         * @return the new Mowers
         */
        public Mowers mowIt() {
            return new Mowers(new InputStreamInstructionParser(instructionInputStreamSupplier),
                              new OutputStreamResultWriter(outputStreamSupplier),
                              lawnConsumer,
                              mowerConsumer)
                    .start();
        }
    }
}
