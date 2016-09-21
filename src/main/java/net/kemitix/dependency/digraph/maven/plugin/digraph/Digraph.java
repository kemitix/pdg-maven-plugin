package net.kemitix.dependency.digraph.maven.plugin.digraph;

import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import net.kemitix.dependency.digraph.maven.plugin.DotFileFormat;

/**
 * A GraphViz Directed Graph root node.
 *
 * @author pcampbell
 */
@Immutable
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
     * @param dotFileFormat the output format
     */
    public Digraph(final DotFileFormat dotFileFormat) {
        super(dotFileFormat);
    }

    @Override
    public final boolean add(final GraphElement graphElement) {
        return elements.add(graphElement);
    }

    @Override
    public final String render() {
        return getDotFileFormat().render(this);
    }

    /**
     * Builder for Digraph.
     */
    public static class Builder {

        private final DotFileFormat dotFileFormat;

        /**
         * Constructor.
         *
         * @param dotFileFormat the output format
         */
        public Builder(final DotFileFormat dotFileFormat) {
            this.dotFileFormat = dotFileFormat;
        }

        /**
         * Builds a Digraph with preset properties and node properties.
         *
         * @return a Digraph
         */
        public final Digraph build() {
            val digraph = new Digraph(dotFileFormat);
            digraph.add(new PropertyElement("compound", "true", dotFileFormat));
            val nodeProperties = new NodeProperties(dotFileFormat);
            nodeProperties.add(
                    new PropertyElement("shape", "box", dotFileFormat));
            digraph.add(nodeProperties);
            return digraph;
        }
    }
}
