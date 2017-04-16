package com.github.bric3.mower;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.PathConverter;
import com.github.bric3.mower.cli.CharsetValidator;
import com.github.bric3.mower.cli.FileExistsAndIsReadable;
import com.github.bric3.mower.cli.FileIsWritable;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

public class Main {

    private static final int EXECUTED_NORMALLY = 0;
    private static final int GENERAL_ERROR = 1;

    @Parameter(
            names = {"--mowers-input", "-i"},
            required = true,
            converter = PathConverter.class,
            validateValueWith = FileExistsAndIsReadable.class,
            description = "File path of the mower instructions"
    )
    private Path input;

    @Parameter(
            names = "--encoding",
            validateWith = CharsetValidator.class,
            description = "File instructions encoding, default to UTF-8"
    )
    private String inputEncoding = StandardCharsets.UTF_8.displayName();

    @Parameter(
            names = {"--mowers-output", "-o"},
            required = true,
            converter = PathConverter.class,
            validateValueWith = FileIsWritable.class,
            description = "File path of the mower results, overwritten if exists"
    )
    private Path output;

    @Parameter(
            names = {"-h", "-?", "--help"},
            help = true,
            description = "Display this help")
    private boolean help = false;

    public static void main(String... args) throws IOException {
        new Main(args).start();
    }

    private Main(String... args) {
        JCommander jCommander = new JCommander(this);
        jCommander.setProgramName("mower");
        if (help) {
            jCommander.usage();
            System.exit(EXECUTED_NORMALLY);
        }
        try {
            jCommander.parse(args);
        } catch (ParameterException pe) {
            System.out.println("Error: " + pe.getLocalizedMessage());
            System.out.println();
            jCommander.usage();
            System.exit(GENERAL_ERROR);
        }
    }

    private void start() throws IOException {
        Mowers mowers = Mowers.forInstructions(uncheck(() -> Files.newInputStream(input, READ)))
                              .useCharset(Charset.forName(inputEncoding))
                              .output(uncheck(() -> Files.newOutputStream(output, CREATE, TRUNCATE_EXISTING, WRITE)))
                              .mowIt();

        if (mowers.failure().isPresent()) {
            System.exit(GENERAL_ERROR);
        }
    }

    private <T> Supplier<T> uncheck(IOSupplier<T> ioSupplier) {
        return () -> {
            try {
                return ioSupplier.get();
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        };
    }

    private interface IOSupplier<T> {
        T get() throws IOException;
    }
}
