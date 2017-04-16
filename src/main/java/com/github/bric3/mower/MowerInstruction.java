package com.github.bric3.mower;

import java.util.Arrays;
import java.util.Stack;
import java.text.ParseException;

import static java.lang.String.format;
import static com.github.bric3.mower.MowerPosition.mowerPosition;

public enum MowerInstruction {

    L {
        void apply(Stack<MowerPosition> positions, Lawn lawn) {
            MowerPosition previous = positions.pop();
            positions.push(mowerPosition(previous.coordinates, previous.orientation.left()));
        }
    },
    F {
        void apply(Stack<MowerPosition> positions, Lawn lawn) {
            MowerPosition previous = positions.pop();
            Coordinates newCoordinates = previous.orientation.forwardFunction
                                                             .apply(previous.coordinates);
            positions.push(mowerPosition(lawn.allows(newCoordinates) ? newCoordinates : previous.coordinates,
                                         previous.orientation));
        }
    },
    R {
        void apply(Stack<MowerPosition> positions, Lawn lawn) {
            MowerPosition previous = positions.pop();
            positions.push(mowerPosition(previous.coordinates, previous.orientation.right()));
        }
    };

    abstract void apply(Stack<MowerPosition> previousPositions, Lawn lawn);

    public static MowerInstruction parseCommand(int line, int charPosition, char character) throws ParseException {
        // TODO improve ugly code
        return Arrays.stream(MowerInstruction.values())
                     .map(MowerInstruction::toString)
                     .filter(inst -> inst.equals(new String(new char[] {character})))
                     .map(MowerInstruction::valueOf)
                     .findFirst()
                     .orElseThrow(() -> new ParseException(format("Line %d: At position %d, Invalid instruction '%s'",
                                                                  line,
                                                                  charPosition,
                                                                  character),
                                                           0));
    }
}
