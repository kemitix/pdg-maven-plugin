package net.kemitix.dependency.digraph.maven.plugin;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        queue.forEach((String item) -> readOrder.add(item));
        assertThat(readOrder.get(0), is(item2));
        assertThat(readOrder.get(1), is(item1));
    }
}
