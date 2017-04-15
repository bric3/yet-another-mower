package com.github.bric3.mower;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.github.bric3.mower.parser.InputStreamInstructionParser;
import com.github.bric3.mower.parser.InstructionParser;

/**
 * This is the mowers engine.
 *
 * It can be passed an instruction parser that will parse
 * mowers instructions.
 *
 * Can be created via the builder API {@link Mowers#forInstructions(Supplier)}.
 */
class Mowers {
    private final InstructionParser instructionParser;
    private final Consumer<Lawn> onLawnInitialization;
    private boolean complete = false;
    private Exception failure;
    private ErrorReporter reporter = ErrorReporter.defaultReporter();

    Mowers(InstructionParser instructionParser,
           Consumer<Lawn> onLawnInitialization) {
        this.instructionParser = instructionParser;
        this.onLawnInitialization = onLawnInitialization;
    }

    /**
     * Fluent builder API to give instructions and customize {@link Mowers}
     * @param is InputStream of the instructions supplier
     * @return the builder
     */
    static MowersBuilder forInstructions(Supplier<InputStream> is) {
        return new MowersBuilder().instructions(is);
    }

    /**
     * Indicates whether or not the mowers run has been completed or not.
     * Note this may not indicate if there was an error or not during the run,
     * check {@link #failure} for that.
     * @return true if complete, false if not
     */
    boolean isComplete() {
        return complete;
    }

    /**
     * Return the exception after invoking {@link #start()}
     * @return an Optional that will be either empty or contain an exception.
     */
    Optional<Exception> failure() {
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
        try(InstructionParser ip = instructionParser) {
            // Initialize Lawn // TODO enforce pars method orders / refactor
            Lawn lawn = ip.parseLawn();
            onLawnInitialization.accept(lawn);

            // Ongoing mower instruction parser
            ip.parseMowers((mower, mowerInstructions) -> System.out.println(mower + "\n" + mowerInstructions));

            complete = true;
        } catch (Exception ex) {
            reporter.reportException(ex);
            failure = ex;
            return this;
        }
        return this;
    }

    /**
     * The Mowers builder
     */
    static class MowersBuilder {
        private Supplier<InputStream> instructionIS;
        private Consumer<Lawn> lawnConsumer;

        /**
         * Defines where to find the instructions InputStream
         * @param is the instruction InputStream supplier
         * @return this
         */
        MowersBuilder instructions(Supplier<InputStream> is) {
            this.instructionIS = is;
            return this;
        }

        /**
         * Callback on new Lawn, once parsed.
         * @param lawnConsumer the callback
         * @return this
         */
        MowersBuilder onLawnLine(Consumer<Lawn> lawnConsumer) {
            this.lawnConsumer = lawnConsumer;
            return this;
        }

        /**
         * Create {@link Mowers} and starts it via {@link Mowers#start()}
         * @return the new Mowers
         */
        Mowers mowIt() {
            return new Mowers(new InputStreamInstructionParser(instructionIS),
                              lawnConsumer)
                    .start();
        }
    }
}
