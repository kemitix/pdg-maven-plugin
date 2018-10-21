package net.kemitix.pdg.maven;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests to confirm the behaviour of the Deque implementation.
 *
 * @author pcampbell
 */
public class QueueTest {

    /**
     * When pulling the contents from a Queue using forEach, then the order
     * should be in the reverse to which they were added.
     */
    @Test
    public void shouldDequeInReverseOrderToThatAdded() {
        //given
        final String item1 = "First Item";
        final String item2 = "Second Item";
        final Deque<String> queue = new ArrayDeque<>();
        final List<String> readOrder = new ArrayList<>();
        //when
        queue.push(item1);
        queue.push(item2);
        //then
        readOrder.addAll(queue);
        assertThat(readOrder).containsExactly(item2, item1);
    }
}
