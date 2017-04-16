package com.github.bric3.mower.result;

import com.github.bric3.mower.MowerPosition;

public interface ResultWriter extends AutoCloseable {
    void writeMowerFinalPosition(MowerPosition finalPosition);
}
