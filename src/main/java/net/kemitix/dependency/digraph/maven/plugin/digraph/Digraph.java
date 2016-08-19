package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * A GraphViz Directed Graph root node.
 *
 * @author pcampbell
 */
public class Digraph extends AbstractGraphElement implements ElementContainer {

    /**
     * A list of ordered elements.
     *
     * <p>These are in the order that they are to be present in the generate
     * file.</p>
     */
    @Getter
    private final List<GraphElement> elements = new ArrayList<>();

    @Override
    public boolean add(final GraphElement graphElement) {
        return elements.add(graphElement);
    }

    /**
     * Builder for Digraph.
     */
    public static class Builder {

        /**
         * Builds a Digraph with preset properties and node properties.
         *
         * @return a Digraph
         */
        public static Digraph build() {
            Digraph digraph = new Digraph();
            digraph.add(new PropertyElement("compound", "true"));
            final NodeProperties nodeProperties = new NodeProperties();
            nodeProperties.add(new PropertyElement("shape", "box"));
            digraph.add(nodeProperties);
            return digraph;
        }
    }
}
