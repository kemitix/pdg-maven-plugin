package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;

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

    /**
     * Constructor.
     *
     * @param format the output format
     */
    public Digraph(final DotFileFormat format) {
        super(format);
    }

    @Override
    public boolean add(final GraphElement graphElement) {
        return elements.add(graphElement);
    }

    /**
     * Builder for Digraph.
     */
    public static class Builder {

        private final DotFileFormat format;

        /**
         * Constructor.
         *
         * @param format the output format
         */
        public Builder(final DotFileFormat format) {
            this.format = format;
        }

        /**
         * Builds a Digraph with preset properties and node properties.
         *
         * @return a Digraph
         */
        public Digraph build() {
            Digraph digraph = new Digraph(format);
            digraph.add(new PropertyElement("compound", "true", format));
            final NodeProperties nodeProperties = new NodeProperties(format);
            nodeProperties.add(new PropertyElement("shape", "box", format));
            digraph.add(nodeProperties);
            return digraph;
        }
    }
}
