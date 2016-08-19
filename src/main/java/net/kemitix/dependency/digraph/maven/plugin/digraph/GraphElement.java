package net.kemitix.dependency.digraph.maven.plugin.digraph;

/**
 * An element appearing in a Dot Graph.
 *
 * @author pcampbell
 */
public interface GraphElement {

    /**
     * Render the graph element for use in a DotFile.
     *
     * @return the rendered graph element
     */
    String render();
}
