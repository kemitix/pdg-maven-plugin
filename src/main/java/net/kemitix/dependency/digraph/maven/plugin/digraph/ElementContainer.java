package net.kemitix.dependency.digraph.maven.plugin.digraph;

import java.util.Collection;

/**
 * A container of {@link GraphElement} objects.
 *
 * @author pcampbell
 */
public interface ElementContainer {

    /**
     * Returns the contained elements.
     *
     * @return the elements
     */
    Collection<GraphElement> getElements();

    /**
     * Adds an element to the container.
     *
     * @param element the element to add
     *
     * @return true if the element was added
     */
    boolean add(GraphElement element);

}
