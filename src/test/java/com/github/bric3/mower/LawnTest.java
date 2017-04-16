package com.github.bric3.mower;

import org.junit.Test;

import static com.github.bric3.mower.Coordinates.coordinates;
import static org.assertj.core.api.Assertions.assertThat;

public class LawnTest {

    @Test
    public void should_validate_coordinates() {
        assertThat(new Lawn(coordinates(5, 5)).allows(coordinates(0, 0))).isTrue();
        assertThat(new Lawn(coordinates(5, 5)).allows(coordinates(5, 5))).isTrue();
        assertThat(new Lawn(coordinates(5, 5)).allows(coordinates(5, 0))).isTrue();
        assertThat(new Lawn(coordinates(5, 5)).allows(coordinates(0, 5))).isTrue();
        assertThat(new Lawn(coordinates(5, 5)).allows(coordinates(3, 3))).isTrue();
        assertThat(new Lawn(coordinates(5, 5)).allows(coordinates(6, 1))).isFalse();
        assertThat(new Lawn(coordinates(5, 5)).allows(coordinates(1, 6))).isFalse();
    }
}
