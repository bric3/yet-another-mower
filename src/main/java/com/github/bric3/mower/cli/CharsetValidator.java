package com.github.bric3.mower.cli;

import java.nio.charset.Charset;
import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import static java.lang.String.format;

public class CharsetValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!Charset.isSupported(value)) {
            throw new ParameterException(format("Charset '%s' not supported", value));
        }
    }
}
