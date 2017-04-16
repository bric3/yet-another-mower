package com.github.bric3.mower.cli;

import java.nio.file.Files;
import java.nio.file.Path;
import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

import static java.lang.String.format;

public class FileIsWritable implements IValueValidator<Path> {
    @Override
    public void validate(String name, Path path) throws ParameterException {
        if(Files.exists(path) && !Files.isWritable(path)) {
            throw new ParameterException(format("'%s %s' : cannot be written, check permissions",
                                                name,
                                                path.toAbsolutePath()));
        }
        Path parent = path.toAbsolutePath().getParent();
        if(!Files.exists(parent) || !Files.isWritable(parent)) {
            throw new ParameterException(format("'%s %s' : parent directory '%s' cannot be written, check permissions",
                                                name,
                                                path.toAbsolutePath(),
                                                parent));
        }
    }
}
