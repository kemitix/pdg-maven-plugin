package net.kemitix.dependency.digraph.maven.plugin;

import net.kemitix.dependency.digraph.maven.plugin.digraph.Digraph;
import net.kemitix.dependency.digraph.maven.plugin.digraph.EdgeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.NodeElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.NodeProperties;
import net.kemitix.dependency.digraph.maven.plugin.digraph.PropertyElement;
import net.kemitix.dependency.digraph.maven.plugin.digraph.Subgraph;

/**
 * Interface for report generators in a specific format.
 *
 * @author pcampbell
 */
public interface DotFileFormat {

    /**
     * Generates the report.
     *
     * @return the formatted report
     */
    String renderReport();

    /**
     * Render the digraph element as a string.
     *
     * @param digraph the digraph to render
     *
     * @return the rendered digraph
     */
    String render(Digraph digraph);

    /**
     * Render the edge element as a string.
     *
     * @param edgeElement the edge element to render
     *
     * @return the rendered edge element
     */
    String render(EdgeElement edgeElement);

    /**
     * Render the node element as a string.
     *
     * @param nodeElement the node element to render
     *
     * @return the rendered node element
     */
    String render(NodeElement nodeElement);

    /**
     * Render the node properties as a string.
     *
     * @param nodeProperties the node properties to render
     *
     * @return the rendered node properties
     */
    String render(final NodeProperties nodeProperties);

    /**
     * Render the property element as a string.
     *
     * @param propertyElement the property element to render
     *
     * @return the rendered property element
     */
    String render(final PropertyElement propertyElement);

    /**
     * Render the subgraph as a string.
     *
     * @param subgraph the subgraph to render
     *
     * @return the rendered subgraph
     */
    String render(final Subgraph subgraph);
}
