package com.github.bric3.mower;

public class ErrorReporter {
    private Verbosity verbosity;

    public ErrorReporter(Verbosity verbosity) {
        this.verbosity = verbosity;
    }

    enum Verbosity {
        NOTHING {
            void print(Exception ex) {
                // noop
            }
        },
        PRINT_MESSAGE {
            void print(Exception ex) {
                System.err.println(ex.getMessage());
            }
        },
        PRINT_STACKTRACE {
            void print(Exception ex) {
                ex.printStackTrace(System.err);
            }
        };

        abstract void print(Exception ex);
    }

    public void reportException(Exception ex) {
        verbosity.print(ex);
    }

    public static ErrorReporter defaultReporter() {
        return new ErrorReporter(Verbosity.PRINT_STACKTRACE);
    }
}
