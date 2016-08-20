package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;

/**
 * Represents an edge (line) on the graph.
 *
 * @author pcampbell
 */
@Getter
public class EdgeElement extends AbstractGraphElement {

    /**
     * Where the arrow is pointing from.
     */
    private final EdgeEndpoint tail;

    /**
     * Where the arrow is pointing to.
     */
    private final EdgeEndpoint head;

    /**
     * Constructor.
     *
     * @param tail          the tail (origin of the edge)
     * @param head          the head (destination of the edge)
     * @param dotFileFormat the output format
     */
    public EdgeElement(
            final EdgeEndpoint tail, final EdgeEndpoint head,
            final DotFileFormat dotFileFormat) {
        super(dotFileFormat);
        this.tail = tail;
        this.head = head;
    }

    @Override
    public final String render() {
        return getDotFileFormat().render(this);
    }
}
