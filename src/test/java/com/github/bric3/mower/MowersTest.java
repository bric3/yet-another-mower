package com.github.bric3.mower;

import com.github.bric3.mower.parser.InstructionParser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class MowersTest {

    @Test
    public void can_be_started() {
        Mowers mowers = new Mowers(mock(InstructionParser.class), lawn -> { });

        mowers.start();

        assertThat(mowers.isComplete()).isTrue();
        assertThat(mowers.failure()).isEmpty();
    }

    @Test
    public void cannot_be_started_more_than_once() {
        Mowers mowers = new Mowers(mock(InstructionParser.class), lawn -> { });

        mowers.start();

        assertThatThrownBy(mowers::start).isInstanceOf(IllegalStateException.class);
    }
}
