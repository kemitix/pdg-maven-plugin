package net.kemitix.pdg.maven;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

class QueueTest implements WithAssertions {

    /**
     * When pulling the contents from a Queue using forEach, then the order
     * should be in the reverse to which they were added.
     */
    @Test
    void shouldDequeInReverseOrderToThatAdded() {
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
