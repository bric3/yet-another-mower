package com.github.bric3.mower;

import java.text.ParseException;
import java.util.Stack;
import org.junit.Test;

import static com.github.bric3.mower.MowerInstruction.F;
import static com.github.bric3.mower.MowerInstruction.L;
import static com.github.bric3.mower.MowerInstruction.R;
import static com.github.bric3.mower.MowerPosition.mowerPosition;
import static com.github.bric3.mower.Orientation.E;
import static com.github.bric3.mower.Orientation.N;
import static com.github.bric3.mower.Orientation.S;
import static com.github.bric3.mower.Orientation.W;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MowerInstructionTest {

    @Test
    public void should_turn_left() {
        assertThat(applyInstruction(L, mowerPosition(1, 3, N)).pop()).isEqualTo(mowerPosition(1, 3, W));
        assertThat(applyInstruction(L, mowerPosition(1, 3, E)).pop()).isEqualTo(mowerPosition(1, 3, N));
        assertThat(applyInstruction(L, mowerPosition(1, 3, W)).pop()).isEqualTo(mowerPosition(1, 3, S));
        assertThat(applyInstruction(L, mowerPosition(1, 3, S)).pop()).isEqualTo(mowerPosition(1, 3, E));
    }

    @Test
    public void should_turn_right() {
        assertThat(applyInstruction(R, mowerPosition(1, 3, N)).pop()).isEqualTo(mowerPosition(1, 3, E));
        assertThat(applyInstruction(R, mowerPosition(1, 3, E)).pop()).isEqualTo(mowerPosition(1, 3, S));
        assertThat(applyInstruction(R, mowerPosition(1, 3, W)).pop()).isEqualTo(mowerPosition(1, 3, N));
        assertThat(applyInstruction(R, mowerPosition(1, 3, S)).pop()).isEqualTo(mowerPosition(1, 3, W));
    }

    @Test
    public void should_move_forward() {
        assertThat(applyInstruction(F, mowerPosition(1, 3, N), lawn(6, 6)).pop()).isEqualTo(mowerPosition(1, 4, N));
        assertThat(applyInstruction(F, mowerPosition(1, 3, E), lawn(6, 6)).pop()).isEqualTo(mowerPosition(2, 3, E));
        assertThat(applyInstruction(F, mowerPosition(1, 3, W), lawn(6, 6)).pop()).isEqualTo(mowerPosition(0, 3, W));
        assertThat(applyInstruction(F, mowerPosition(1, 3, S), lawn(6, 6)).pop()).isEqualTo(mowerPosition(1, 2, S));
    }

    @Test
    public void should_not_move_forward_if_on_lawn_boundary() {
        assertThat(applyInstruction(F, mowerPosition(1, 6, N), lawn(6, 6)).pop()).isEqualTo(mowerPosition(1, 6, N));
        assertThat(applyInstruction(F, mowerPosition(6, 3, E), lawn(6, 6)).pop()).isEqualTo(mowerPosition(6, 3, E));
        assertThat(applyInstruction(F, mowerPosition(0, 3, W), lawn(6, 6)).pop()).isEqualTo(mowerPosition(0, 3, W));
        assertThat(applyInstruction(F, mowerPosition(1, 0, S), lawn(6, 6)).pop()).isEqualTo(mowerPosition(1, 0, S));
    }

    @Test
    public void can_parse() throws ParseException {
        assertThat(MowerInstruction.parseCommand(3, 1, 'F')).isEqualTo(MowerInstruction.F);
        assertThat(MowerInstruction.parseCommand(3, 2, 'L')).isEqualTo(MowerInstruction.L);
        assertThat(MowerInstruction.parseCommand(3, 3, 'R')).isEqualTo(MowerInstruction.R);


        assertThatThrownBy(() -> MowerInstruction.parseCommand(3, 73, 'z'))
                .isInstanceOf(ParseException.class)
                .hasMessageContaining("Line 3: At position 73")
                .hasMessageContaining("Invalid instruction 'z'");


    }

    @Test
    public void lower_case_instructions_not_supported() {
        assertThatThrownBy(() -> MowerInstruction.parseCommand(3, 1, 'f')).isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> MowerInstruction.parseCommand(3, 1, 'l')).isInstanceOf(ParseException.class);
        assertThatThrownBy(() -> MowerInstruction.parseCommand(3, 1, 'r')).isInstanceOf(ParseException.class);
    }

    private Stack<MowerPosition> applyInstruction(MowerInstruction instruction, MowerPosition position) {
        return applyInstruction(instruction, position, lawn(6, 6));
    }

    private Stack<MowerPosition> applyInstruction(MowerInstruction instruction, MowerPosition position, Lawn lawn) {
        Stack<MowerPosition> positions = stack(position);
        instruction.apply(positions, lawn);
        return positions;
    }

    private Lawn lawn(int x, int y) {
        return new Lawn(Coordinates.coordinates(x, y));
    }

    private Stack<MowerPosition> stack(MowerPosition position) {
        Stack<MowerPosition> positions = new Stack<>();
        positions.push(position);
        return positions;
    }
}
