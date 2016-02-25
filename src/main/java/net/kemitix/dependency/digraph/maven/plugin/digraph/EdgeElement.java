package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;

/**
 * Represents an edge (line) on the graph.
 *
 * @author pcampbell
 */
@Getter
public class EdgeElement extends GraphElement {

    private final EdgeEndpoint tail; // where the arrow is pointing from

    private final EdgeEndpoint head; // where the arrow is pointing to

    /**
     * Constructor.
     *
     * @param tail the tail (origin of the edge)
     * @param head the head (destination of the edge)
     */
    public EdgeElement(final EdgeEndpoint tail, final EdgeEndpoint head) {
        this.tail = tail;
        this.head = head;
    }

}
