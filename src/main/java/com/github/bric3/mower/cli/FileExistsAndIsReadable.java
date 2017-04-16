package com.github.bric3.mower.cli;

import java.nio.file.Files;
import java.nio.file.Path;
import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

import static java.lang.String.format;

public class FileExistsAndIsReadable implements IValueValidator<Path> {
    @Override
    public void validate(String name, Path path) throws ParameterException {
        if (!Files.isRegularFile(path)
            || !Files.isReadable(path)) {
            throw new ParameterException(format("'%s %s' : path should exist and has to be readable",
                                                name,
                                                path.toAbsolutePath()));
        }
    }
}
